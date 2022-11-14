package zw.co.cassavasmartech.auctionsystem.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zw.co.cassavasmartech.auctionsystem.model.Category;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Tinashe & Calvin
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CategoryRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void init() {

    }

    @Test
    public void givenACategory_whenPersistCategory_thenShouldReturnPersistedCategory() throws Exception {
        Category category = new Category();
        Category newCategory = entityManager.persist(category);
        assertThat(newCategory).isNotNull();
    }
}
