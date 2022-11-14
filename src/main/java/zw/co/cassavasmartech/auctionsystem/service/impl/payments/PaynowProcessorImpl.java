package zw.co.cassavasmartech.auctionsystem.service.impl.payments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.auctionsystem.common.AuctionSystemProperties;
import zw.co.cassavasmartech.auctionsystem.common.GenericResponse;
import zw.co.cassavasmartech.auctionsystem.common.Utils;
import zw.co.cassavasmartech.auctionsystem.common.enums.EntityStatus;
import zw.co.cassavasmartech.auctionsystem.common.enums.PaymentMethod;
import zw.co.cassavasmartech.auctionsystem.common.enums.PaymentStatus;
import zw.co.cassavasmartech.auctionsystem.common.enums.ResponseCode;
import zw.co.cassavasmartech.auctionsystem.model.PaymentRequest;
import zw.co.cassavasmartech.auctionsystem.model.StatusHistory;
import zw.co.cassavasmartech.auctionsystem.repository.PaymentRequestRepository;
import zw.co.cassavasmartech.auctionsystem.repository.StatusHistoryRepository;
import zw.co.cassavasmartech.auctionsystem.schedulers.Scheduler;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.mail.AuctionSystemMailer;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.payments.PaynowProcessor;
import zw.co.paynow.constants.MobileMoneyMethod;
import zw.co.paynow.constants.TransactionStatus;
import zw.co.paynow.core.Payment;
import zw.co.paynow.core.Paynow;
import zw.co.paynow.responses.MobileInitResponse;
import zw.co.paynow.responses.StatusResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static zw.co.cassavasmartech.auctionsystem.common.Utils.getPaymentRequestHelper;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaynowProcessorImpl implements PaynowProcessor {
    private final PaymentRequestRepository paymentRequestRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final AuctionSystemMailer mailer;
    private final AuctionSystemProperties props;

    public <T> GenericResponse<T> createGenericResponse(T entity, String narrative, ResponseCode responseCode) {
        return GenericResponse.<T>builder()
                .entity(entity)
                .narrative(narrative)
                .responseCode(responseCode)
                .build();
    }

    @Override
    public GenericResponse<PaymentRequest> handlePayment(PaymentRequest paymentRequest) {
        log.info("Sending paymentRequest request to paynow for processing");
        paymentRequest.setPaymentStatus(PaymentStatus.PENDING);
        handlePaymentRequest(paymentRequest);
        return createGenericResponse(paymentRequest, "Processing Payment", ResponseCode.SUCCESS);
    }

    private void handlePaymentRequest(PaymentRequest paymentRequest) {
        log.info("Handling paynow paymentRequest processing request.");
        String merchantReference = UUID.randomUUID().toString();
        String description = "Client purchase";
        String integrationId = props.getPaynowIntegrationId();
        String integrationKey = props.getPaynowIntegrationKey();

        Paynow paynow = new Paynow(integrationId, integrationKey);
        Payment paynowPayment = paynow.createPayment(merchantReference, paymentRequest.getUser().getEmail());

        String item = "Bid PaymentRequest";
        paynowPayment.add(item, paymentRequest.getValue());
        paymentRequest.setItem(item);

        paymentRequest.setMerchantReference(merchantReference);
        paymentRequest.setDescription(description);

        paynowPayment.setCartDescription(description);

        PaymentRequest savedPaymentRequest = paymentRequestRepository.save(paymentRequest);
        statusHistoryRepository.save(getStatusHistory(savedPaymentRequest, "Details Set"));

        String mobileNumber = paymentRequest.getMobileNumber();
        log.info("We'll send payment prompt to mobile number: {}", mobileNumber);

        MobileMoneyMethod mobileMoneyMethod = getPaynowPaymentMethod(paymentRequest.getPaymentMethod());
        MobileInitResponse response = paynow.sendMobile(paynowPayment, mobileNumber, mobileMoneyMethod);

        if(response.success()) {
            log.info("Payment request was successful.");
            log.info("Getting the instructions to show the user");
            String instructions = response.instructions();
            log.info("User instructions: {}", instructions);
            TransactionStatus status = response.getStatus();

            log.info("Get poll url of the transaction");
            String pollUrl = response.pollUrl();

            paymentRequest.setPaymentStatus(PaymentStatus.PROCESSING_IN_PROGRESS);
            paymentRequest.setPollUrl(pollUrl);
            paymentRequest.setTransactionStatus(status.name());
            PaymentRequest p1 = paymentRequestRepository.save(paymentRequest);
            statusHistoryRepository.save(getStatusHistory(p1, "New Paynow Status: " + status.name()));
        } else {
            log.error("Payment request failed.");
            String errors = response.getErrors().stream().reduce("", (s, s1) -> s + "\n" + s1);
            log.error("Errors: {}", errors);
            savedPaymentRequest.setTransactionStatus(response.getStatus().name());
            savedPaymentRequest.setPaymentStatus(PaymentStatus.FAILED);
            paymentRequestRepository.save(savedPaymentRequest);
            statusHistoryRepository.save(getStatusHistory(savedPaymentRequest, errors));
        }
    }

    private MobileMoneyMethod getPaynowPaymentMethod(PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case ECOCASH:
                return MobileMoneyMethod.ECOCASH;
            case ONE_MONEY:
                return MobileMoneyMethod.ONEMONEY;
            case TELECASH:
                return MobileMoneyMethod.TELECASH;
            case ZIPIT:
            default:
                return null;
        }
    }

    private StatusHistory getStatusHistory(PaymentRequest paymentRequest, String description) {
        return getPaymentRequestHelper(paymentRequest, description, "paynow-processor");
    }

    @Override
    public void handlePollRequest() {
//        log.info("Poll request received at: {}", OffsetDateTime.now());
        List<PaymentRequest> paymentRequests = paymentRequestRepository.findByPaymentStatus(PaymentStatus.PROCESSING_IN_PROGRESS);

        if(paymentRequests.size() > 0) {
            handlePendingPaymentRequests(paymentRequests);
        } else {
//            log.info("No payment requests in status {}", PaymentStatus.PROCESSING_IN_PROGRESS);
        }
    }

    private void handlePendingPaymentRequests(List<PaymentRequest> paymentRequests) {
        String integrationId = props.getPaynowIntegrationId();
        String integrationKey = props.getPaynowIntegrationKey();
        Paynow paynow = new Paynow(integrationId, integrationKey);

        paymentRequests.stream().forEach(paymentRequest -> {
            String pollUrl = paymentRequest.getPollUrl();
            StatusResponse status = paynow.pollTransaction(pollUrl);
            if(status.isPaid()) {
                final TransactionStatus transactionStatus = status.getStatus();
                paymentRequest.setPaynowReference(status.getPaynowReference());
                paymentRequest.setPaymentStatus(PaymentStatus.SUCCESSFUL);
                paymentRequest.setTransactionStatus(transactionStatus.name());
                paymentRequestRepository.save(paymentRequest);
                statusHistoryRepository.save(getStatusHistory(paymentRequest, "Paid"));
                handlePaymentRequestSuccessful(paymentRequest);
            } else {
                if(paymentRequest.getDate().plusMinutes(5).isBefore(OffsetDateTime.now())) {
                    log.info("Payment request timed out for {}", paymentRequest.getMerchantReference());
                    log.info("Failing payment request {}", paymentRequest.getMerchantReference());
                    paymentRequest.setPaymentStatus(PaymentStatus.FAILED);
                    paymentRequestRepository.save(paymentRequest);
                    statusHistoryRepository.save(getStatusHistory(paymentRequest, "Request timed out"));
                } else {
                    log.info("Payment request Not yet paid: {}", paymentRequest.getMerchantReference());
                }
            }
        });
    }

    private void handlePaymentRequestSuccessful(PaymentRequest paymentRequest) {
        String emailAddress = paymentRequest.getUser().getEmail();
        mailer.sendEmail("Payment Received", getPaymentReceivedMessage(paymentRequest), emailAddress);
    }

    private String getPaymentReceivedMessage(PaymentRequest paymentRequest) {
        StringBuilder sb = new StringBuilder("Hi ")
                .append(paymentRequest.getUser().getFullName()).append("\n\n")
                .append("We have received your payment on asset ")
                .append(paymentRequest.getAsset().getName())
                .append(" on ").append(paymentRequest.getDateCreated().format(Utils.DATE_TIME_FORMATTER))
                .append(".\n\n")
                .append("Please make arrangements to come and collect the asset")
                .append("\n\n")
                .append("Kindly")
                .append("\n\n")
                .append("The BidTeam");
        return sb.toString();
    }
}
