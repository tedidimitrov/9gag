package finalproject.ninegag.controller;

import finalproject.ninegag.exceptions.AuthorizationException;
import finalproject.ninegag.exceptions.NotFoundException;
import finalproject.ninegag.model.dao.PostDAO;
import finalproject.ninegag.model.dto.MakePostDTO;
import finalproject.ninegag.model.dto.ReadyPostDTO;
import finalproject.ninegag.model.pojo.Post;
import finalproject.ninegag.model.pojo.User;
import finalproject.ninegag.model.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.bind.annotation.*;

import javax.mail.Session;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
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

    //Problem working via repository
    @GetMapping("/posts/{id}")
    public ReadyPostDTO getPostById(@PathVariable long id){
        ReadyPostDTO readyPostDTO = new ReadyPostDTO(getPost(id));
        return readyPostDTO;
    }

}
