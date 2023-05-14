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
        return "/html/LodingPage.html";
    }

    @GetMapping(value = "/Project/outfit.do")
    public String openOutfitPage(@RequestParam(required = true) int pageIndex) {
        return "/html/OutfitPage.html?pageIndex=" + pageIndex;
    }

    @GetMapping(value = "/Project/detail.do")
    public String openDetailPage(@RequestParam(required = true) int pageIndex, int detailIndex) {
        return "/html/DetailPage.html?pageIndex=" + pageIndex + "detailIndex=" + detailIndex;
    }

    @GetMapping(value = "/Project/storage.do")
    public String openStoragePage() {
        return "/html/StoragePage.html";
    }
}
