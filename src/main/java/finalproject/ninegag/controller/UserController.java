package finalproject.ninegag.controller;

import finalproject.ninegag.exceptions.AuthorizationException;
import finalproject.ninegag.exceptions.BadRequestException;
import finalproject.ninegag.exceptions.CreatingEntityException;
import finalproject.ninegag.model.dao.PostDAO;
import finalproject.ninegag.model.dto.*;
import finalproject.ninegag.model.entity.Post;
import finalproject.ninegag.model.entity.User;
import finalproject.ninegag.utilities.SessionManager;
import finalproject.ninegag.utilities.mail.SuccessfullyChangedPassword;
import finalproject.ninegag.utilities.mail.SuccessfullyChangedUsername;
import finalproject.ninegag.utilities.mail.WelcomeToCommunity;
import finalproject.ninegag.model.repository.PostRepository;
import finalproject.ninegag.model.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController extends AbstractController{

    public static final String SESSION_KEY_LOGGED_USER = "logged_user";

    @Autowired
    private PostDAO postDAO;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @SneakyThrows
    @PostMapping("/users/register")
    public ResponseEntity<UserWithoutPasswordDTO> register(@Valid @RequestBody RegisterUserDTO userDTO,
                                                           HttpSession session, Errors errors){
        if (errors.hasErrors()) {
            throw new CreatingEntityException(errors.getFieldError().getDefaultMessage());
        }
        User user = new User(userDTO);
        //check if already exists
        if(userDTO.getPassword().equals(userDTO.getConfirmPassword())){
            if(userRepository.existsByEmail(userDTO.getEmail())){
                throw  new AuthorizationException("Already existing account!");
            }
        }else{
            throw  new BadRequestException("Passwords mismatch");
        }
        //add to database
        userRepository.save(user);
        session.setAttribute(SESSION_KEY_LOGGED_USER,user);
        UserWithoutPasswordDTO responseDTO =new UserWithoutPasswordDTO(user);
        WelcomeToCommunity send = new WelcomeToCommunity(user,userRepository);
        send.start();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @SneakyThrows
    @PostMapping("/users/login")
    public UserWithoutPasswordDTO login(@Valid @RequestBody LoginUserDTO userDTO, HttpSession session, Errors errors) {

        if(errors.hasErrors()){
            throw  new CreatingEntityException(errors.getFieldError().getDefaultMessage());
        }

        User user = userRepository.findByEmail(userDTO.getEmail());
        if(user == null){
            throw new BadRequestException("Invalid Credentials");
        }
        else if(passwordValid(userDTO)){
            SessionManager.logUser(session,user);
            UserWithoutPasswordDTO responseDTO =new UserWithoutPasswordDTO(user);
            return responseDTO;
        }
        throw new BadRequestException("Invalid Credentials");
    }

    @PostMapping("/users/logout")
    public ResponseEntity<String> logout(HttpSession session){
        session.invalidate();
        return new ResponseEntity<>("Logged out successfully!",HttpStatus.OK);
    }

    @GetMapping("users/posts")
    public List<ReadyPostDTO> getPosts(HttpSession session){
        User user = SessionManager.getLoggedUser(session);
        List<Post> posts = postRepository.findAllByUser_Id(user.getId());
        List<ReadyPostDTO> readyPosts = new ArrayList<>();
        for(Post post: posts){
            readyPosts.add(new ReadyPostDTO(post));
        }
        return readyPosts;
    }

    @PutMapping("/users/changeUsername")
    public ResponseEntity<String> changeUsername(@Valid @RequestBody ChangeUsernameDTO usernameDTO, HttpSession session,
                                                 Errors errors){

        if(errors.hasErrors()){
            throw new BadRequestException("Wrong (current/new) username");
        }
        User currentUser = SessionManager.getLoggedUser(session);
        if(!currentUser.getUserName().equals(usernameDTO.getUsernameBeforeChange())) {
            throw new BadRequestException("Current username mismatch");
        }
        currentUser.setUserName(usernameDTO.getUsernameAfterChange());
        userRepository.save(currentUser);
        SuccessfullyChangedUsername send = new SuccessfullyChangedUsername(currentUser,userRepository);
        send.start();
        return new ResponseEntity<>("Username changed successfully!", HttpStatus.OK);
    }

    @PutMapping("/users/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDTO userDTO, HttpSession session){
        User currentUser = SessionManager.getLoggedUser(session);
        if(!BCrypt.checkpw(userDTO.getPasswordBeforeChange(),currentUser.getPassword())){
            throw new BadRequestException("Mismatching old password!");
        }
        currentUser.setPassword(userDTO.getPasswordAfterChange());
        userRepository.save(currentUser);
        SuccessfullyChangedPassword send = new SuccessfullyChangedPassword(currentUser,userRepository);
        send.start();
        return new ResponseEntity<>("Password changed successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/users/delete")
    public ResponseEntity<String> deleteUser(HttpSession session){
        User user = SessionManager.getLoggedUser(session);
        userRepository.delete(user);
        return new ResponseEntity<>("Deletion succesful!",HttpStatus.OK);
    }

    private boolean passwordValid(LoginUserDTO userDTO) {
        boolean identicalPasswords = BCrypt.checkpw(userDTO.getPassword(),userRepository.findByEmail(userDTO.getEmail()).getPassword());
        if(identicalPasswords){
            return true;
        }
        return false;
    }
}

