package finalproject.ninegag.utilities.mail;

import finalproject.ninegag.exceptions.NotFoundException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;

public class MailSender {
    private static final String SENDER = "ninegagtalents@gmail.com";

    public static void sendMail(String to, String subject, String body) {
        final String username = SENDER;
        final String password;
        try(Scanner scanner = new Scanner(new FileInputStream("9gagEmailPassword.txt"))){
            if(scanner.hasNextLine()){
                password = scanner.nextLine();
            }
            else{
                password = null;
            }
        }catch (FileNotFoundException e){
            throw new NotFoundException("Missing file.");
        }
        Properties prop = new Properties();
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("Done");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}