package TeamProject.Project.Repository;

import TeamProject.Project.Entity.OutfitStorageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutfitRepository extends JpaRepository<OutfitStorageItem, Integer> {
}
