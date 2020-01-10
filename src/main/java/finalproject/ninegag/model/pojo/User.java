package finalproject.ninegag.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import finalproject.ninegag.model.dto.RegisterUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
    private String user_name;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String email;
    @Column
    @JsonIgnore
    private String password;
    @Column
    private LocalDateTime dateRegistered;
    //problems below
    @ManyToMany(mappedBy = "users")
    @Transient
    private List<Post> posts= new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(this.email, ((User) o).email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    public User(RegisterUserDTO userDTO){
        setUser_name(userDTO.getUsername());
        setFirstName(userDTO.getFirstName());
        setLastName(userDTO.getLastName());
        setEmail(userDTO.getEmail());
        setPassword(userDTO.getPassword());
        setDateRegistered(LocalDateTime.now());
    }

    public void setPassword(String password){
        this.password = new BCryptPasswordEncoder().encode(password);
    }


}
