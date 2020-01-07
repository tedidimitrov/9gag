package finalproject.ninegag.exceptions;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException(){
        super("You must log in to use this service!");
    }

}
