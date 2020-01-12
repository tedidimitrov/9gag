package finalproject.ninegag.model.dto;

import finalproject.ninegag.model.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReadyCommentDTO {

    @NotNull
    private long id;
    @NotNull
    private LocalDateTime datePosted;
    @NotNull
    private String text;
    private String imageUrl;
    @NotNull
    private long postId;
    @NotNull
    private long ownerId;
    private long fatherCommentId;

    public ReadyCommentDTO(Comment comment) {
        setId(comment.getId());
        setText(comment.getText());
        setDatePosted(comment.getDatePosted());
        if(comment.getImageUrl() != null){
            setImageUrl(comment.getImageUrl());
        }
        setImageUrl(comment.getImageUrl());
        setPostId(comment.getPost().getId());
        setOwnerId(comment.getUser().getId());
        if(comment.getParentComment() != null) {
            setFatherCommentId(comment.getParentComment().getId());
        }
    }

}
