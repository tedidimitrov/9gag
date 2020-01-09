package finalproject.ninegag.controller;



import finalproject.ninegag.exceptions.AuthorizationException;
import finalproject.ninegag.exceptions.BadRequestException;
import finalproject.ninegag.exceptions.NotFoundException;
import finalproject.ninegag.model.dao.CommentDao;
import finalproject.ninegag.model.dto.CommentDTO;
import finalproject.ninegag.model.pojo.Comment;
import finalproject.ninegag.model.pojo.User;
import finalproject.ninegag.model.repository.CommentRepository;
import lombok.SneakyThrows;
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
    @Autowired
    private CommentDao commentDao;

    @PostMapping("/{post_id}/comments/add")
    public Comment addComment(@RequestBody CommentDTO commentDto,
                              @PathVariable(name = "post_id") long postId,
                              HttpSession session){
        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }

        //todo post.getId()

        Comment comment = new Comment(commentDto);
        comment.setDatePosted(LocalDateTime.now());
        comment.setUser(user);
        //todo comment.setPost(post);
        commentRepository.save(comment);
        return comment;
    }

    @GetMapping("/{post_id}/comments/{comment_id}")
    public Comment getCommentById(@PathVariable(name = "post_id") long postId,
                           @PathVariable(name = "comment_id") long commentId){

        //todo post.isPresent()

        if(!commentRepository.existsById(commentId)){
            throw new NotFoundException("Comment not found");
        }

        return commentRepository.getById(commentId);
    }

    @DeleteMapping("/{post_id}/comments/{comment_id}/delete")
    public Comment deleteComment(@PathVariable(name = "post_id") long postId,
                                 @PathVariable(name = "comment_id") long commentId,
                                 HttpSession session){

        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }

        //todo post.isPresent()

        if(!commentRepository.existsById(commentId)){
            throw new NotFoundException("Comment not found");
        }

        Comment commentToBeDeleted = commentRepository.getById(commentId);

        if(commentToBeDeleted.getUser().getId() != user.getId()){
            throw new BadRequestException("You can only delete your own comments!");
        }
        commentRepository.deleteById(commentId);
        return commentToBeDeleted;
    }

//    @PutMapping("/{post_id}/comments/{comment_id}/edit")
//    public Comment editComment(@PathVariable(name = "post_id") long postId,
//                               @PathVariable(name = "comment_id") long commentId,
//                               HttpSession session){
//
//        User user = (User) session.getAttribute(""); //TODO session
//        if(user == null){
//            throw new AuthorizationException();
//         }
//        todo post.isPresent()
//
//        Optional<Comment> comment = commentRepository.findById(commentId);
//        if(comment.isEmpty()){
//            throw new NullPointerException("Comment not found");
//        }
//
//
//    }
    @PostMapping("/{post_id}/comments/{comment_id}/reply")
    public Comment reply(@RequestBody CommentDTO commentDTO,
                         @PathVariable(name = "post_id") long postId,
                         @PathVariable(name = "comment_id") long commentId,
                         HttpSession session){

        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        //todo post.isPresent()

        if(!commentRepository.existsById(commentId)){
            throw new NotFoundException("Comment not found");
        }

        Comment commentToBeReplied = commentRepository.getById(commentId);
        Comment reply = new Comment(commentDTO);

        reply.setDatePosted(LocalDateTime.now());
        reply.setUser(user);
        //todo comment.setPost(post);
        reply.setRepliedTo(commentToBeReplied);
        commentRepository.save(reply);
        return reply;
    }


    @SneakyThrows
    @PostMapping("/{post_id}/comments/{comment_id}/upvote")
    public Comment upvote(@PathVariable(name = "post_id") long postId,
                          @PathVariable(name = "comment_id") long commentId,
                          HttpSession session){

        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }

        //todo post verification

        if(!commentRepository.existsById(commentId)){
            throw new NotFoundException("Comment not found");
        }

        Comment comment = commentRepository.getById(commentId);

        commentDao.upvoteComment(user, comment);

        return comment;

    }

    @SneakyThrows
    @PostMapping("/{post_id}/comments/{comment_id}/downvote")
    public Comment downvote(@PathVariable(name = "post_id") long postId,
                          @PathVariable(name = "comment_id") long commentId,
                          HttpSession session){

        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }

        //todo post verification

        if(!commentRepository.existsById(commentId)){
            throw new NotFoundException("Comment not found");
        }

        Comment comment = commentRepository.getById(commentId);

        commentDao.downvoteComment(user, comment);

        return comment;

    }

}
