package zw.co.cassavasmartech.auctionsystem.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.auctionsystem.common.GenericResponse;
import zw.co.cassavasmartech.auctionsystem.common.enums.ResponseCode;
import zw.co.cassavasmartech.auctionsystem.forms.PaymentInitiationForm;
import zw.co.cassavasmartech.auctionsystem.model.PaymentRequest;
import zw.co.cassavasmartech.auctionsystem.repository.PaymentRequestRepository;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.PaymentRequestService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.payments.PaynowProcessor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentRequestServiceImpl implements PaymentRequestService {

    private final PaymentRequestRepository paymentRequestRepository;
    private final PaynowProcessor paynowProcessor;

    @Override
    public PaymentRequestRepository getRepository() {
        return paymentRequestRepository;
    }

    @Override
    public GenericResponse<PaymentRequest> makePayment(PaymentInitiationForm paymentInitiationForm) {
        return paymentRequestRepository.findById(paymentInitiationForm.getPaymentRequestId()).map(paymentRequest -> {
            paymentRequest.setPaymentMethod(paymentInitiationForm.getPaymentMethod());
            paymentRequest.setMobileNumber(paymentInitiationForm.getMobileNumber());
            paymentRequest.setDate(OffsetDateTime.now());
            return paynowProcessor.handlePayment(paymentRequest);
        }).orElseGet(() -> {
            String message = String.format("Failed to find payment request with ID %d", paymentInitiationForm.getPaymentRequestId());
            return createGenericResponse(null, message, ResponseCode.FAIL);
        });
    }

    public <T> GenericResponse<T> createGenericResponse(T entity, String narrative, ResponseCode responseCode) {
        return GenericResponse.<T>builder()
                .entity(entity)
                .narrative(narrative)
                .responseCode(responseCode)
                .build();
    }

    @Override
    public Optional<PaymentRequest> findById(Long id) {
        return paymentRequestRepository.findById(id);
    }

    @Override
    public List<PaymentRequest> getByUsername(String username) {
        return paymentRequestRepository.getByUsername(username);
    }

    @Override
    public List<PaymentRequest> getByUserId(Long userId) {
        return paymentRequestRepository.findByUserId(userId);
    }

    @Override
    public List<PaymentRequest> findAll() {
        return paymentRequestRepository.findAll();
    }

    @Override
    public List<PaymentRequest> findByKeyword(String keyword) {
        return paymentRequestRepository.findByKeyword(keyword);
    }
}


