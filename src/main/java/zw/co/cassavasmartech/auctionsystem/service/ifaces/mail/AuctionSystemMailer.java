package zw.co.cassavasmartech.auctionsystem.service.ifaces.mail;

public interface AuctionSystemMailer {
    void sendEmail(String subject, String message, String emailAddress);
}
