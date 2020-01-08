package finalproject.ninegag.model.dto;

import finalproject.ninegag.model.pojo.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserWithoutPasswordDTO {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    public UserWithoutPasswordDTO(User user){
        setId(user.getId());
        setUsername(user.getUsername());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setEmail(user.getEmail());
    }

}
