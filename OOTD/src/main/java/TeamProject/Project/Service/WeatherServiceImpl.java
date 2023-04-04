package TeamProject.Project.Service;

import TeamProject.Project.Dto.AreaRequestDTO;
import TeamProject.Project.Mapper.WeatherMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WeatherServiceImpl implements WeatherService
{
    private final WeatherMapper weatherMapper;

    public WeatherServiceImpl(WeatherMapper weatherMapper) {
        this.weatherMapper = weatherMapper;
    }

    @Override
    public List<AreaRequestDTO> getArea(Map<String, String> params){
        return this.weatherMapper.selectArea(params);
    }
}
