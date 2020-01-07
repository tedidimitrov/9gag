package finalproject.ninegag.model.repository;

import finalproject.ninegag.model.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {



}
