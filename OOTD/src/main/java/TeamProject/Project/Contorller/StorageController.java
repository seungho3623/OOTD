package TeamProject.Project.Contorller;

import TeamProject.Project.Entity.OutfitStorageItem;
import TeamProject.Project.Service.OutfitStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class StorageController {

    private final OutfitStorageService outfitStorageService;

    public StorageController(OutfitStorageService outfitStorageService) {
        this.outfitStorageService = outfitStorageService;
    }

    @PostMapping("/Project/storage.do/storage")
    public String StorageProcess(OutfitStorageItem item) {
        outfitStorageService.outfitWrite(item);

        return "";
    }

    @GetMapping(value = "/Project/storage.do")
    public String openStoragePage(Model model) {
        List<OutfitStorageItem> list = outfitStorageService.itemList();
        model.addAttribute("list", list);

        for(OutfitStorageItem item : list){
            System.out.println(item);
        }
        //model.addAttribute("data", "TestData");

        return "/html/StoragePage.html";
    }
}
