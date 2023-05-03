package TeamProject.Project.Mapper;

import TeamProject.Project.Dto.AreaRequestDTO;
import TeamProject.Project.Dto.WeatherDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
public interface WeatherMapper {
    List<AreaRequestDTO> selectArea(Map<String, String> params);

    AreaRequestDTO selectCoordinate(String areacode);

    List<WeatherDTO> selectSameCoordinateWeatherList(AreaRequestDTO areaRequestDTO);

    void insertWeatherList(List<WeatherDTO> weatherList);
}