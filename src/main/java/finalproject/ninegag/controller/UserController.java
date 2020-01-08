package finalproject.ninegag.controller;

import finalproject.ninegag.model.dao.UserDAO;
import finalproject.ninegag.model.dto.RegisterUserDTO;
import finalproject.ninegag.model.dto.UserWithoutPasswordDTO;
import finalproject.ninegag.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class UserController extends AbstractController{

    @Autowired
    private UserDAO userDAO;

    @PostMapping("/users/register")
    public UserWithoutPasswordDTO register(@RequestBody RegisterUserDTO userDTO) throws SQLException {
        //validate data in userDTO
            //if already exists in database
            //if the password contains at least 8 symbols with at least one digit
            //if user
        //TODO validate the above!

        //create User object
        User user = new User(userDTO);
        //add to database
        userDAO.addUser(user);
        //return userWithoutPasswordDTO
        UserWithoutPasswordDTO responseDTO =new UserWithoutPasswordDTO(user);
        return responseDTO;
    }
}

