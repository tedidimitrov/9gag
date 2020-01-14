package finalproject.ninegag.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ErrorDTO {

    private String msg;
    private int status;
    @JsonFormat(pattern = "HH:mm:ss  dd-MM-yyyy")
    private LocalDateTime time;
    private String exceptionType;

    public ErrorDTO(String exceptionType, int status, LocalDateTime time, String msg) {
        this.msg = msg;
        this.status = status;
        this.time = time;
        this.exceptionType = exceptionType;
    }
}
