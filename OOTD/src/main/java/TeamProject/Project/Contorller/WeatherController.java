package TeamProject.Project.Contorller;

import TeamProject.Project.Dto.AreaRequestDTO;
import TeamProject.Project.Dto.WeatherDTO;
import TeamProject.Project.Service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@Controller
public class WeatherController
{
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping(value = "/Project/getWeather.do")
    @ResponseBody
    public List<WeatherDTO> getWeatherInfo(@ModelAttribute AreaRequestDTO areaRequestDTO) throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException, URISyntaxException
    {
        AreaRequestDTO coordinate = this.weatherService.getCoordinate(areaRequestDTO.getAreacode());
        areaRequestDTO.setNx(coordinate.getNx());
        areaRequestDTO.setNy(coordinate.getNy());

        List<WeatherDTO> weatherList = this.weatherService.getWeather(areaRequestDTO);
        return weatherList;
    }

    @PostMapping(value = "/Project/weatherStep.do")
    @ResponseBody
    public List<AreaRequestDTO> getAreaStep(@RequestParam Map<String, String> params)
    {
        return this.weatherService.getArea(params);
    }
}