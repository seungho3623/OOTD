package TeamProject.Project.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CoordiDTO {
    private final String name;
    private final String thumbnail;
    private final String url;
    private String description;
    private List<String> itemThumbnails;

    public CoordiDTO(String name, String thumbnail, String url) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.url = url;
    }
}
