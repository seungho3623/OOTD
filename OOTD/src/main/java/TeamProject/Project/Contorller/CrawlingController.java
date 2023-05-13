package TeamProject.Project.Contorller;

import TeamProject.Project.Dto.CoordiDTO;
import TeamProject.Project.Dto.CrawlingRequestDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CrawlingController {
    private static WebDriver driver;
    private static WebDriverWait wait;

    private static int index;
    private static final String[][] sequence = {
            {"캐주얼", "남성"}, {"포멀", "남성"}, {"아메카지", "남성"}, {"스트릿", "남성"}, {"스포츠", "남성"}, {"고프코어", "남성"},
            {"캐주얼", "여성"}, {"포멀", "여성"}, {"아메카지", "여성"}, {"스트릿", "여성"}, {"스포츠", "여성"}, {"고프코어", "여성"}
    };
    private static List<CoordiDTO>[] coordiData = new ArrayList[12];

    public static void setDriver() {
        //맥
        System.setProperty("webdriver.chrome.driver", "OOTD/src/main/resources/bin/chromedriver");

        //윈도우
        //System.setProperty("webdriver.chrome.driver", "OOTD/src/main/resources/bin/chromedriver.exe");

        //옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");   //브라우저 안 띄움
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private static void stopDriver() {
        driver.close();    //탭 닫기
        driver.quit();    //브라우저 닫기
    }

    private static void setStyle(String style) {
        String url = "https://www.musinsa.com/app/codimap/lists?style_type=";

        switch(style) {
            case "캐주얼": url += "casual";
                break;
            case "포멀": url += "formal";
                break;
            case "아메카지": url += "americancasual";
                break;
            case "스트릿": url += "street";
                break;
            case "고프코어": url += "gorpcore";
                break;
            case "스포츠": url += "sports";
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
        int index = 0;

        for(String[] seq: sequence) {
            if((dto.getStyle().equals(seq[0])) && (dto.getGender().equals(seq[1])))
                return coordiData[index];

            index ++;
        }

        return coordiData[0];
    }

    @Scheduled(cron = "0 * * * * *")
    private static void getCoordiDataAuto() {
        List<CoordiDTO> coordiList = new ArrayList<>();
        List<String> coordiNames, coordiThumbnails, coordiLinks;

        System.out.println("\n크롤링 시작\n");

        setStyle(sequence[index][0]);
        setGender(sequence[index][1]);

        try {
            coordiNames = getCoordiNames();
            coordiThumbnails = getCoordiThumbnails();
            coordiLinks = getCoordiLinks();

            for (int i = 0; i < coordiNames.size(); i++) {
                coordiList.add(new CoordiDTO(coordiNames.get(i), coordiThumbnails.get(i), coordiLinks.get(i)));
                getCoordiDetail(coordiList.get(i));

                System.out.println("\n" + (i + 1) + "번째 코디 정보");

                System.out.println(coordiList.get(i).getName());
                System.out.println(coordiList.get(i).getUrl());
                System.out.println(coordiList.get(i).getDescription());
                System.out.println(coordiList.get(i).getThumbnail());
                for(String url: coordiList.get(i).getItemThumbnails()) {
                    System.out.println(url);
                }
            }
            coordiData[index] = coordiList;
            if(++ index >= sequence.length) index = 0;

            System.out.println("\n크롤링 완료\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void getCoordiDetail(CoordiDTO coordi) throws InterruptedException {
        driver.get(coordi.getUrl());
        coordi.setDescription(getCoordiDescription());
        coordi.setItemThumbnails(getCoordiItemThumbnail(3));
    }
}
