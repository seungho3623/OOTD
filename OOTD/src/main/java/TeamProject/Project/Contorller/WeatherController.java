package TeamProject.Project.Contorller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class WeatherController {
    @RequestMapping(value = "/home")
    public String home(){
        return "index.html";
    }

    @Value("${resources.location}")
    private String resourceLocation;

    @PostMapping("/region")
    public ResponseEntity<String> resetRegionList() {
        String fileLocation = resourceLocation + "/weatherData.csv"; // 설정파일에 설정된 경로 뒤에 붙인다
        Path path = Paths.get(fileLocation);
        URI uri = path.toUri();
        return null;
    }
}
