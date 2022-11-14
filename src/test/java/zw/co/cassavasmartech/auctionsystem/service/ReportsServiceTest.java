package zw.co.cassavasmartech.auctionsystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zw.co.cassavasmartech.auctionsystem.model.Admin;
import zw.co.cassavasmartech.auctionsystem.model.PaymentRequest;
import zw.co.cassavasmartech.auctionsystem.model.Person;
import zw.co.cassavasmartech.auctionsystem.model.User;
import zw.co.cassavasmartech.auctionsystem.repository.AdminRepository;
import zw.co.cassavasmartech.auctionsystem.repository.PaymentRequestRepository;
import zw.co.cassavasmartech.auctionsystem.repository.UserRepository;
import zw.co.cassavasmartech.auctionsystem.service.impl.PersonServiceImpl;
import zw.co.cassavasmartech.auctionsystem.service.impl.reports.ReportsServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by alfred on 21 September 2020
 */
@ExtendWith(SpringExtension.class)
public class ReportsServiceTest {
    @InjectMocks
    private ReportsServiceImpl reportsService;

    @Mock
    private PaymentRequestRepository paymentRequestRepository;

    @BeforeEach
    public void init() {

    }

    @AfterEach
    public void cleanUp() {

    }

    @Test
    public void givenAnId_whenFindPerson_shouldReturnPerson() throws Exception {

    }
}
