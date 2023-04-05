package TeamProject.Project.Dto;

import lombok.Data;

@Data
public class WeatherBodyDTO
{
    private String          dataType;

    private WeatherItemsDTO items;
}