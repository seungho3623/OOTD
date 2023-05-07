package TeamProject.Project.Contorller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @GetMapping(value = "/Project/ootd.do")
    public String openMainPage() {
        return "/html/MainPage.html";
    }

    @GetMapping(value = "/Project/loding.do")
    public String openLodingPage() {
        return "/html/Loding.html";
    }

    @GetMapping(value = "/Project/outfit.do")
    public String openOutfitPage() {
        return "/html/DDZA_1.html";
    }

    @GetMapping(value = "/Project/detail.do")
    public String openDetailPage(@RequestParam(required = true) int index) {
        return "/html/DDZA_2.html?index=" + index;
    }
}
