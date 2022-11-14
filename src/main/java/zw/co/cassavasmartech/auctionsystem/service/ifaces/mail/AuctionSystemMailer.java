package zw.co.cassavasmartech.auctionsystem.service.ifaces.mail;

/**
 * Created by alfred on 08 October 2020
 */
public interface AuctionSystemMailer {
    void sendEmail(String subject, String message, String emailAddress);
}
