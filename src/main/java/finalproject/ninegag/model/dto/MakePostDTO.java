package finalproject.ninegag.model.dto;

import finalproject.ninegag.model.pojo.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MakePostDTO{

    private String title;
    private String image_url;
    private Category category;

}
