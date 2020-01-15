package finalproject.ninegag.controller;

import finalproject.ninegag.exceptions.AuthorizationException;
import finalproject.ninegag.exceptions.BadRequestException;
import finalproject.ninegag.exceptions.CreatingEntityException;
import finalproject.ninegag.exceptions.NotFoundException;
import finalproject.ninegag.model.dto.ErrorDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Validated
@ControllerAdvice
@EnableWebMvc
public abstract class AbstractController extends ResponseEntityExceptionHandler {


//    @ExceptionHandler({NullPointerException.class})
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorDTO handleNullPointerException(NullPointerException e) {
//        ErrorDTO errorDTO = new ErrorDTO(
//                e.getClass().getSimpleName(),
//                HttpStatus.NOT_FOUND.value(),
//                LocalDateTime.now(),
//                e.getMessage());
//        return errorDTO;
//    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFound(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO(
                e.getClass().getSimpleName(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                e.getMessage());
        return errorDTO;
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleSQLExceptions(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO(
                e.getClass().getSimpleName(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                e.getMessage());
        return errorDTO;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequests(BadRequestException e) {
        ErrorDTO errorDTO = new ErrorDTO(
                e.getClass().getSimpleName(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                e.getMessage());
        return errorDTO;
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleUnauthorized(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO(
                e.getClass().getName(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now(),
                e.getMessage());
        return errorDTO;
    }

    @ExceptionHandler(CreatingEntityException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public void handleCreatingEntityException( HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleIOException(IOException e){
        ErrorDTO errorDTO = new ErrorDTO(
                e.getClass().getName(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                e.getMessage());
        return errorDTO;
    }


//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorDTO handleMethodArgumentNotValid(MethodArgumentNotValidException e){
//        ErrorDTO errorDTO = new ErrorDTO(
//                e.getClass().getName(),
//                HttpStatus.BAD_REQUEST.value(),
//                LocalDateTime.now(),
//                e.getMessage());
//        return errorDTO;
//    }

}
