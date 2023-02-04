package com.finance.business.service;
import org.springframework.stereotype.Component;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class EmailComponent {
        private static final String email ="constantin.timofte.dev@gmail.com";
        private static final String password ="Ileniaaurora13";

    public static void sendEmail(String to, String subject, String body) throws  MessagingException {

        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");


        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });

        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(email));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(body);

        Transport transport = session.getTransport("smtp");
        transport.connect("smtp.gmail.com", email, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
    public static void sendEmail(String to, String subject, String body,int e) throws  MessagingException {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.postacert.vodafone.it");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props);

        MimeMessage mail = new MimeMessage(session);
        mail.setFrom(new InternetAddress(email));
        mail.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        mail.setSubject(subject);
        mail.setText(body);
        Transport.send(mail);

    }
}