package management_farm.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * Date: 11/24/2023
 * Project: Management-Farm
 * Author: ManhTien
 */
@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("tien.manh.do99@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        if (list != null && list.size() > 0) {
            message.setCc(getCCArray(list));
        }

        emailSender.send(message);
    }

    private String[] getCCArray(List<String> list) {
        String[] cc = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            cc[i] = list.get(i);
        }
        return cc;
    }

    public void forgotMail(String to, String subject, String userLogin, String token) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("tien.manh.do99@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        String htmlMsg = "<p><b>Your Login details for Farm System</b><br><b>Email: </b> " + to + " <br><b>" +
                "User Login: </b>" + userLogin + "<br><b>Token: </b> " + token + "<br>" +
                "<b style=" + "color:red" + ">Token Effective in 2 minute.</b><br><a href=\"http://localhost:4200/" + token + "\">Click here to change password</a></p>";
        message.setContent(htmlMsg, "text/html");
        emailSender.send(message);
    }
}
