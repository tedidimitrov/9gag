package finalproject.ninegag.controller;

import finalproject.ninegag.exceptions.AuthorizationException;
import finalproject.ninegag.exceptions.NotFoundException;
import finalproject.ninegag.model.dao.PostDAO;
import finalproject.ninegag.model.dto.MakePostDTO;
import finalproject.ninegag.model.dto.ReadyPostDTO;
import finalproject.ninegag.model.entity.Post;
import finalproject.ninegag.model.entity.User;
import finalproject.ninegag.model.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
public class PostController extends AbstractController{

    public static final String SESSION_KEY_LOGGED_USER = "logged_user";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostDAO postDAO;

    @PostMapping("/posts/create")
    public ReadyPostDTO addPost(@RequestBody MakePostDTO postDTO, HttpSession session){
        if(session.isNew() || session.getAttribute(SESSION_KEY_LOGGED_USER) == null){
            throw new AuthorizationException("You must be logged in first!");
        }
        User currentUser = (User)session.getAttribute(SESSION_KEY_LOGGED_USER);
        Post post = new Post(postDTO,currentUser);
        //add to database
        postRepository.save(post);

        ReadyPostDTO readyPostDTO =new ReadyPostDTO(post);
        return readyPostDTO;
    }

    public Post getPost(long id){
        Optional<Post> optionalPost = postRepository.findById(id);
        if(optionalPost.isPresent()){
            Post post = optionalPost.get();
            return post;
        }
        throw new NotFoundException("Post not found!");

    }

    @GetMapping("/posts/{id}")
    public ReadyPostDTO getPostById(@PathVariable long id){
        ReadyPostDTO readyPostDTO = new ReadyPostDTO(getPost(id));
        return readyPostDTO;
    }

    @GetMapping("/posts")
    public List<Post> getAllPosts(){
        List<Post> posts =this.postRepository.findAll();
        return posts;
    }

    @GetMapping("/posts/getByDate")
    public List<Post> getAllByDateDesc(){
        return this.postRepository.findAllByOrderByDateUploadedDesc();
    }

    @GetMapping("/posts/titles/{title}")
    public List<Post> getPostByTitle(@PathVariable String title){
        List<Post> posts =this.postRepository.getByTitle(title);
        if(posts != null){
            return posts;
        }
        throw new NotFoundException("Not found such post!");
    }

    @PostMapping("/posts/downvote/{post_id}")
    public ResponseEntity<String> downvotePost(@PathVariable long post_id,HttpSession session){
        if(session.isNew() || session.getAttribute(SESSION_KEY_LOGGED_USER) == null){
            throw new AuthorizationException("You must be logged in first!");
        }
        User currentUser = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        Optional<Post> optionalPost = this.postRepository.findById(post_id);
        if(!optionalPost.isPresent()){
            throw  new NotFoundException("No such post found!");
        }
        Post post = optionalPost.get();
        post.removeUser(currentUser);
        post.setPoints(post.getPoints()-1);
        this.postRepository.save(post);

        return new ResponseEntity<>("downvoting done correctly!", HttpStatus.OK);
    }

    @PostMapping("/posts/upvote/{post_id}")
    public ResponseEntity<String> upvotePost(@PathVariable long post_id,HttpSession session){
        if(session.isNew() || session.getAttribute(SESSION_KEY_LOGGED_USER) == null){
            throw new AuthorizationException("You must be logged in first!");
        }
        User currentUser = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        Optional<Post> optionalPost = this.postRepository.findById(post_id);
        if(!optionalPost.isPresent()){
            throw  new NotFoundException("No such post found!");
        }
        Post post = optionalPost.get();
        post.addUser(currentUser);
        post.setPoints(post.getPoints()+1);
        this.postRepository.save(post);

        return new ResponseEntity<>("upvoting done correctly!", HttpStatus.OK);
    }

}
