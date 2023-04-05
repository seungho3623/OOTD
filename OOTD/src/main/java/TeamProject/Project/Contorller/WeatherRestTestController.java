package TeamProject.Project.Contorller;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WeatherRestTestController {

    private final EntityManager em;

    @GetMapping("/weatherApi")
    public String callApiWithJson() throws IOException, ParseException {
        //Get Date
        LocalDate now = LocalDate.now();

        //Init Format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        //Apply Format
        String formatterNow = now.format(formatter);

        String apiUrl = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        // 홈페이지에서 받은 키
        String serviceKey = "6wwQhBuKz6tg4wGAnMPF19o1eTOE4N7fyCFgBMhdb1eUPYZjWiBvaaM90Zrqr6FisEYu3MJOjDowhuOivARgZA%3D%3D";
        String nx = "60";	//위도
        String ny = "125";	//경도
        String baseDate = formatterNow;	//조회하고싶은 날짜
        String baseTime = "0200";	//조회하고싶은 시간
        String type = "json";	//타입 xml, json 등등 ..

        StringBuilder urlBuilder = new StringBuilder(apiUrl); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("200", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*예보지점의 Y 좌표값*/

        URL url = new URL(urlBuilder.toString());
        System.out.println(url);
        BufferedReader bf;
        String line = "";
        String result = "";
        //Get Weather Info
        bf = new BufferedReader(new InputStreamReader(url.openStream()));
        //Transfer String
        while((line = bf.readLine()) != null)
        {
            result = result.concat(line);
        }
        //Json Parser String Object
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(result);

        //Top Level Response Key Data Parsing
        JSONObject parse_response = (JSONObject) obj.get("response");
        //Response Get Body
        JSONObject parse_body = (JSONObject) parse_response.get("body");
        //Response Get Items
        JSONObject parse_items = (JSONObject) parse_body.get("items");
        //Items Get ItemList
        JSONArray parse_item = (JSONArray) parse_items.get("item");

        String category;
        String obsrValue;
        JSONObject weather;

        //Get Use Data
        for(int i = 0; i < parse_item.size(); i++)
        {
            weather = (JSONObject) parse_item.get(i);
            obsrValue = (String) weather.get("obsrValue");
            category = (String) weather.get("category");
            System.out.println(i + "번째 요소");
            System.out.println("category : " + category);
            System.out.println("obsrValue : " + obsrValue);
            System.out.println();
        }

        bf.close();

        return result;
    }

}
