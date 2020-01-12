package finalproject.ninegag.model.dto;

import finalproject.ninegag.model.entity.Post;
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
    private LocalDateTime dateUploaded;
    private String imageUrl;
    private int points;
    private long postOwnerId;
    private long categoryId;

    public ReadyPostDTO(Post post){
        setId(post.getId());
        setTitle(post.getTitle());
        setDateUploaded(post.getDateUploaded());
        setImageUrl(post.getImageUrl());
        setPoints(post.getPoints());
        setPostOwnerId(post.getUser().getId());
        setCategoryId(post.getCategory().getId());
    }
}
