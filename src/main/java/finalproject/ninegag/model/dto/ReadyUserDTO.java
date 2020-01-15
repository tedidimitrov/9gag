package finalproject.ninegag.model.dto;

import finalproject.ninegag.model.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class ReadyUserDTO {

    private long id;
    private String username;

    public ReadyUserDTO(User user){
        setId(user.getId());
        setUsername(user.getUserName());
    }

}
