package zw.co.cassavasmartech.auctionsystem.controllers;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.web.servlet.MockMvc;

import zw.co.cassavasmartech.auctionsystem.common.enums.PaymentMethod;

import zw.co.cassavasmartech.auctionsystem.model.Admin;
import zw.co.cassavasmartech.auctionsystem.model.PaymentRequest;
import zw.co.cassavasmartech.auctionsystem.model.User;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.PaymentRequestService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.PersonService;


import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(PaymentController.class)
public class PaymentRequestControllerTest {

    @MockBean
    PaymentRequestService paymentRequestService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private PersonService personService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private MockMvc mockMvc;

    private PaymentRequest paymentRequest;
    private User user;
    private Admin admin;

    @BeforeEach
    public void init() {
        paymentRequest = new PaymentRequest();
        paymentRequest.setValue(new BigDecimal(200));
        paymentRequest.setId(1L);
        paymentRequest.setPaymentMethod(PaymentMethod.ECOCASH);

        user = new User();
        user.setFirstName("gerald");
        user.setUsername("gerald@gmail.com");

        admin = new Admin();
        admin.setFirstName("gerald");
        admin.setUsername("gerald@gmail.com");
    }

    @AfterEach
    public void destroy() {
        paymentRequest = null;
        user = null;
        admin = null;
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void givenPaymentRecords_whenFindAll_thenShouldReturn_PaymentList() throws Exception {
        mockMvc.perform(get("/admin/payments")
                .with(user(admin))
        )

                .andExpect(view().name("admin/payments"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Auction System - Cassava Smartech")));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void givenPaymentRecords_whenFindByUsername_thenShouldReturn_Payment() throws Exception {
        when(personService.findByUsername(any())).thenReturn(Optional.of(user));
        mockMvc.perform(get("/payments")
                .with(user(user))
        )
                .andExpect(view().name("user_payments"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Auction System - Cassava Smartech")));


    }


}
