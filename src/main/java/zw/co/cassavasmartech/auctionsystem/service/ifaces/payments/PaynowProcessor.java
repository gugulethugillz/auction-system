package zw.co.cassavasmartech.auctionsystem.service.ifaces.payments;

import zw.co.cassavasmartech.auctionsystem.common.GenericResponse;
import zw.co.cassavasmartech.auctionsystem.model.PaymentRequest;

/**
 * Created by alfred on 08 October 2020
 */
public interface PaynowProcessor {
    GenericResponse<PaymentRequest> handlePayment(PaymentRequest paymentRequest);
    void handlePollRequest();
}
