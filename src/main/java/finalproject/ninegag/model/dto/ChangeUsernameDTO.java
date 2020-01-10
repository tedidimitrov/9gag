package finalproject.ninegag.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class ChangeUsernameDTO {

    private static final String INCORRECT_USERNAME = "username should be at between 8 and 20 symbols.Allowed symbols:" +
                                                      "letters,digits, '.' and '_'";

    private String usernameBeforeChange;
    @NotBlank
    @Pattern(regexp = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$",message = INCORRECT_USERNAME)
    private String usernameAfterChange;
}
