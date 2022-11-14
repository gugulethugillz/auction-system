package zw.co.cassavasmartech.auctionsystem.service.ifaces;

import zw.co.cassavasmartech.auctionsystem.common.GenericResponse;
import zw.co.cassavasmartech.auctionsystem.forms.PaymentInitiationForm;
import zw.co.cassavasmartech.auctionsystem.model.PaymentRequest;
import zw.co.cassavasmartech.auctionsystem.repository.PaymentRequestRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRequestService {
   PaymentRequestRepository getRepository();
   GenericResponse<PaymentRequest> makePayment(PaymentInitiationForm paymentInitiationForm);
   Optional<PaymentRequest> findById(Long id);
   List<PaymentRequest> getByUsername(String username);
   List<PaymentRequest> getByUserId(Long userId);
   List<PaymentRequest> findAll();
   List<PaymentRequest> findByKeyword(String keyword);

}
