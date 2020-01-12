package finalproject.ninegag.controller;
import finalproject.ninegag.exceptions.AuthorizationException;
import finalproject.ninegag.exceptions.BadRequestException;
import finalproject.ninegag.exceptions.NotFoundException;
import finalproject.ninegag.model.dao.CommentDao;
import finalproject.ninegag.model.dto.CommentDTO;
import finalproject.ninegag.model.dto.ReadyCommentDTO;
import finalproject.ninegag.model.entity.Comment;
import finalproject.ninegag.model.entity.Post;
import finalproject.ninegag.model.entity.User;
import finalproject.ninegag.model.repository.CommentRepository;
import finalproject.ninegag.model.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
public class CommentController extends AbstractController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    private CommentDao commentDao;

    private static final String STORAGE_ABSOLUTE_PATH  = "D:\\Git\\uploads\\";

    @PostMapping("/posts/{post_id}/comments")
    public ReadyCommentDTO addComment(@RequestPart(name = "file",required=false) MultipartFile file,
                                      @RequestParam String text,
                                      @RequestParam(required=false) Long parentCommentId,
                                      @PathVariable(name = "post_id") long postId,
                                      HttpSession session) throws IOException {
        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException("You must login first!");
        }
        Optional<Post> post = postRepository.findById(postId);
        ReadyCommentDTO commentDTO;

        if(post.isPresent()) {
            Comment comment = new Comment();
            comment.setText(text);
            comment.setDatePosted(LocalDateTime.now());
            comment.setUser(user);
            comment.setPost(post.get());
            //upload file
            if (file != null) {
                String extension = file.getOriginalFilename();
                String pattern = "yyyy-MM-dd-hh-mm-ss";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                String postUrl = STORAGE_ABSOLUTE_PATH + LocalDateTime.now().format(formatter) + extension;
                Path path = Paths.get(postUrl);
                byte[] bytes = file.getBytes();

                Files.write(path, bytes);
                comment.setImageUrl(postUrl);
            }
            if (parentCommentId != null) {
                Optional<Comment> parent = commentRepository.findById(parentCommentId);
                if (!parent.isPresent()) {
                   throw new BadRequestException("There is no parent comment with this id!");
                } else {
                    //else - reply to parent comment
                    comment.setParentComment(parent.get());
                    commentRepository.save(comment);
                    commentDTO = new ReadyCommentDTO(comment);
                    return commentDTO;
                }
            } else{
                commentRepository.save(comment);
                commentDTO = new ReadyCommentDTO(comment);
                return commentDTO;
            }
        } else{
            throw new NotFoundException("Post not found!");
        }
    }

    @GetMapping("/comments/{comment_id}")
    public ReadyCommentDTO getCommentById(@PathVariable(name = "comment_id") long commentId){

        Optional<Comment> comment = commentRepository.findById(commentId);

        if(comment.isPresent()){
            ReadyCommentDTO commentDTO = new ReadyCommentDTO(comment.get());
            return commentDTO;
        } else{
            throw new NotFoundException("Comment not found!");
        }
    }

    @DeleteMapping("/comments/{comment_id}")
    public ReadyCommentDTO deleteComment(@PathVariable(name = "comment_id") long commentId,
                                 HttpSession session){

        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null) {
            throw new AuthorizationException("You must login first!");
        }

        Optional<Comment> commentToBeDeleted = commentRepository.findById(commentId);

        if(commentToBeDeleted.isPresent()){
            if(commentToBeDeleted.get().getUser().getId() != user.getId()){
                throw new BadRequestException("You can only delete your own comments!");
            }
            commentRepository.delete(commentToBeDeleted.get());
            ReadyCommentDTO commentDTO = new ReadyCommentDTO(commentToBeDeleted.get());
            return commentDTO;
        } else{
            throw new NotFoundException("Comment not found");
        }
    }

    @PostMapping("/comments/{comment_id}/upvote")
    public ReadyCommentDTO upvote(@PathVariable(name = "comment_id") long commentId,
                          HttpSession session) throws SQLException {

        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException("You must login first!");
        }
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isPresent()){
            commentDao.upvoteComment(user, comment.get());
            ReadyCommentDTO commentDTO = new ReadyCommentDTO(comment.get());
            return commentDTO;
        }
        throw new NotFoundException("Comment not found");
    }

    @PostMapping("/comments/{comment_id}/downvote")
    public ReadyCommentDTO downvote(@PathVariable(name = "comment_id") long commentId,
                          HttpSession session) throws SQLException{
        User user = (User) session.getAttribute(UserController.SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException("You must login first!");
        }
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isPresent()){
            commentDao.downvoteComment(user, comment.get());
            ReadyCommentDTO commentDTO = new ReadyCommentDTO(comment.get());
            return commentDTO;
        } else{
            throw new NotFoundException("Comment not found");
        }
    }

    @GetMapping("/comments/{comment_id}/points")
    public long getPoints(@PathVariable(name = "comment_id") long comment_id) throws SQLException {

        Optional<Comment> comment = commentRepository.findById(comment_id);

        if(comment.isPresent()){
            long points = commentDao.getPoints(comment.get());
            if(points < 0){
                return 0;
            }else {
                return points;
            }
        }else{
            throw new NotFoundException("Comment not found!");
        }
    }
}
