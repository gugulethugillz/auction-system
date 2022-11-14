package zw.co.cassavasmartech.auctionsystem.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zw.co.cassavasmartech.auctionsystem.model.PaymentRequest;
import zw.co.cassavasmartech.auctionsystem.projections.PaymentReportEntry;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Created by alfred on 18 September 2020
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PaymentRequestRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PaymentRequestRepository paymentRequestRepository;

    @BeforeEach
    public void init() {

    }

    @Test
    public void givenAPayment_whenPersistPayment_thenShouldReturnPersistedPayment() throws Exception {
        PaymentRequest paymentRequest = new PaymentRequest();
        PaymentRequest newPaymentRequest = entityManager.persist(paymentRequest);
        assertThat(newPaymentRequest).isNotNull();
    }

//    @Test
//    public void givenExistingData_whenGetPaymentReportEntries_shouldReturnNonEmptyList() throws Exception {
//        final OffsetDateTime start = OffsetDateTime.now().minusDays(1);
//        final OffsetDateTime end = OffsetDateTime.now().plusDays(1);
//        final List<PaymentReportEntry> paymentReportEntries = paymentRequestRepository.getPaymentReportEntries(start, end);
//        assertAll(
//                () -> assertThat(paymentReportEntries).isNotNull(),
//                () -> assertThat(paymentReportEntries).isNotEmpty()
//        );
//    }

}

