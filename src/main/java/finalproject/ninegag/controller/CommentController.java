package finalproject.ninegag.controller;


import com.sun.xml.bind.v2.TODO;
import finalproject.ninegag.exceptions.BadRequestException;
import finalproject.ninegag.exceptions.NotFoundException;
import finalproject.ninegag.model.dto.CommentDTO;
import finalproject.ninegag.model.pojo.Comment;
import finalproject.ninegag.model.pojo.User;
import finalproject.ninegag.model.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/{post_id}/comments/add")
    public Comment addComment(@RequestBody CommentDTO commentDto,
                              @PathVariable(name = "post_id") long postId,
                              HttpSession session){
        //User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER); TODO session

        //todo post.getId()


        Comment comment = new Comment();
        comment.setDatePosted(LocalDateTime.now());
        commentRepository.save(comment);
        return comment;
    }

    @GetMapping("/{post_id}/comments/{comment_id}")
    public Comment getCommentById(@PathVariable(name = "post_id") long postId,
                           @PathVariable(name = "comment_id") long commentId){

        //todo post.isPresent()

        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isPresent()){
            return comment.get();
        }
        else{
            throw new NotFoundException("Comment not found");
        }
    }

    @DeleteMapping("/{post_id}/comments/{comment_id}/delete")
    public Comment deleteComment(@PathVariable(name = "post_id") long postId,
                                 @PathVariable(name = "comment_id") long commentId,
                                 HttpSession session){

        User user = (User) session.getAttribute(""); //TODO session
        //todo post.isPresent()

        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isEmpty()){
            throw new NullPointerException("Comment not found");
        }
        if(comment.get().getUser().getId() != user.getId()){
            throw new BadRequestException("You don't have access to this service");
        }
        commentRepository.deleteById(commentId);
        return comment.get();
    }

}
