package finalproject.ninegag.utilities.mail;

import finalproject.ninegag.model.entity.User;
import finalproject.ninegag.model.repository.UserRepository;

public class SuccessfullyChangedUsername  extends Thread{
    private static final String SUBJECT = "Changed Username!";

    private String text = "You have changed your username correctly!";
    private User user;
    private UserRepository userRepository;

    public SuccessfullyChangedUsername(User user,UserRepository userRepository){
        this.user = user;
        this.userRepository = userRepository;
    }

    @Override
    public void run() {
        MailSender.sendMail(user.getEmail(),SUBJECT,text);
    }
}
