package TeamProject.Project.Contorller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping(value = "/Project/ootd.do")
    public String openMainPage() {
        return "/html/MainPage.html";
    }
}
