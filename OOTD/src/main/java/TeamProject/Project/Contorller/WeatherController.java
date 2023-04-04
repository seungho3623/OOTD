package TeamProject.Project.Contorller;

import TeamProject.Project.Dto.AreaRequestDTO;
import TeamProject.Project.Service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class WeatherController
{
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping(value = "/Project/main.do")
    public String openMainPage()
    {
        return "/html/DongDongZoA-1.html";
    }
    @GetMapping(value = "/Project/weather.do")
    public String openWeatherPage()
    {
        return "/index.html";
    }
    @PostMapping(value = "/Project/weatherStep.do")
    @ResponseBody
    public List<AreaRequestDTO> getAreaStep(@RequestParam Map<String, String> params)
    {
        return this.weatherService.getArea(params);
    }
}