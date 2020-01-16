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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Validated
@ControllerAdvice
@EnableWebMvc
public abstract class AbstractController extends ResponseEntityExceptionHandler {


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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleConstraintViolationException(RuntimeException e) {
        ErrorDTO error = new ErrorDTO(e.getClass().getSimpleName(),HttpStatus.BAD_REQUEST.value(),LocalDateTime.now(),"Incorrect inputs!");
        return new ResponseEntity<>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AuthorizationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleUnauthorized(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO(
                e.getClass().getName(),
                HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now(),
                e.getMessage());
        return errorDTO;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    protected ErrorDTO handleException(Exception e) {
        return new ErrorDTO(e.getClass().getSimpleName(),HttpStatus.SERVICE_UNAVAILABLE.value(),LocalDateTime.now(),"Oops! Something happened.Please try again later.");
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
//
//    @ExceptionHandler(NumberFormatException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorDTO handleHttpMessageNotReadable(NumberFormatException e){
//        ErrorDTO errorDTO = new ErrorDTO(
//                e.getClass().getName(),
//                HttpStatus.BAD_REQUEST.value(),
//                LocalDateTime.now(),
//                "You must enter the right data format!");
//        return errorDTO;
//    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleHttpMessageNotReadable(MethodArgumentTypeMismatchException e) {
        ErrorDTO errorDTO = new ErrorDTO(
                e.getClass().getName(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "You must enter the right data format!");
        return errorDTO;
    }
}
