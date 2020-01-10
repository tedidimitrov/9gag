package finalproject.ninegag.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordDTO {

    private static final String INCORRECT_PASSWORD_MESSAGE = "password must contain at least 8 symbols that includes:" +
            "one digit, one uppercase letter and one lowercase letter";

    private String passwordBeforeChange;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",message =INCORRECT_PASSWORD_MESSAGE)
    private String passwordAfterChange;
}
