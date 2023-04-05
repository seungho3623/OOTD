package TeamProject.Project.Service;

import TeamProject.Project.Dto.AreaRequestDTO;
import TeamProject.Project.Dto.WeatherApiResponseDTO;
import TeamProject.Project.Dto.WeatherDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface WeatherService
{
    List<WeatherDTO> getWeather(AreaRequestDTO areaRequestDTO) throws UnsupportedEncodingException, URISyntaxException, JsonMappingException, JsonProcessingException;

    List<AreaRequestDTO> getArea(Map<String, String> params);

    AreaRequestDTO getCoordinate(String areacode);

    ResponseEntity<WeatherApiResponseDTO> requestWeatherApi(AreaRequestDTO areaRequestDTO) throws UnsupportedEncodingException, URISyntaxException;
}
