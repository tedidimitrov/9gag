package finalproject.ninegag.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CommentDTO {

    @NonNull
    private String text;
    private long parentId;

}