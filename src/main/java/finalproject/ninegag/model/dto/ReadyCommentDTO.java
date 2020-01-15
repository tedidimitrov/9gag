package finalproject.ninegag.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime datePosted;
    @NotNull
    private String text;
    private String imageUrl;
    @NotNull
    private long postId;
    @NotNull
    private ReadyUserDTO postOwner;
    private long parentCommentId;

    public ReadyCommentDTO(Comment comment){
        setId(comment.getId());
        setText(comment.getText());
        setDatePosted(comment.getDatePosted());
        if(comment.getImageUrl() != null){
            setImageUrl(comment.getImageUrl());
        }
        setImageUrl(comment.getImageUrl());
        setPostId(comment.getPost().getId());
        setPostOwner(comment.getUser().toUserDTO());
        if(comment.getParentComment() != null) {
            setParentCommentId(comment.getParentComment().getId());
        }
    }

    public ReadyCommentDTO(Comment comment, ReadyUserDTO userDTO) {
        setId(comment.getId());
        setText(comment.getText());
        setDatePosted(comment.getDatePosted());
        if(comment.getImageUrl() != null){
            setImageUrl(comment.getImageUrl());
        }
        setImageUrl(comment.getImageUrl());
        setPostId(comment.getPost().getId());
        setPostOwner(userDTO);
        if(comment.getParentComment() != null) {
            setParentCommentId(comment.getParentComment().getId());
        }
    }

}
