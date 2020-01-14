package finalproject.ninegag.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import finalproject.ninegag.model.entity.Post;
import finalproject.ninegag.model.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReadyPostDTO {

    private long id;
    private String title;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateUploaded;
    private String imageUrl;
    private int points;
    private UserWithoutPasswordDTO postOwner;
    private String category;

    public ReadyPostDTO(Post post){
        setId(post.getId());
        setTitle(post.getTitle());
        setDateUploaded(post.getDateUploaded());
        setImageUrl(post.getImageUrl());
        setPoints(post.getPoints());
        setPostOwner(new UserWithoutPasswordDTO(post.getUser()));
        setCategory(post.getCategory().getCategoryName());
    }
}
