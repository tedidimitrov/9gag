package finalproject.ninegag.controller;

import finalproject.ninegag.model.dao.PostDAO;
import finalproject.ninegag.model.dto.MakePostDTO;
import finalproject.ninegag.model.dto.ReadyPostDTO;
import finalproject.ninegag.model.pojo.Post;
import finalproject.ninegag.model.pojo.User;
import finalproject.ninegag.model.repository.PostRepository;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.Session;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class PostController extends AbstractController{

    public static final String SESSION_KEY_LOGGED_USER = "logged_user";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostDAO postDAO;

    @PostMapping("/posts/create")
    public ReadyPostDTO addPost(@RequestBody MakePostDTO postDTO, HttpSession session) throws SQLException {
        User currentUser = (User)session.getAttribute(SESSION_KEY_LOGGED_USER);
        Post post = new Post(postDTO,currentUser);
        //add to database
        postRepository.save(post);
        //postDAO.makePost(post,currentUser);

        ReadyPostDTO readyPostDTO =new ReadyPostDTO(post);
        return readyPostDTO;
    }

}
