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
        return "/html/LoadingPage.html";
    }

    @GetMapping(value = "/Project/outfit.do")
    public String openOutfitPage(@RequestParam(required = true) int page) {
        return "/html/OutfitPage.html?page=" + page;
    }

    @GetMapping(value = "/Project/detail.do")
    public String openDetailPage(@RequestParam(required = true) int page, int detail) {
        return "/html/DetailPage.html?page=" + page + "detail=" + detail;
    }

    @GetMapping(value = "/Project/storage.do")
    public String openStoragePage() {
        return "/html/StoragePage.html";
    }
}
