package TeamProject.Project.Dto;

import lombok.Data;

import java.util.List;

@Data
public class WeatherItemsDTO {
    private List<WeatherItemDTO> item;

    private int numOfRows;

    private int pageNo;

    private int totalCount;
}