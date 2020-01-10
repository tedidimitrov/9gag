package finalproject.ninegag.model.pojo;


import finalproject.ninegag.model.dto.MakePostDTO;
import finalproject.ninegag.model.dto.ReadyPostDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String title;
    @Column(name = "date_uploaded")
    private LocalDateTime dateUploaded;
    @Column
    private String imageUrl;
    @Column
    private int points;
    @ManyToOne
    @JoinColumn(name = "post_owner_id")
    private User user;
    @ManyToOne
    @JoinColumn(name ="category_id")
    private Category category;

    public Post(MakePostDTO makePostDTO,User user){
        setTitle(makePostDTO.getTitle());
        setImageUrl(makePostDTO.getImage_url());
        setCategory(makePostDTO.getCategory());
        setPoints(0);
        setDateUploaded(LocalDateTime.now());
        setUser(user);

    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", dateUploaded=" + dateUploaded +
                ", imageUrl='" + imageUrl + '\'' +
                ", points=" + points +
                ", user=" + user +
                ", category=" + category +
                '}';
    }
}
