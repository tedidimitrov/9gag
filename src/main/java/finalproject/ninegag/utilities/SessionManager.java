package finalproject.ninegag.utilities;

import finalproject.ninegag.exceptions.AuthorizationException;
import finalproject.ninegag.model.entity.User;

import javax.servlet.http.HttpSession;

public class SessionManager {

    private static final String SESSION_KEY_LOGGED_USER = "loggedUser";
    public static boolean isValidSession(HttpSession session) {
        if (session.isNew()) {
            return false;
        }
        if (session.getAttribute(SESSION_KEY_LOGGED_USER) == null) {
            return false;
        }
        return true;
    }
    public static User getLoggedUser(HttpSession session) throws AuthorizationException {
        if (isValidSession(session)) {
            return (User) session.getAttribute("user");
        }
        throw new AuthorizationException("You must login first!");
    }
    public static void logUser(HttpSession session, User user) {
        session.setAttribute(SESSION_KEY_LOGGED_USER, true);
    }
}
