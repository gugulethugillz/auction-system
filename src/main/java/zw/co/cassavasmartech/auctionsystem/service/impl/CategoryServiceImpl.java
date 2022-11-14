package zw.co.cassavasmartech.auctionsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.auctionsystem.model.Category;
import zw.co.cassavasmartech.auctionsystem.repository.CategoryRepository;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.CategoryService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    //private final UserRepository userRepository;

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Optional<Category> createCategory(Category category) {
        return Optional.of(categoryRepository.save(category));
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> updateCategory(Category category) {
        return Optional.of(categoryRepository.save(category));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

}
