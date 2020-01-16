package finalproject.ninegag.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class ChangePostTitleDTO {

    @NotBlank
    private String currentTitle;
    @NotBlank
    private String newTitle;

}
