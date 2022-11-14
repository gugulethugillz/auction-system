package zw.co.cassavasmartech.auctionsystem.service.ifaces;

import zw.co.cassavasmartech.auctionsystem.model.Category;

import java.util.List;
import java.util.Optional;

/**
 * Created by Tinashe & Calvin
 */
public interface CategoryService {
    Optional<Category> findById(Long id);

    Optional<Category> findByName(String name);

    Optional<Category> createCategory(Category category);

    List<Category> findAll();

    Optional<Category> updateCategory(Category category);

    void deleteCategoryById(Long id);


}
