package finalproject.ninegag.model.dto;

import finalproject.ninegag.model.pojo.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CommentDTO {

    private String text;
    private String imageUrl;

}
