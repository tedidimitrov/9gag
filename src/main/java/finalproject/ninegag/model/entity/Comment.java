package finalproject.ninegag.model.entity;


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
    @JoinColumn(name = "owner_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "replied_to_id")
    private Comment parentComment;

    public Comment(CommentDTO commentDTO){
        setText(commentDTO.getText());
    }

}
