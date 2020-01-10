package finalproject.ninegag.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDTO {

    @NotBlank(message = "Email is mandatory!")
    private String email;
    @NotBlank(message = "Password is mandatory!")
    private String password;

}
