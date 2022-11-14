package zw.co.cassavasmartech.auctionsystem.controllers;
import zw.co.cassavasmartech.auctionsystem.model.PaymentRequest;
import com.google.common.collect.Lists;
import java.time.OffsetDateTime;
import zw.co.cassavasmartech.auctionsystem.common.enums.EntityStatus;
import zw.co.cassavasmartech.auctionsystem.model.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import zw.co.cassavasmartech.auctionsystem.model.Asset;
import zw.co.cassavasmartech.auctionsystem.model.Bid;
import zw.co.cassavasmartech.auctionsystem.model.User;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.AssetService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.BidService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.PersonService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by alfred on 24 September 2020
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(BidController.class)
public class BidControllerTest {
    @MockBean
    private BidService bidService;

    @MockBean
    private PersonService personService;

    @MockBean
    private AssetService assetService;

    @MockBean
    private UserDetailsService userDetailsService;

   @Autowired
    private MockMvc mockMvc;

    private Bid bid;
    private Asset asset;
    private User user;

    @BeforeEach
    public void init() {
        user = new User();
        user.setFirstName("Test");
        user.setMiddleName("Test");
        user.setLastName("Test");
        user.setNationalId("Test");
        user.setPassportNumber("Test");
        user.setAddress("Test");
        user.setEmail("Test");
        user.setPhone("Test");
        user.setPassword("Test");
        user.setUsername("Test");
        user.setCreatedBy("Test");
        user.setLastUpdatedBy("Test");

        asset = new Asset();
        asset.setName("");
        asset.setDescription("");
        asset.setBidStartPrice(new BigDecimal("0"));
        asset.setCategory(new Category());
        asset.setBids(Lists.newArrayList());
        asset.setPaymentRequest(new PaymentRequest());
        asset.setId(1L);
        asset.setCreatedBy("");
        asset.setLastUpdatedBy("");

        bid = new Bid();
        bid.setId(1L);
        bid.setAsset(asset);
        bid.setValue(new BigDecimal(100.00));

    }

    @AfterEach
    public void destroy() {
        bid = null;
        asset = null;
        user = null;
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void givenTheBidPath_whenGetRequest_thenShouldReturnPageContainingBids() throws Exception {
        mockMvc.perform(get("/bids").with(user(user)))
                .andExpect(view().name("bids"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Auction System - Cassava Smartech")));
    }


    @Test
    @WithMockUser(roles = {"USER"})
    public void testRenderBids() throws Exception {

        when(bidService.createBid(any())).thenReturn(Optional.of(bid));
        when(assetService.findById(any())).thenReturn(Optional.of(asset));
        mockMvc.perform(
                post("/bids/create")
                        .with(csrf())
                        .with(user(user))
                        .param("value", "100.0")
                        .param("paymentDate", "2020-09-30")
                        .param("assetId", "1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(redirectedUrl("/assets/" + asset.getId()));
    }
}
