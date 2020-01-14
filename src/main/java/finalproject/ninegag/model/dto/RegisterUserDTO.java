package finalproject.ninegag.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {

    private static final String INCORRECT_PASSWORD_MESSAGE = "password must contain at least 8 symbols that includes:" +
                                                             "one digit, one uppercase letter and one lowercase letter";

    private static final String INCORRECT_EMAIL_MESSAGE = "Email format is incorrect!";
    @NotBlank(message = "username is mandatory!")
    private String username;
    @NotBlank(message = "first name is mandatory!")
    private String firstName;
    @NotBlank(message = "last name is mandatory!")
    private String lastName;
    @NotBlank(message = "Email is mandatory!")
    @Size(min = 10,max = 40 ,message = "Email must be between 8 and 40")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",message = INCORRECT_EMAIL_MESSAGE)
    private String email;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",message =INCORRECT_PASSWORD_MESSAGE)
    private String password;
    @NotBlank(message = "Please confirm your password!")
    private String confirmPassword;
}
