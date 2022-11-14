package zw.co.cassavasmartech.auctionsystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zw.co.cassavasmartech.auctionsystem.model.Category;
import zw.co.cassavasmartech.auctionsystem.repository.CategoryRepository;
import zw.co.cassavasmartech.auctionsystem.service.impl.CategoryServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    //@Mock
    //private UserRepository userRepository;

    private Category category;
    //private User user;

    @BeforeEach
    public void init() {
        category = new Category();
        category.setId(1L);
        category.setName("Furniture");

        /*user = new User();
        user.setId(2L);
        user.setUsername("alfred@gmail.com");*/
    }

    @AfterEach
    public void cleanUp() {
        category = null;
    }

    @Test
    public void givenAnId_whenFindCategory_shouldReturnCategory() throws Exception {
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        Optional<Category> categoryOptional = categoryService.findById(1L);
        assertTrue(categoryOptional.isPresent(), "Category not found");
        Category category = categoryOptional.get();
        assertEquals(1L, category.getId());
    }

    @Test
    public void givenAnExistingCategory_whenFindByName_thenShouldReturnTheCategory() throws Exception {
        when(categoryRepository.findByName(any())).thenReturn(Optional.of(category));

        Optional<Category> categoryOptional = categoryService.findByName("Furniture");
        assertTrue(categoryOptional.isPresent(), "Category not found");
        Category category = categoryOptional.get();
        assertEquals(1L, category.getId());
    }

    @Test
    public void givenANonExistingCategory_whenFindByName_thenShouldReturnEmptyOptional() throws Exception {
        when(categoryRepository.findByName(any())).thenReturn(Optional.of(category));

        Optional<Category> categoryOptional = categoryService.findByName("Furniture");
        assertTrue(categoryOptional.isPresent(), "Category not found");
        Category category = categoryOptional.get();
        assertEquals(1L, category.getId());
    }

    @Test
    public void givenUnsavedCategory_whenCreateCategory_thenShouldReturnCreatedCategory() throws Exception {
        when(categoryRepository.save(any())).thenReturn(category);

        Optional<Category> categoryOptional = categoryService.createCategory(category);
        assertAll(
                () -> assertThat(categoryOptional).isNotNull(),
                () -> assertTrue(categoryOptional.isPresent()),
                () -> assertThat(categoryOptional.get()).isNotNull(),
                () -> assertEquals(category.getName(), categoryOptional.get().getName())
        );
    }

    @Test
    public void givenSavedCategory_whenCreateCategory_thenShouldThrowExceptionThatCategoryAlreadyExists()
            throws Exception {
        when(categoryRepository.save(any())).thenThrow(new RuntimeException("Category already exists."));

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            categoryService.createCategory(category);
        });
        assertEquals("Category already exists.", runtimeException.getMessage());
    }

    @Test
    public void givenCategoryRecords_whenFindAll_thenShouldReturnNonEmptyList() throws Exception {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<Category> categoryList = categoryService.findAll();
        assertAll(
                () -> assertThat(categoryList).isNotNull(),
                () -> assertTrue(categoryList.size() > 0),
                () -> assertThat(categoryList.get(0).getName()).isEqualTo(category.getName())
        );
    }

    @Test
    public void givenNoCategoryRecords_whenFindAll_thenShouldReturnEmptyList() throws Exception {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<Category> categoryList = categoryService.findAll();
        assertAll(
                () -> assertThat(categoryList).isNotNull(),
                () -> assertTrue(categoryList.size() == 0)
        );
    }

    @Test
    public void givenExistingCategory_whenEdit_thenShouldReturnUpdatedCategory() throws Exception {
        //
        when(categoryRepository.save(any())).thenReturn(category);
        category.setName("Furnitures");
        Optional<Category> categoryOptional = categoryService.updateCategory(category);
        assertAll(
                () -> assertThat(categoryOptional).isNotNull(),
                () -> assertTrue(categoryOptional.isPresent()),
                () -> assertThat(categoryOptional.get()).isNotNull(),
                () -> assertEquals(category.getName(), categoryOptional.get().getName())
        );
    }

    @Test
    public void givenANonExistingCategory_whenEdit_thenShouldThrowExceptionCategoryNotFound() throws Exception {
        when(categoryRepository.save(any())).thenThrow(new RuntimeException("Category does not exists."));

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            categoryService.updateCategory(category);
        });
        assertEquals("Category does not exists.", runtimeException.getMessage());
    }


    @Test
    public void givenANonExistingCategory_whenDelete_thenShouldThrowCategoryDoesNotExistException() throws Exception {

    }


}
