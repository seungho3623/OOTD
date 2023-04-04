package TeamProject.Project.Mapper;

import TeamProject.Project.Dto.AreaRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
public interface WeatherMapper {
    List<AreaRequestDTO> selectArea(Map<String, String> params);
}
