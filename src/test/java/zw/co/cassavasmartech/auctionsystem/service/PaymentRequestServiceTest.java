package zw.co.cassavasmartech.auctionsystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import zw.co.cassavasmartech.auctionsystem.model.*;
import zw.co.cassavasmartech.auctionsystem.repository.PaymentRequestRepository;
import zw.co.cassavasmartech.auctionsystem.service.impl.PaymentRequestServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PaymentRequestServiceTest {

    @InjectMocks
    private PaymentRequestServiceImpl paymentService;

    @Mock
    private PaymentRequestRepository paymentRequestRepository;
    private PaymentRequest paymentRequest;

    @BeforeEach
    public void init(){
        paymentRequest = new PaymentRequest();
        paymentRequest.setId(1L);
//
//        paymentRequest.setPaymentMethod(PaymentMethod.ECOCASH);

        User user = new User();
        user.setUsername("gerald.garinga@gmail.com");



    }

    @AfterEach
    public void cleanUp() {

    }

    @Test
    public void givenAnId_whenFindPayment_shouldReturnPayment() throws Exception {
        when(paymentRequestRepository.findById(any())).thenReturn(Optional.of(this.paymentRequest));
        Optional<PaymentRequest> thePayment = paymentService.findById(1L);
        assertTrue(thePayment.isPresent(), "PaymentRequest not found");
        PaymentRequest paymentRequest = thePayment.get();
        assertEquals(1L, paymentRequest.getId());
    }
    @Test
    public void givenAnNonExistingId_whenFindPayment_shouldReturnPayment() throws Exception {
        when(paymentRequestRepository.findById(any())).thenReturn(Optional.of(this.paymentRequest));
        Optional<PaymentRequest> thePayment = paymentService.findById(22L);
        assertTrue(thePayment.isPresent(), "PaymentRequest not found");
        PaymentRequest paymentRequest = thePayment.get();
        assertEquals(1L, paymentRequest.getId());
    }


    @Test
    public void givenExistingPayment_whenFindByUser_thenShouldReturnEmptyList() throws Exception {
        when(paymentRequestRepository.getByUsername(any())).thenReturn(List.of(paymentRequest));
        List<PaymentRequest> userPaymentRequests = paymentService.getByUsername("ggalsons@gmail.com");
        assertAll(
                () -> assertThat(userPaymentRequests).isNotNull(),
                () -> assertTrue(userPaymentRequests.size() > 0),
                () -> assertThat(userPaymentRequests.get(0).getUser()).isEqualTo(paymentRequest.getUser())
        );
    }

    @Test
    public void givenAnNonExistingPayment_whenFindByUser_thenShouldReturnEmptyList() throws Exception {
        when(paymentRequestRepository.getByUsername(any())).thenReturn(List.of(paymentRequest));
        List<PaymentRequest> userPaymentRequests = paymentService.getByUsername("hshsh@gmail.com");
        assertAll(
                () -> assertThat(userPaymentRequests).isNotNull(),
                () -> assertTrue(userPaymentRequests.size() > 0),
                () -> assertThat(userPaymentRequests.get(0).getUser()).isEqualTo(paymentRequest.getUser())
        );
    }

    @Test
    public void givenPaymentRecords_whenFindAll_thenShouldReturnNonEmptyList() throws Exception {
        when(paymentRequestRepository.findAll()).thenReturn(List.of(paymentRequest));
        List<PaymentRequest> paymentRequestList = paymentService.findAll();
        assertAll(
                () -> assertThat(paymentRequestList).isNotNull(),
                () -> assertTrue(paymentRequestList.size() > 0),
                () -> assertThat(paymentRequestList.get(0).getUser()).isEqualTo(paymentRequest.getUser())
        );
    }

    @Test
    public void givenNoPaymentRecord_whenFindAll_thenShouldReturnEmptyList() throws Exception {
        when(paymentRequestRepository.findAll()).thenReturn(Collections.emptyList());

        List<PaymentRequest> paymentRequestList = paymentService.findAll();
        assertAll(
                () -> assertThat(paymentRequestList).isNotNull(),
                () -> assertTrue(paymentRequestList.size() == 0)
        );
    }






}
