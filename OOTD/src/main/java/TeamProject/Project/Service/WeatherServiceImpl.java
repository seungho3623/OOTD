package TeamProject.Project.Service;

import TeamProject.Project.Dto.*;
import TeamProject.Project.Mapper.WeatherMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {
    private final WeatherMapper weatherMapper;

    public WeatherServiceImpl(WeatherMapper weatherMapper) {
        this.weatherMapper = weatherMapper;
    }

    @Override
    public List<WeatherDTO> getWeather(AreaRequestDTO areaRequestDTO) throws UnsupportedEncodingException, URISyntaxException, JsonMappingException, JsonProcessingException {
        List<WeatherDTO> weatherList = this.weatherMapper.selectSameCoordinateWeatherList(areaRequestDTO); // 날짜, 시간, nx, ny가 requestDTO의 값과 일치하는 데이터가 있는지 검색
        List<WeatherDTO> weatherTempList = getDailyTemp(areaRequestDTO);
        if (weatherList.isEmpty()) {
            ResponseEntity<WeatherApiResponseDTO> response = requestWeatherApi(areaRequestDTO); // 데이터가 하나도 없는 경우 새로 생성
            ObjectMapper objectMapper = new ObjectMapper();
            List<WeatherItemDTO> weatherItemList = response.getBody()
                    .getResponse()
                    .getBody()
                    .getItems()
                    .getItem();
            for (WeatherItemDTO item : weatherItemList) {
                weatherList.add(objectMapper.readValue(objectMapper.writeValueAsString(item), WeatherDTO.class));
            }
            weatherList.addAll(weatherTempList);
            this.weatherMapper.insertWeatherList(weatherList); // API요청 후 결과값을 DB에 저장
            return weatherList;    // 로그를 찍지 않으려면 삭제해도 OK
        }
        return weatherList;    // DB에 기존 저장되어있는 값에서 가져온 List
    }

    @Override
    public List<AreaRequestDTO> getArea(Map<String, String> params) {
        return this.weatherMapper.selectArea(params);
    }

    @Override
    public AreaRequestDTO getCoordinate(String areacode) {
        return this.weatherMapper.selectCoordinate(areacode);
    }

    @Override
    public ResponseEntity<WeatherApiResponseDTO> requestWeatherApi(AreaRequestDTO areaRequestDTO) throws UnsupportedEncodingException, URISyntaxException {
        String url = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        String serviceKey = "6wwQhBuKz6tg4wGAnMPF19o1eTOE4N7fyCFgBMhdb1eUPYZjWiBvaaM90Zrqr6FisEYu3MJOjDowhuOivARgZA%3D%3D";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "JSON", Charset.forName("UTF-8")));

        StringBuilder builder = new StringBuilder(url);
        builder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        builder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        builder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("12", "UTF-8"));
        builder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        builder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(areaRequestDTO.getBaseDate(), "UTF-8"));
        builder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(areaRequestDTO.getBaseTime(), "UTF-8"));
        builder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(areaRequestDTO.getNx(), "UTF-8"));
        builder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(areaRequestDTO.getNy(), "UTF-8"));
        URI uri = new URI(builder.toString());

        ResponseEntity<WeatherApiResponseDTO> response;
        response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<String>(headers), WeatherApiResponseDTO.class);
        return response;
    }

    //Get Max, Min Temp Method
    @Override
    public List<WeatherDTO> getDailyTemp(AreaRequestDTO areaRequestDTO) throws NullPointerException{
        final int HOUR_OF_DAY = 24;
        final int NUMBER_PER_DATE_SHORT_FORECAST = 12;

        // 해당 지역 격자 좌표
        final String POS_X = areaRequestDTO.getNx();
        final String POS_Y = areaRequestDTO.getNy();

        //서비스 키
        final String API_URL = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        final String SERVICE_KEY = "6wwQhBuKz6tg4wGAnMPF19o1eTOE4N7fyCFgBMhdb1eUPYZjWiBvaaM90Zrqr6FisEYu3MJOjDowhuOivARgZA%3D%3D";

        // 속성 키
        final String PROPERTY_FORECAST_VALUE = "fcstValue";
        final String PROPERTY_CATEGORY = "category";
        final String PROPERTY_TEMPERATURE = "TMP";

        // 마지막 단기예보 발표시각(하루 전 2300) 구하기
        LocalDateTime baseDate = LocalDateTime.now().minusDays(1).withHour(23).withMinute(0);

        final int NUMBER_REQUEST = HOUR_OF_DAY * NUMBER_PER_DATE_SHORT_FORECAST;

        String dataType = "json";    //타입 xml, json
        String numberOfRows = Integer.toString(NUMBER_REQUEST);
        String urlBuilder = API_URL + "?" + URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8) + "=" + SERVICE_KEY +
                "&" + URLEncoder.encode("nx", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(POS_X, StandardCharsets.UTF_8) + //경도
                "&" + URLEncoder.encode("ny", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(POS_Y, StandardCharsets.UTF_8) + //위도
                "&" + URLEncoder.encode("base_date", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(baseDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")), StandardCharsets.UTF_8) + /* 조회하고싶은 날짜*/
                "&" + URLEncoder.encode("base_time", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(baseDate.format(DateTimeFormatter.ofPattern("HHmm")), StandardCharsets.UTF_8) + /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
                "&" + URLEncoder.encode("dataType", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(dataType, StandardCharsets.UTF_8) +    /* 타입 */
                "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(numberOfRows, StandardCharsets.UTF_8);    /* 한 페이지 결과 수 */


        // 어제 발표된 단기예보 결과
        JSONArray basetimeSrtFcstResult;
        WeatherDTO tempMaxDTO = new WeatherDTO();
        WeatherDTO tempMinDTO = new WeatherDTO();
        List<WeatherDTO> weatherDTOList = new ArrayList<>();
        DailyWeatherDTO dailyWeatherDTO = new DailyWeatherDTO(baseDate);
        // 단기예보를 통해 오늘 최고/최저 기온 조회
        try {
            // 단기예보 요청하기
            URL url = new URL(urlBuilder);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            String data = sb.toString();

            // Json parser를 만들어 만들어진 문자열 데이터를 객체화
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(data);
            // response 키를 가지고 데이터를 파싱
            JSONObject parse_response = (JSONObject) obj.get("response");
            // response 로 부터 body 찾기
            JSONObject parse_body = (JSONObject) parse_response.get("body");
            // body 로 부터 items 찾기
            JSONObject parse_items = (JSONObject) parse_body.get("items");
            // items 로 부터 item 찾기
            basetimeSrtFcstResult = (JSONArray) parse_items.get("item");
            
            if (basetimeSrtFcstResult.size() == NUMBER_REQUEST) {
                ArrayList<Integer> tempList = new ArrayList<>();
                for (int i = 0; i < NUMBER_REQUEST; i++) {
                    JSONObject dataLine = (JSONObject) basetimeSrtFcstResult.get(i);
                    if (dataLine.get(PROPERTY_CATEGORY).equals(PROPERTY_TEMPERATURE)) {
                        tempList.add(Integer.parseInt(dataLine.get(PROPERTY_FORECAST_VALUE).toString()));
                    }
                }
                // 데이터 길이 체크
                if (tempList.size() == HOUR_OF_DAY) {
                    dailyWeatherDTO.setTempMax(Collections.max(tempList));
                    dailyWeatherDTO.setTempMin(Collections.min(tempList));
                    ArrayList<Integer> tempListDay = new ArrayList<>(tempList.subList(0, HOUR_OF_DAY));
                    dailyWeatherDTO.setTempMax(Collections.max(tempListDay));
                    dailyWeatherDTO.setTempMin(Collections.min(tempListDay));
                    LocalDateTime todayDate = baseDate.plusDays(1);
                    dailyWeatherDTO.setForecastDate(todayDate);
                    tempMaxDTO.setNx(POS_X);
                    tempMaxDTO.setNy(POS_Y);
                    tempMaxDTO.setCategory("DTX");
                    tempMaxDTO.setBaseDate(todayDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    tempMaxDTO.setBaseTime("2300");
                    tempMaxDTO.setFcstValue(String.valueOf(dailyWeatherDTO.getTempMax()));
                    weatherDTOList.add(tempMaxDTO);
                    tempMinDTO.setNx(POS_X);
                    tempMinDTO.setNy(POS_Y);
                    tempMinDTO.setCategory("DTN");
                    tempMinDTO.setBaseDate(todayDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    tempMinDTO.setBaseTime("2300");
                    tempMinDTO.setFcstValue(String.valueOf(dailyWeatherDTO.getTempMin()));
                    weatherDTOList.add(tempMinDTO);
                    return weatherDTOList;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("오늘 최고/최저 기온 정보를 불러오지 못했습니다.");
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
