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
import zw.co.cassavasmartech.auctionsystem.forms.CategoryForm;
import zw.co.cassavasmartech.auctionsystem.model.Admin;
import zw.co.cassavasmartech.auctionsystem.model.Category;
import zw.co.cassavasmartech.auctionsystem.model.User;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.CategoryService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.PersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Tinashe & Calvin
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    @MockBean
    private PersonService personService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private CategoryService categoryService;

    private Admin admin;
    private User user;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        admin = new Admin();
        admin.setFirstName("Tinashe");
        admin.setUsername("admin@auctionsystem.co.zw");
        admin.setPassword("#pass123");

        user = new User();
        user.setFirstName("Tinashe");
        user.setUsername("tdzwokora@gmail.com");
        user.setPassword("#pass123");
    }

    @AfterEach
    public void destroy() {
        admin = null;
        user = null;
    }


    @Test
    @WithMockUser("ADMIN")
    public void givenTheRootPath_whenGetRequest_thenShouldReturnPageContainingListOfCategories() throws Exception {

        List<Category> categories = new ArrayList<>();
        Category category1 = new Category();
        category1.setName("Furniture");
        categories.add(category1);

        Category category2 = new Category();
        category2.setName("Electronics");
        categories.add(category2);

        given(categoryService.findAll()).willReturn(categories);

        this.mockMvc.perform(get("/admin/categories")
                .with(user(admin))
        )
                .andExpect(view().name("/admin/view_categories"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Electronics")));
    }

    @Test
    @WithMockUser("ADMIN")
    public void givenTheCreatePath_whenGetRequest_thenShouldReturnPageContainingTheCreateCategoryForm() throws Exception {

        this.mockMvc.perform(get("/admin/category/create")
                .with(user(admin))
        )
                .andExpect(view().name("admin/create_category"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Create Category")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenANonExistingCategory_whenPostRequest_thenShouldCreateCategoryAndRedirect()
            throws Exception {

        Category category = new Category();
        category.setName("Furniture");

        CategoryForm categoryForm = new CategoryForm();
        categoryForm.setName(category.getName());

        given(categoryService.createCategory(any())).willReturn(Optional.of(category));
        //TODO to find a method to add the form to the request
        this.mockMvc.perform(post("/admin/category").with(csrf())
                .with(user(admin))
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenExistingCategoryId_whenEdit_thenShouldRedirectToEditCategory() throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("test");

        when(categoryService.findById(any())).thenReturn(Optional.of(category));
        mockMvc.perform(get("/admin/category/edit/1")
                .with(user(admin))
        ).andExpect(status().isOk())
                .andExpect(view().name("admin/edit_category"))
                .andExpect(content().string(containsString("Edit")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void givenNonExistingCategoryId_whenEdit_thenShouldThrowAnExceptionAndRedirectToViewCategories()
            throws Exception {

        mockMvc.perform(get("/admin/category/edit/212")
                .with(user(admin))
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));
    }


    @Test
    @WithMockUser("ADMIN")
    public void givenACategory_whenUpdate_thenShouldUpdateCategoryAndRedirectToViewCategoriesPage()
            throws Exception {

        Category category = new Category();
        category.setId(1L);
        category.setName("Computers");

        CategoryForm categoryForm = new CategoryForm();
        categoryForm.setId(category.getId());
        categoryForm.setName(category.getName());
        //TODO to add form to the request
        when(categoryService.updateCategory(any())).thenReturn(Optional.of(category));
    }

    @Test
    @WithMockUser("ADMIN")
    public void givenAIdOfAExistingCategory_whenDeleteRequest_thenShouldDeleteCategoryAndRedirectToViewCategoriesPage()
            throws Exception {

        mockMvc.perform(get("/admin/category/delete/1").with(user(admin))
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/categories"));
        //TODO to add an assertion to check the message returned in the model.addFlashAttributes

    }


}
