package finalproject.ninegag.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CommentDTO {

    private String text;
    private String imageUrl;
    private long parentId;

}