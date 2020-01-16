package finalproject.ninegag.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalproject.ninegag.model.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ParentCommentDTO {
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
    private ReadyUserDTO commentOwner;
    private List<ChildCommentDTO> children;

    public ParentCommentDTO(Comment comment){
        setId(comment.getId());
        setText(comment.getText());
        setDatePosted(comment.getDatePosted());
        if(comment.getImageUrl() != null){
            setImageUrl(comment.getImageUrl());
        }
        setImageUrl(comment.getImageUrl());
        setPostId(comment.getPost().getId());
        setCommentOwner(comment.getUser().toUserDTO());
    }

    public ParentCommentDTO(Comment comment, List<ChildCommentDTO> replies){
        setId(comment.getId());
        setText(comment.getText());
        setDatePosted(comment.getDatePosted());
        if(comment.getImageUrl() != null){
            setImageUrl(comment.getImageUrl());
        }
        setImageUrl(comment.getImageUrl());
        setPostId(comment.getPost().getId());
        setCommentOwner(comment.getUser().toUserDTO());
        this.children = new ArrayList<>(replies);
    }
}
