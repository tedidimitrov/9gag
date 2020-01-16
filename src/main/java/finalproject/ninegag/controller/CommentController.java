package finalproject.ninegag.controller;
import finalproject.ninegag.exceptions.BadRequestException;
import finalproject.ninegag.exceptions.NotFoundException;
import finalproject.ninegag.model.dao.CommentDao;
import finalproject.ninegag.model.dto.*;
import finalproject.ninegag.model.entity.Comment;
import finalproject.ninegag.model.entity.Post;
import finalproject.ninegag.model.entity.User;
import finalproject.ninegag.model.repository.CommentRepository;
import finalproject.ninegag.model.repository.PostRepository;
import finalproject.ninegag.utilities.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
public class CommentController extends AbstractController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    private CommentDao commentDao;

    private static final String STORAGE_ABSOLUTE_PATH = "D:\\Git\\uploads\\";
    private static final List<String> AVAILABLE_FILE_TYPES = Arrays.asList("image/jpeg", "image/png", "video/mp4");

    @PostMapping("/posts/{post_id}/comments")
    public ReadyCommentDTO addComment(@Valid @RequestPart(name = "file", required = false) MultipartFile file,
                                      @Valid @RequestParam(name = "text", defaultValue = "") String text,
                                      @Valid @RequestParam(required = false) Long parentCommentId,
                                      @Valid @PathVariable(name = "post_id") long postId,
                                      HttpSession session) throws IOException {
        User user = SessionManager.getLoggedUser(session);
        if(!AVAILABLE_FILE_TYPES.contains(file.getContentType())){
            throw new BadRequestException("Invalid file type");
        }
        text = text.trim();
        if (text.equals("")) {
            throw new BadRequestException("You must enter text!");
        }

        Optional<Post> post = postRepository.findById(postId);

        if (post.isPresent()) {
            ReadyUserDTO userDTO = new ReadyUserDTO(user);
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
                String postUrl = LocalDateTime.now().format(formatter) + extension;
                Path path = Paths.get(STORAGE_ABSOLUTE_PATH + postUrl);
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
                }
            }
            commentRepository.save(comment);
            return new ReadyCommentDTO(comment, userDTO);
        } else {
            throw new NotFoundException("Post not found!");
        }
    }

    @GetMapping("/comments/{comment_id}")
    public ReadyCommentDTO getCommentById(@PathVariable(name = "comment_id") long commentId) {

        Optional<Comment> comment = commentRepository.findById(commentId);

        if (comment.isPresent()) {
            ReadyCommentDTO commentDTO = new ReadyCommentDTO(comment.get());
            return commentDTO;
        } else {
            throw new NotFoundException("Comment not found!");
        }
    }

    @DeleteMapping("/comments/{comment_id}")
    public ResponseEntity<String> deleteComment(@PathVariable(name = "comment_id") long commentId,
                                                HttpSession session) throws IOException {
        User user =SessionManager.getLoggedUser(session);
        Optional<Comment> commentToBeDeleted = commentRepository.findById(commentId);
        if (commentToBeDeleted.isPresent()) {
            if (commentToBeDeleted.get().getUser().getId() != user.getId()) {
                throw new BadRequestException("You can only delete your own comments!");
            }
            if (commentToBeDeleted.get().getImageUrl() != null) {
                File file = new File(STORAGE_ABSOLUTE_PATH + commentToBeDeleted.get().getImageUrl());
                if (!file.delete()) {
                    throw new IOException("The file was not deleted properly!");
                }
            }
            commentRepository.delete(commentToBeDeleted.get());
            return new ResponseEntity<>("The comment was deleted successfully!", HttpStatus.OK);
        }
        else {
            throw new NotFoundException("Comment not found");
        }
    }


        @PostMapping("/comments/{comment_id}/upvote")
    public ReadyCommentDTO upvote(@PathVariable(name = "comment_id") long commentId,
                          HttpSession session) throws SQLException {

        User user = SessionManager.getLoggedUser(session);
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
        User user = SessionManager.getLoggedUser(session);
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
            //in 9gag comments cannot have negative number of votes
            if(points < 0){
                return 0;
            }else {
                return points;
            }
        }else{
            throw new NotFoundException("Comment not found!");
        }
    }

    @GetMapping("/posts/{post_id}/comments")
    public List<ParentCommentDTO> getAllCommentByPostId(@PathVariable(name = "post_id")long postId) {
        Optional<Post> post = postRepository.findById(postId);
        //check if such post exists
        if (!post.isPresent()) {
            throw new NotFoundException("Post not found!");
        }
        List<Comment> repliesToPost = commentRepository.findAllByPostIdAndParentCommentIsNullOrderByDatePostedDesc(postId);
            //if there are no comments to the post
            if (repliesToPost.isEmpty()) {
                return null;
            }
            List<ParentCommentDTO> parentComments = new ArrayList<>();

            for (Comment c : repliesToPost) {
                List<Comment> repliesToComments = commentRepository.findAllByParentCommentIdOrderByDatePostedDesc(c.getId());
                //if there are any repliesToComments
                if (!repliesToComments.isEmpty()) {
                    List<ChildCommentDTO> childrenComments = new ArrayList<>();
                    for (Comment r : repliesToComments) {
                        childrenComments.add(r.toChildCommentDTO());
                    }
                    parentComments.add(new ParentCommentDTO(c, childrenComments));
                } else {
                    parentComments.add(new ParentCommentDTO(c));
                }
            }
            return parentComments;
        }


}
