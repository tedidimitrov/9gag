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
import finalproject.ninegag.model.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
public class UserController extends AbstractController{

    public static final String SESSION_KEY_LOGGED_USER = "logged_user";

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @SneakyThrows
    @PostMapping("/users/register")
    public UserWithoutPasswordDTO register(@Valid @RequestBody RegisterUserDTO userDTO, HttpSession session){
        User user = new User(userDTO);
        //check if already exists
        if(userDTO.getPassword().equals(userDTO.getConfirmPassword())){
            if(userRepository.existsByEmail(userDTO.getEmail())){
                throw  new AuthorizationException("Already existing account!");
            }
        }
        //add to database
        userRepository.save(user);
        session.setAttribute(SESSION_KEY_LOGGED_USER,user);
        UserWithoutPasswordDTO responseDTO =new UserWithoutPasswordDTO(user);
        return responseDTO;
    }

    @PostMapping("/users/login")
    public UserWithoutPasswordDTO login(@RequestBody LoginUserDTO userDTO, HttpSession session) {

        User user = userRepository.findByEmail(userDTO.getEmail());
        if(user == null){
            throw new BadRequestException("Invalid Credentials");
        }
        else if(passwordValid(userDTO)){
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
            throw new AuthorizationException("You must login first!");
        }
        List<Post> posts = postRepository.findAllByUser_Id(user.getId());
        return posts;
    }

    private boolean passwordValid(LoginUserDTO userDTO) {
        boolean identicalPasswords = BCrypt.checkpw(userDTO.getPassword(),userRepository.findByEmail(userDTO.getEmail()).getPassword());
        if(identicalPasswords){
            return true;
        }
        return false;
    }
}

