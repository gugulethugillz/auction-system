package zw.co.cassavasmartech.auctionsystem.service.impl.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.mail.AuctionSystemMailer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by alfred on 08 October 2020
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionSystemMailerImpl implements AuctionSystemMailer {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String subject, String message, String emailAddress) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }

    void sendEmailWithAttachment(String subject, String message, String emailAddress) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(emailAddress);
        helper.setSubject(subject);
        helper.setText(message);
        helper.addAttachment("bike.png", new ClassPathResource("bike.png"));
        javaMailSender.send(msg);
    }
}
