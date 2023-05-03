package TeamProject.Project.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.sound.sampled.DataLine;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class DailyWeatherDTO {
    //예보 대상 날짜
    private LocalDateTime forecastDate;
    //예보 발표 날짜
    private LocalDateTime announceDate;
    private int tempMax;
    private int tempMin;

    public DailyWeatherDTO(LocalDateTime announceDate) {
        this.announceDate = announceDate;
    }
}
