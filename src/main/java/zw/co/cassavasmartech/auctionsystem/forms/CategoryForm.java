package zw.co.cassavasmartech.auctionsystem.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

/**
 * Created by Tinashe & Calvin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class CategoryForm {

    @NotBlank(message = "Category name is required")
    private String name;

    private Long id;

    /*public Category getCategory(Long id) {
        Category category = new Category();
        category.setId(id);
        category.setId(id);
        return category;
    }

    public static CategoryForm createCategoryForm(Category category) {
        if (category == null) return CategoryForm.builder().build();
        CategoryForm categoryForm = CategoryForm.builder()
                .name(category.getName())
                .build();
        return categoryForm;
    }

    private Category fillInDetails(Category category) {
        category.setId(id);
        category.setName(name);

        return category;
    }*/

}
