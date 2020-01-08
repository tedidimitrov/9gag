package finalproject.ninegag.model.pojo;

//import jdk.jfr.Category;
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
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String title;
    @Column
    private LocalDateTime dateUploaded;
    @Column
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    //private Category category;

}
