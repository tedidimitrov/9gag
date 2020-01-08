package finalproject.ninegag.controller;

import finalproject.ninegag.exceptions.AuthorizationException;
import finalproject.ninegag.exceptions.BadRequestException;
import finalproject.ninegag.model.dao.PostDAO;
import finalproject.ninegag.model.dao.UserDAO;
import finalproject.ninegag.model.dto.LoginUserDTO;
import finalproject.ninegag.model.dto.RegisterUserDTO;
import finalproject.ninegag.model.dto.UserWithoutPasswordDTO;
import finalproject.ninegag.model.pojo.Post;
import finalproject.ninegag.model.pojo.User;
import finalproject.ninegag.model.repository.PostRepository;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class UserController extends AbstractController{

    public static final String SESSION_KEY_LOGGED_USER = "logged_user";

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private PostDAO postDAO;
    @Autowired
    private PostRepository postRepository;

    @PostMapping("/users/register")
    public UserWithoutPasswordDTO register(@RequestBody RegisterUserDTO userDTO,HttpSession session) throws SQLException {
        //validate data in userDTO
            //if already exists in database
            //if the password contains at least 8 symbols with at least one digit
            //if the passwords watch
            // encrypt the password
        //TODO validate the above!

        //create User object
        User user = new User(userDTO);
        //add to database
        userDAO.addUser(user);
        //return userWithoutPasswordDTO
        session.setAttribute(SESSION_KEY_LOGGED_USER,user);
        UserWithoutPasswordDTO responseDTO =new UserWithoutPasswordDTO(user);
        return responseDTO;
    }

    @PostMapping("/users/login")
    public UserWithoutPasswordDTO login(@RequestBody LoginUserDTO userDTO, HttpSession session) throws SQLException {

        User user = userDAO.getByUserName(userDTO.getUsername());
        if(user == null){
            throw new BadRequestException("Invalid Credentials");
        }
        else if(passwordValid(user,userDTO)){
            session.setAttribute(SESSION_KEY_LOGGED_USER,user);
            UserWithoutPasswordDTO responseDTO =new UserWithoutPasswordDTO(user);
            return responseDTO;
        }
        throw new BadRequestException("Invalid Credentials");
    }

    @PostMapping("/users/logout")
    public void logout(HttpSession session){
        session.invalidate();
    }

    @GetMapping("users/posts")
    public List<Post> getPosts(HttpSession session) throws SQLException {
        User user = (User) session.getAttribute(SESSION_KEY_LOGGED_USER);
        if(user == null){
            throw new AuthorizationException();
        }
        List<Post> posts = postRepository.findAllByUser_Id(user.getId());
        return posts;
    }

    private boolean passwordValid(User user, LoginUserDTO userDTO) {
        //TODO VALIDATE
        return true;
    }
}

