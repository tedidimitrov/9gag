package finalproject.ninegag.utilities.mail;

import finalproject.ninegag.model.entity.User;
import finalproject.ninegag.model.repository.UserRepository;

public class WelcomeToCommunity extends Thread {

    private static final String SUBJECT = "Welcome";

    private String text = "Welcome to 9gagTalents Community!" +
            "   Prepare for more amazing experiences!";
    private User user;
    private UserRepository userRepository;

    public WelcomeToCommunity(User user,UserRepository userRepository){
        this.user = user;
        this.userRepository = userRepository;
    }

    @Override
    public void run() {
        MailSender.sendMail(user.getEmail(),SUBJECT,text);
    }
}
