package zw.co.cassavasmartech.auctionsystem.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.cassavasmartech.auctionsystem.forms.CategoryForm;
import zw.co.cassavasmartech.auctionsystem.model.Category;
import zw.co.cassavasmartech.auctionsystem.model.Person;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.CategoryService;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Created by Tinashe & Calvin
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class CategoryController {

    private final CategoryService categoryService;

    // display a list of categories
    @GetMapping("/categories")
    public String viewCategories(@AuthenticationPrincipal Person person, Model model) {
        model.addAttribute("authUser", person);
        model.addAttribute("listCategories", categoryService.findAll());
        return "/admin/view_categories";
    }

    //Create Get Returns the form to create the category
    @GetMapping("/category/create")
    public String createCategory(@AuthenticationPrincipal Person person, Model model, RedirectAttributes redirectAttributes) {

        CategoryForm categoryForm = new CategoryForm();
        model.addAttribute("authUser", person);
        model.addAttribute("categoryForm", categoryForm);
        return "admin/create_category";

    }

    //Create Category
    @PostMapping("/category")
    public String createCategory(@AuthenticationPrincipal Person person, @Valid CategoryForm categoryForm,
                                 BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.error("Errors in form: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("Errors", bindingResult.getAllErrors());
            model.addAttribute("categoryForm", categoryForm);
            return "redirect:/admin/categories";
        }

        final Category category = new Category();
        category.setName(categoryForm.getName());
        if (categoryService.findByName(category.getName()).isPresent()) {
            log.error("Category already exists");
            redirectAttributes.addFlashAttribute("error", "Could not create category, category already exists");
            return "redirect:/admin/categories";
        }
        category.setCreatedBy(person.getUsername());
        category.setLastUpdatedBy(person.getUsername());

        final Optional<Category> categoryOptional = categoryService.createCategory(category);
        if (categoryOptional.isPresent()) {
            Category createdCategory = categoryOptional.get();
            log.info("Successfully Created Category " + createdCategory.getName());
            redirectAttributes.addFlashAttribute("info", "Created successfully");
        } else {
            redirectAttributes.addFlashAttribute("errors", "Could not create category, try again later");
        }

        return "redirect:/admin/categories";

    }

    //this method is done in the asset view by assets category
    /*//categories/id
    @GetMapping("/{id}")
    public String viewCategoryById(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {

        try {
            Category category = categoryService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found for ID: " + id));
            model.addAttribute("category", category);
            return "view_category";
        } catch (Exception ex) {
            log.error("Category not found with ID:" + id);
            redirectAttributes.addFlashAttribute("Error", "Category not found");
            return "redirect:/admin/categories";
        }
    }*/

    //Edit Get
    @GetMapping("/category/edit/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal Person person) {
        model.addAttribute("authUser", person);
        if (!categoryService.findById(id).isPresent()) {
            log.error("Category does not exist with ID " + id);
            redirectAttributes.addFlashAttribute("Error", "Category does not exist with ID " + id);
            return "redirect:/admin/categories";
        } else {
            try {
                Category category = categoryService.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
                CategoryForm categoryForm = new CategoryForm();
                categoryForm.setId(category.getId());
                categoryForm.setName(category.getName());
                model.addAttribute("categoryForm", categoryForm);

                return "admin/edit_category";
            } catch (IllegalArgumentException ex) {
                log.error("Invalid Category Id " + id);
                redirectAttributes.addFlashAttribute("Error", "Invalid Category");
                return "redirect:/admin/categories";
            }
        }


    }

    //update category
    @PostMapping("/category/{id}")
    public String updateCategory(@AuthenticationPrincipal Person person, @PathVariable("id") Long id,
                                 @Valid CategoryForm categoryForm, BindingResult bindingResult, Model model,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("Errors", bindingResult.getAllErrors());
            log.error("Errors in form: {}", bindingResult.getAllErrors());
            model.addAttribute("categoryForm", categoryForm);
            return "/admin/edit_category";
        }

        try {
            Category categoryToUpdate = categoryService.findById(id).orElseThrow(() -> new Exception("Category not Exist"));
            categoryToUpdate.setName(categoryForm.getName());
            categoryToUpdate.setLastUpdatedBy(person.getUsername());
            final Optional<Category> categoryOptional = categoryService.updateCategory(categoryToUpdate);
            if (categoryOptional.isPresent()) {
                Category updatedCategory = categoryOptional.get();
                redirectAttributes.addFlashAttribute("info", "Successful Update");
                log.info("Updated successfully of category  " + categoryToUpdate.getName());
            } else {
                redirectAttributes.addFlashAttribute("errors", "Could not update category, try again later");
                log.error("Could not Update category Category");
            }
            return "redirect:/admin/categories";
        } catch (Exception ex) {
            log.error("Category does not Exist with ID: " + id);
            redirectAttributes.addFlashAttribute("error", "Category Does not Exist");
            return "redirect:/admin/categories";
        }

    }

    //Delete Post
    @GetMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, RedirectAttributes redirectAttributes,
                                 @AuthenticationPrincipal Person person) {


        if (categoryService.findById(id).isPresent()) {
            try {
                categoryService.deleteCategoryById(id);
                redirectAttributes.addFlashAttribute("info", "Successful Delete of Category");
                log.info("Deleted Category ");
                return "redirect:/admin/categories";
            } catch (Exception ex) {
                redirectAttributes.addFlashAttribute("info", "Cannot delete a category with assets");
                log.error("Cannot delete a category with assets");
                return "redirect:/admin/categories";
            }
        } else {
            log.error("Category does not exist with ID :" + id);
            redirectAttributes.addFlashAttribute("Error", "Category does not exist");
            return "redirect:/admin/categories";
        }


    }

}
