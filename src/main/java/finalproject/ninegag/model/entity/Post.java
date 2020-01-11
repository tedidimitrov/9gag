package finalproject.ninegag.model.entity;


import finalproject.ninegag.model.dto.MakePostDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    //problems below
    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name = "users_downvoted_posts",joinColumns = @JoinColumn(name ="posts_id"),inverseJoinColumns =@JoinColumn(name="user_id"))
    @Transient
    private List<User> users = new ArrayList<>();

    public void addUser(User user){
        this.users.add(user);
        user.getPosts().add(this);
    }
    public void removeUser(User user){
        this.users.remove(user);
        user.getPosts().remove(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Post)) return false;
        return this.id != 0 && this.id != ((Post) obj).id;
    }
    @Override
    public int hashCode() {
        return 31;
    }

    public Post(MakePostDTO makePostDTO, User user){
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
