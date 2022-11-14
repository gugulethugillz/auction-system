package zw.co.cassavasmartech.auctionsystem.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zw.co.cassavasmartech.auctionsystem.model.Winner;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class WinnerRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WinnerRepository winnerRepository;

    @BeforeEach
    public void init() {

    }

    @Test
    public void givenAWinner_whenPersistWinner_thenShouldReturnPersistedWinner() throws Exception {
        Winner winner = new Winner();
        Winner newWinner= entityManager.persist(winner);
        assertThat(newWinner).isNotNull();
    }

}
