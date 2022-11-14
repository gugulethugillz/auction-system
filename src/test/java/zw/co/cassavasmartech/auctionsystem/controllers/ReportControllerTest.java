package zw.co.cassavasmartech.auctionsystem.controllers;
import java.time.OffsetDateTime;
import zw.co.cassavasmartech.auctionsystem.common.enums.EntityStatus;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import zw.co.cassavasmartech.auctionsystem.model.Admin;
import zw.co.cassavasmartech.auctionsystem.model.User;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.reports.ReportsService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by alfred on 12 October 2020
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(ReportController.class)
public class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private ReportsService reportsService;

    private Admin admin;
    private User user;

    @BeforeEach
    public void init() {
        admin = new Admin();
        admin.setFirstName("John");
        admin.setMiddleName("");
        admin.setLastName("Doe");
        admin.setEmail("email@example.com");
        admin.setUsername("email@example.com");
        admin.setId(1L);

        user = new User();
        user.setFirstName("John");
        user.setMiddleName("");
        user.setLastName("Doe");
        user.setEmail("email@example.com");
        user.setUsername("email@example.com");
        user.setId(1L);
    }

    @AfterEach
    public void afterEach() {
        admin = null;
        user = null;
    }

    @Test
    @WithMockUser(roles = "{ADMIN}")
    public void givenPathToPaymentReportsPage_whenGet_shouldReturnStatusOk() throws Exception {
        mockMvc.perform(get("/admin/reports/payments").with(user(admin)))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/reports/payment_reports"))
                .andExpect(content().string(containsString("Payment Reports")));
    }

    @Test
    @WithMockUser(roles = "{USER}")
    public void givenPathToPaymentReportsPageAndWrongUser_whenGet_shouldReturnStatusForbidden() throws Exception {
        mockMvc.perform(get("/admin/reports/payments").with(user(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "{ADMIN}")
    public void givenPathToAssetReportsPage_whenGet_shouldReturnStatusOk() throws Exception {
        mockMvc.perform(get("/admin/reports/assets").with(user(admin)))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/reports/asset_reports"))
                .andExpect(content().string(containsString("Asset Reports")));
    }

    @Test
    @WithMockUser(roles = "{USER}")
    public void givenPathToAssetReportsPageWithWrongUser_whenGet_shouldReturnStatusForbidden() throws Exception {
        mockMvc.perform(get("/admin/reports/assets").with(user(user)))
                .andExpect(status().isForbidden());
    }
}
