package finalproject.ninegag.model.pojo;

<<<<<<< HEAD
//import jdk.jfr.Category;
=======

import jdk.jfr.Category;
>>>>>>> a0f6f002bf26a56b1c72639ffc0a3d73c635f486
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
