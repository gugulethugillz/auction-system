package zw.co.cassavasmartech.auctionsystem.repository;
import zw.co.cassavasmartech.auctionsystem.model.Category;
import java.math.BigDecimal;
import com.google.common.collect.Lists;
import zw.co.cassavasmartech.auctionsystem.common.enums.PaymentStatus;
import zw.co.cassavasmartech.auctionsystem.model.User;
import zw.co.cassavasmartech.auctionsystem.common.enums.PaymentMethod;
import org.junit.jupiter.api.AfterEach;
import zw.co.cassavasmartech.auctionsystem.model.PaymentRequest;
import java.time.OffsetDateTime;
import zw.co.cassavasmartech.auctionsystem.common.enums.EntityStatus;
import zw.co.cassavasmartech.auctionsystem.model.Asset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zw.co.cassavasmartech.auctionsystem.model.Bid;
import zw.co.cassavasmartech.auctionsystem.model.StatusHistory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by alfred on 12 October 2020
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class StatusHistoryRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StatusHistoryRepository statusHistoryRepository;

    private Asset asset;
    private PaymentRequest paymentRequest;
    private StatusHistory statusHistory;

    @BeforeEach
    public void init() {
        asset = getAsset(1L);
        entityManager.persist(asset);

        paymentRequest = getPaymentRequest(1L);
        entityManager.persist(paymentRequest);

        statusHistory = getStatusHistory(1L);
        entityManager.persist(statusHistory);

    }

    @AfterEach
    public void destroy() {
        entityManager.remove(paymentRequest);
        entityManager.remove(asset);
        entityManager.remove(statusHistory);
        paymentRequest = null;
        asset = null;
        statusHistory = null;
    }

    @Test
    public void givenAStatusHistoryEntry_whenPersist_thenShouldReturnPersistedStatusHistory() throws Exception {
        StatusHistory statusHistory = getStatusHistory(2L);
        StatusHistory persisted = statusHistoryRepository.save(statusHistory);
        assertThat(persisted).isNotNull();
        assertAll(
                () -> assertSame(persisted.getId(), statusHistory.getId())
        );
    }

    private Asset getAsset(Long id) {
        Asset asset = new Asset();
        asset.setName("test");
        asset.setDescription("test");
        asset.setBidStartDate(OffsetDateTime.now());
        asset.setBidEndDate(OffsetDateTime.now());
        asset.setBidStartPrice(new BigDecimal("0"));
        asset.setId(id);
        asset.setDateCreated(OffsetDateTime.now());
        asset.setLastUpdated(OffsetDateTime.now());
        asset.setStatus(EntityStatus.ACTIVE);
        asset.setCreatedBy("test");
        asset.setLastUpdatedBy("test");
        return asset;
    }

    private PaymentRequest getPaymentRequest(Long id) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setId(id);
        paymentRequest.setValue(BigDecimal.TEN);
        paymentRequest.setDate(OffsetDateTime.now());
        paymentRequest.setPaymentMethod(PaymentMethod.ECOCASH);
        paymentRequest.setPaymentStatus(PaymentStatus.PENDING);
        paymentRequest.setPollUrl("");
        paymentRequest.setTransactionStatus("");
        paymentRequest.setItem("");
        paymentRequest.setMobileNumber("");
        paymentRequest.setDescription("");
        paymentRequest.setMerchantReference("");
        paymentRequest.setPaynowReference("");
        paymentRequest.setSystemReference("");
        paymentRequest.setAsset(getAsset(id));
        paymentRequest.setDateCreated(OffsetDateTime.now());
        paymentRequest.setDate(OffsetDateTime.now());
        paymentRequest.setLastUpdated(OffsetDateTime.now());
        paymentRequest.setStatus(EntityStatus.ACTIVE);
        paymentRequest.setCreatedBy("test");
        paymentRequest.setLastUpdatedBy("test");
        return paymentRequest;
    }

    private StatusHistory getStatusHistory(Long id) {
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setEntityStatus("Test");
        statusHistory.setOtherStatus("Another Status");
        statusHistory.setAsset(getAsset(id));
        statusHistory.setPaymentRequest(getPaymentRequest(id));
        statusHistory.setId(id);
        statusHistory.setDateCreated(OffsetDateTime.now());
        statusHistory.setLastUpdated(OffsetDateTime.now());
        statusHistory.setStatus(EntityStatus.ACTIVE);
        statusHistory.setCreatedBy("alfred");
        statusHistory.setLastUpdatedBy("alfred");
        return statusHistory;
    }
}
