package TeamProject.Project.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_outfit_storage")
public class OutfitStorageItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String thumbnail;
    private String url;
}
