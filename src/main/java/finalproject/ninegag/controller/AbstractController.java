package finalproject.ninegag.controller;

import finalproject.ninegag.model.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;

public abstract class AbstractController {

    @ExceptionHandler(SQLException.class)
    public ErrorDTO handleSQLExceptions(Exception e){
        ErrorDTO errorDTO = new ErrorDTO(e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                e.getClass().getName());

        return errorDTO;
    }
}
