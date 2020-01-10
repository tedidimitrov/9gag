package finalproject.ninegag.controller;



import finalproject.ninegag.exceptions.AuthorizationException;
import finalproject.ninegag.exceptions.BadRequestException;
import finalproject.ninegag.exceptions.NotFoundException;
import finalproject.ninegag.model.dao.CommentDao;
import finalproject.ninegag.model.dto.CommentDTO;
import finalproject.ninegag.model.dto.MakePostDTO;
import finalproject.ninegag.model.dto.ReadyPostDTO;
import finalproject.ninegag.model.pojo.Comment;
import finalproject.ninegag.model.pojo.Post;
import finalproject.ninegag.model.pojo.User;
import finalproject.ninegag.model.repository.CommentRepository;
import finalproject.ninegag.model.repository.PostRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class CommentController extends AbstractController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    private CommentDao commentDao;

    @PostMapping("/posts/{post_id}/comments")
    public Comment addComment(@RequestBody CommentDTO commentDto,
                              @PathVariable(name = "post_id") long postId,
                              HttpSession session){
        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException("You must login first!");
        }

        Optional<Post> post = postRepository.findById(postId);

        if(post.isPresent()){
            Comment comment = new Comment(commentDto);
            comment.setDatePosted(LocalDateTime.now());
            comment.setPost(post.get());
            comment.setUser(user);
            commentRepository.save(comment);
            return comment;
        }
        else{
            throw new NotFoundException("Post not found!");
        }
    }

    @GetMapping("/comments/{comment_id}")
    public Comment getCommentById(@PathVariable(name = "comment_id") long commentId){

        Optional<Comment> comment = commentRepository.findById(commentId);

        if(comment.isPresent()){
            return comment.get();
        }
        else{
            throw new NotFoundException("Comment not found!");
        }
    }

    @DeleteMapping("/comments/{comment_id}")
    public Comment deleteComment(@PathVariable(name = "comment_id") long commentId,
                                 HttpSession session){

        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException("You must login first!");
        }

        Optional<Comment> commentToBeDeleted = commentRepository.findById(commentId);

        if(commentToBeDeleted.isPresent()){
            if(commentToBeDeleted.get().getUser().getId() != user.getId()){
                throw new BadRequestException("You can only delete your own comments!");
            }
            commentRepository.delete(commentToBeDeleted.get());
            return commentToBeDeleted.get();
        }
        else{
            throw new NotFoundException("Comment not found");
        }
    }

    @PostMapping("/comments/{comment_id}")
    public Comment reply(@RequestBody CommentDTO commentDTO,
                         @PathVariable(name = "comment_id") long commentId,
                         HttpSession session){

        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException("You must login first!");
        }

        Optional<Comment> commentToBeReplied = commentRepository.findById(commentId);

        if(commentToBeReplied.isPresent()){
            Comment reply = new Comment(commentDTO);
            reply.setDatePosted(LocalDateTime.now());
            reply.setUser(user);
            reply.setRepliedTo(commentToBeReplied.get());
            commentRepository.save(reply);
            return reply;
        }
        else{
            throw new NotFoundException("Comment not found");
        }
    }



    @PostMapping("/comments/{comment_id}/upvote")
    public Comment upvote(@PathVariable(name = "comment_id") long commentId,
                          HttpSession session) throws SQLException {

        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException("You must login first!");
        }

        Optional<Comment> comment = commentRepository.findById(commentId);

        if(comment.isPresent()){
            commentDao.upvoteComment(user, comment.get());
            return comment.get();
        }
        throw new NotFoundException("Comment not found");
    }

    @PostMapping("/comments/{comment_id}/downvote")
    public Comment downvote(@PathVariable(name = "comment_id") long commentId,
                          HttpSession session) throws SQLException{

        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException("You must login first!");
        }

        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isPresent()){
            commentDao.downvoteComment(user, comment.get());
            return comment.get();
        }
        else{
            throw new NotFoundException("Comment not found");
        }
    }

    @GetMapping("/comments/{comment_id}/points")
    public long getPoints(@PathVariable(name = "comment_id") long comment_id) throws SQLException {

        Optional<Comment> comment = commentRepository.findById(comment_id);

        if(comment.isPresent()){
            return commentDao.getPoints(comment.get());
        }
        else{
            throw new NotFoundException("Comment not found!");
        }
    }
}
