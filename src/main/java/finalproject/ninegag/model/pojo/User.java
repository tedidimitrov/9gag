package finalproject.ninegag.model.pojo;

import finalproject.ninegag.model.dto.RegisterUserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class User {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDateTime dateRegistered;

    public User(RegisterUserDTO userDTO){
        setUsername(userDTO.getUsername());
        setFirstName(userDTO.getFirstName());
        setLastName(userDTO.getLastName());
        setEmail(userDTO.getEmail());
        setPassword(userDTO.getPassword());
        setDateRegistered(LocalDateTime.now());
    }


}
