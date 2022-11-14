package zw.co.cassavasmartech.auctionsystem.controllers;

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
import zw.co.cassavasmartech.auctionsystem.model.Asset;
import zw.co.cassavasmartech.auctionsystem.model.User;
import zw.co.cassavasmartech.auctionsystem.repository.CategoryRepository;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.AssetService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.CategoryService;
import zw.co.cassavasmartech.auctionsystem.storage.StorageService;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AssetController.class)
public class AssetControllerTest {
    @MockBean
    private AssetService assetService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private StorageService storageService;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private MockMvc mockMvc;

    private Asset asset;

    private Admin admin;

    private User user;

    @BeforeEach
    public void init() {
        asset = new Asset();
        asset.setName("Laptop");

        admin = new Admin();
        admin.setFirstName("Test");
        admin.setMiddleName("");
        admin.setLastName("Test");
        admin.setNationalId("");
        admin.setPassportNumber("");
        admin.setAddress("");
        admin.setEmail("");
        admin.setPhone("");
        admin.setPassword("");
        admin.setUsername("");
        admin.setVersion(0L);

        user = new User();
        user.setFirstName("Test");
        user.setMiddleName("");
        user.setLastName("Test");
        user.setNationalId("");
        user.setPassportNumber("");
        user.setAddress("");
        user.setEmail("");
        user.setPhone("");
        user.setPassword("");
        user.setUsername("");
        user.setVersion(0L);
    }

    @AfterEach
    public void destroy() {
        asset = null;
        admin = null;
        user = null;
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void givenTheAdminAssetsPath_whenGetRequest_thenShouldReturnPageContainingString() throws Exception {
        Admin admin = new Admin();
        admin.setFirstName("Test");
        admin.setMiddleName("");
        admin.setLastName("Test");
        admin.setNationalId("");
        admin.setPassportNumber("");
        admin.setAddress("");
        admin.setEmail("");
        admin.setPhone("");
        admin.setPassword("");
        admin.setUsername("");
        admin.setVersion(0L);

        mockMvc.perform(get("/admin/assets")
                .with(csrf()).with(user(admin))
        )
                .andExpect(view().name("admin/assets_admin"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("meta name")));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void givenTheUserAssetsPath_whenGetRequest_thenShouldReturnPageContainingString() throws Exception {
        when(assetService.findAllAssets()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/assets")
                .with(csrf())
        .with(user(user)))
                .andExpect(view().name("assets_user"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("meta name")));
    }

    @Test
    @WithMockUser("ADMIN")
    public void givenAnImageName_whenDeleteRequest_thenShouldDeleteAssetImageAndRedirectViewAsset()
            throws Exception {
        Asset asset = new Asset();
        asset.setId(1L);

        asset.setImageNames(Collections.singletonList("image1.png"));
        when(assetService.findById(1L)).thenReturn(Optional.of(asset));

        mockMvc.perform(post("/admin/asset/delete/image/jsdfjdsfdskds.png")
                .with(csrf())
                .with(user(admin))
                .param("id", "61")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/assets/"));
    }


}
