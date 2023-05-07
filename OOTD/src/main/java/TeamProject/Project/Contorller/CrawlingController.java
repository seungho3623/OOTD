package TeamProject.Project.Contorller;

import TeamProject.Project.Dto.CoordiDTO;
import TeamProject.Project.Dto.CrawlingRequestDTO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CrawlingController {
    private static WebDriver driver;
    private static WebDriverWait wait;

    private static void setDriver() {
        /**
         * 크롬 드라이버 설정
         **/

        /**
         * 맥
         **/
        System.setProperty("webdriver.chrome.driver", "src/main/resources/bin/chromedriver");

        /**
         * 윈도우
         **/
        //System.setProperty("webdriver.chrome.driver", "OOTD/src/main/resources/bin/chromedriver.exe");

        // 옵션 설정
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("headless");   // 브라우저 안 띄움
        options.addArguments("--remote-allow-origins=*");

        //브라우저 선택
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private static void stopDriver() {
        driver.close();    //탭 닫기
        driver.quit();    //브라우저 닫기
    }

    private static void setStyle(String style) {
        String url = "https://www.musinsa.com/app/codimap/lists";

        switch(style) {
            case "캐주얼": url += "?style_type=casual";
                break;
            case "레트로": url += "?style_type=retro";
                break;
            case "홈웨어": url += "?style_type=homewear";
                break;
            case "스트릿": url += "?style_type=street";
                break;
            case "로맨틱": url += "?style_type=romantic";
                break;
            case "스포츠": url += "?style_type=sports";
                break;
            default:
                break;
        }

        System.out.println("\n스타일 : " + style);
        System.out.println("url : " + url);

        driver.get(url);
    }

    private static void setGender(String gender) {
        String className = "";

        switch(gender) {
            case "남성": className = "global-filter__button--mensinsa";
                break;
            case "여성": className = "global-filter__button--wusinsa";
                break;
            default:
                break;
        }

        System.out.println("\n성별 : " + gender);
        System.out.println("className : " + className);

        WebElement button = driver.findElement(By.className(className));
        button.click();
    }

    private static List<String> getCoordiNames() throws InterruptedException {
        List<String> list = new ArrayList<>();

        List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("style-list-information__link")));
        for (WebElement element : elements) {
            list.add(element.getAttribute("title"));
        }

        return list;
    }

    private static List<String> getCoordiThumbnails() throws InterruptedException {
        final String url = "https:";
        final String empty_src = "data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";

        List<String> list = new ArrayList<>();
        List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("style-list-thumbnail")));

        for(WebElement element : elements) {
            String src = element.findElement(By.tagName("img")).getAttribute("src");
            if(src.equals(empty_src)) src = url + element.findElement(By.tagName("img")).getAttribute("data-original");

            list.add(src);
        }

        return list;
    }

    private static List<String> getCoordiLinks() throws InterruptedException {
        List<String> list = new ArrayList<>();
        final String url = "https://www.musinsa.com/app/codimap/views/";

        List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("style-list-information__title")));
        for (WebElement element : elements) {
            String link = element.getAttribute("onclick");
            list.add(url + link.substring(link.indexOf("'") + 1, link.lastIndexOf("'")));
        }

        return list;
    }

    private static String getCoordiDescription() throws InterruptedException {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.className("styling_txt"))).getText();
    }

    private static List<String> getCoordiItemThumbnail(int count) throws InterruptedException {
        List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("styling_img")));
        List<String> list = new ArrayList<>();

        for(int i = 0; i < count; i++) {
            list.add(elements.get(i).findElement(By.tagName("img")).getAttribute("src"));
        }

        return list;
    }

    @ResponseBody
    @PostMapping("/Project/getCoordi")
    private static List<CoordiDTO> getCoordiData(@ModelAttribute CrawlingRequestDTO dto) {
        List<CoordiDTO> coordiList = new ArrayList<>();
        List<String> coordiNames, coordiThumbnails, coordiLinks;

        System.out.println("\n크롤링 시작\n");
        setDriver();

        setStyle(dto.getStyle());
        setGender(dto.getGender());

        try {
            coordiNames = getCoordiNames();
            coordiThumbnails = getCoordiThumbnails();
            coordiLinks = getCoordiLinks();

            //for (int i = 0; i < coordiNames.size(); i++) {
            for (int i = 0; i < 3; i++) {
                coordiList.add(new CoordiDTO(coordiNames.get(i), coordiThumbnails.get(i), coordiLinks.get(i)));
                getCoordiDetail(coordiList.get(i));

                System.out.println("\n" + (i + 1) + "번째 코디 정보");

                System.out.println(coordiList.get(i).getName());
                System.out.println(coordiList.get(i).getDescription());
                System.out.println(coordiList.get(i).getUrl());
                System.out.println(coordiList.get(i).getThumbnail());
                for(String url: coordiList.get(i).getItemThumbnails()) {
                    System.out.println(url);
                }
            }

            System.out.println("\n크롤링 완료\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stopDriver();

        return coordiList;
    }

    private static void getCoordiDetail(CoordiDTO coordi) throws InterruptedException {
        driver.get(coordi.getUrl());
        coordi.setDescription(getCoordiDescription());
        coordi.setItemThumbnails(getCoordiItemThumbnail(3));
    }
}
