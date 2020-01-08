package finalproject.ninegag.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Post {
    private long id;
    private String title;
    private LocalDateTime dateUploaded;
    private String imageUrl;
    private int points;
    private long postOwnerId;
    private long categoryId;
}
