package finalproject.ninegag.model.mail;

import finalproject.ninegag.model.entity.User;
import finalproject.ninegag.model.repository.UserRepository;

public class SuccessfullyChangedPassword extends Thread {

    private static final String SUBJECT = "Changed Password!";

    private String text = "You have changed your password correctly!";
    private User user;
    private UserRepository userRepository;

    public SuccessfullyChangedPassword(User user,UserRepository userRepository){
        this.user = user;
        this.userRepository = userRepository;
    }

    @Override
    public void run() {
        MailSender.sendMail(user.getEmail(),SUBJECT,text);
    }
}
