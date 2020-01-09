package finalproject.ninegag.model.pojo;

import finalproject.ninegag.model.dto.RegisterUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String username;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private LocalDateTime dateRegistered;

    public User(RegisterUserDTO userDTO){
        setUsername(userDTO.getUsername());
        setFirstName(userDTO.getFirstName());
        setLastName(userDTO.getLastName());
        setEmail(userDTO.getEmail());
        setPassword(userDTO.getPassword()); //TODO ENCRYPT PASSWORD;
        setDateRegistered(LocalDateTime.now());
    }

    public void setPassword(String password){
        this.password = new BCryptPasswordEncoder().encode(password);
    }


}
