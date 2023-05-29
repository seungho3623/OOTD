package TeamProject.Project.Service;

import TeamProject.Project.Entity.OutfitStorageItem;
import TeamProject.Project.Repository.OutfitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutfitStorageService {

    private final OutfitRepository outfitRepository;

    public OutfitStorageService(OutfitRepository outfitRepository) {
        this.outfitRepository = outfitRepository;
    }

    public void outfitWrite(OutfitStorageItem item) {
        outfitRepository.save(item);
    }

    public List<OutfitStorageItem> itemList(){
        return outfitRepository.findAll();
    }

    public void outfitDelete(Integer id){
        outfitRepository.deleteById(id);
    }
}
