package finalproject.ninegag.model.dto;

import finalproject.ninegag.model.pojo.Post;
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
    private LocalDateTime date_uploaded;
    private String image_url;
    private int points;
    private long post_owner_id;
    private long category_id;

    public ReadyPostDTO(Post post){
        setId(post.getId());
        setTitle(post.getTitle());
        setDate_uploaded(post.getDateUploaded());
        setImage_url(post.getImageUrl());
        setPoints(post.getPoints());
        setPost_owner_id(post.getUser().getId());
        setCategory_id(post.getCategory().getId());
    }
}
