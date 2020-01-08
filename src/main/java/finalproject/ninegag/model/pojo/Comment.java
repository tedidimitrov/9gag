package finalproject.ninegag.model.pojo;


import finalproject.ninegag.model.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private LocalDateTime datePosted;
    @Column
    private String text;
    @Column
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "id")
    private Comment repliedTo;

    public Comment(CommentDTO commentDTO){
        setText(commentDTO.getText());
        setImageUrl(commentDTO.getImageUrl());
    }

}
