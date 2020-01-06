package finalproject.ninegag.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class User {

    private long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDateTime dateRegistered;


}
