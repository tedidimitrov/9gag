package finalproject.ninegag.model.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CommentDTO {

    @NotNull
    private String text;
    private long parentId;

}