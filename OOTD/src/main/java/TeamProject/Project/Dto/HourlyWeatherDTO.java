package TeamProject.Project.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

enum SKY_CODE{
    SUNNY, CLOUDY, OVERCAST,ERROR
}

enum PRECIPITATION_CODE{
    NO_PRECIPITATION, RAIN, RAIN_SNOW, SNOW, SHOWER, RAINDROP, RAINDROP_SNOW_DRIFT, SNOW_DRIFT, ERROR
}

@Getter
@Setter
@Data
public class HourlyWeatherDTO {
    private final static String KOREAN_DATE_TIME_FORMAT = "yyyy년 MM월 dd일 HH시 mm분";
    // 예보 대상 날짜
    private LocalDateTime forecastDate;
    // 예보 발표 날짜
    private LocalDateTime announceDate;
    // 기온(℃)
    private double temperature;
    // 습도(%)
    private int humidity;
    // 하늘 코드
    private SKY_CODE sky;
    // 강수 코드
    private PRECIPITATION_CODE precipitation;
    // 풍향(degree)
    private int windDirection;
    // 풍속(m/s)
    private double windSpeed;
    // 강수량
    private String rainScale;
    // 강수확률 probability of precipitation
    private int pop;

    public void setSky(String sky) {
        this.sky = switch (sky){
            case "1" -> SKY_CODE.SUNNY;
            case "3" -> SKY_CODE.CLOUDY;
            case "4" -> SKY_CODE.OVERCAST;
            default -> SKY_CODE.ERROR;
        };
    }

    public void setPrecipitation(String ptyCode) {
        this.precipitation = switch (ptyCode) {
            case "0" -> PRECIPITATION_CODE.NO_PRECIPITATION;
            case "1" -> PRECIPITATION_CODE.RAIN;
            case "2" -> PRECIPITATION_CODE.RAIN_SNOW;
            case "3" -> PRECIPITATION_CODE.SNOW;
            case "4" -> PRECIPITATION_CODE.SHOWER;
            case "5" -> PRECIPITATION_CODE.RAINDROP;
            case "6" -> PRECIPITATION_CODE.RAINDROP_SNOW_DRIFT;
            case "7" -> PRECIPITATION_CODE.SNOW_DRIFT;
            default -> PRECIPITATION_CODE.ERROR;
        };
    }

    public String getPrecipitationString(){
        String result = switch (this.precipitation) {
            case NO_PRECIPITATION -> "강수없음";
            case RAIN -> "비";
            case RAIN_SNOW -> "비/눈";
            case SNOW -> "눈";
            case SHOWER -> "소나기";
            case RAINDROP -> "빗방울";
            case RAINDROP_SNOW_DRIFT -> "빗방울/눈날림";
            case SNOW_DRIFT -> "눈날림";
            case ERROR -> "강수코드 에러";
        };
        return result;
    }

    public String getSkyString() {
        String result = switch (this.sky) {
            case SUNNY -> "맑음";
            case CLOUDY -> "구름많음";
            case OVERCAST -> "흐림";
            case ERROR -> "하늘코드 에러";
        };
        return result;
    }

    public void printWeather() {
        System.out.println("기온: " + temperature + "°C");
        System.out.println("강수상태: " + getPrecipitationString());
        System.out.println("강수량: " + rainScale);
        System.out.println("하늘상태: " + getSkyString());
        System.out.println("습도: " + humidity + "%");
        System.out.println("풍향: " + windDirection + "°");
        System.out.println("풍속: " + windSpeed + "m/s");
    }
}
