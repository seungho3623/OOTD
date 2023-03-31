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
}
