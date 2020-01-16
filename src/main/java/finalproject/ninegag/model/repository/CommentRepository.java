package finalproject.ninegag.model.repository;

import finalproject.ninegag.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostIdAndParentCommentIsNullOrderByDatePostedDesc(long postId);
    List<Comment> findAllByParentCommentIdOrderByDatePostedDesc(long commentId);
}
