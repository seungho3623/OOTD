package TeamProject.Project.Contorller;

import TeamProject.Project.Entity.OutfitStorageItem;
import TeamProject.Project.Service.OutfitStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

        return "/html/StoragePage.html";
    }

    @ResponseBody
    @PostMapping("/Project/getStorageData")
    private List<OutfitStorageItem> getStorageData() {
        return outfitStorageService.itemList();
    }

    @DeleteMapping("/Project/deleteStorageData")
    public String outfitDelete(Integer id) {
        
        outfitStorageService.outfitDelete(id);
        return "redirect:/html/StoragePage.html";
    }
}
