package TeamProject.Project.Service;

import TeamProject.Project.Dto.AreaRequestDTO;

import java.util.List;
import java.util.Map;

public interface WeatherService {
    List<AreaRequestDTO> getArea(Map<String, String> params);
}
