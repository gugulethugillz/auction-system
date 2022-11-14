package zw.co.cassavasmartech.auctionsystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import zw.co.cassavasmartech.auctionsystem.model.Asset;
import zw.co.cassavasmartech.auctionsystem.model.Person;
import zw.co.cassavasmartech.auctionsystem.model.Winner;
import zw.co.cassavasmartech.auctionsystem.repository.AssetRepository;
import zw.co.cassavasmartech.auctionsystem.repository.WinnerRepository;
import zw.co.cassavasmartech.auctionsystem.service.impl.AssetServiceImpl;
import zw.co.cassavasmartech.auctionsystem.service.impl.WinnerServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class WinnerServiceTest {

    @InjectMocks
    private WinnerServiceImpl winnerService;

    @Mock
    private WinnerRepository winnerRepository;

    private Winner winner;

    @BeforeEach
    public void init() {

        winner = new Winner();
        winner.setId(1L);
        winner.setName("Takunda");
    }

    @AfterEach
    public void cleanUp() {
        winner = null;
    }

    @Test
    public void givenAnId_whenFindWinner_shouldReturnWinner() throws Exception {
        when(winnerRepository.findById(any())).thenReturn(Optional.of(winner));

        Optional<Winner> winnerOptional = winnerService.findById(1L);
        assertTrue(winnerOptional.isPresent(), "Winner Id not found");
        Winner winner = winnerOptional.get();
        assertEquals(1L, winner.getId());
    }

    @Test
    public void givenExistingWinners_whenFindByUsername_thenShouldReturnNonEmptyList() throws Exception {
        when(winnerRepository.getByUsername(any())).thenReturn(Collections.singletonList(winner));

        List<Winner> winners = winnerService.findByUsername("alfred.samanga@gmail.com");
        assertThat(winners).isNotNull();
        assertTrue(winners.size() > 0, "No winners found by that username");
    }


    @Test
    public void givenWinnerRecords_whenFindAll_thenShouldReturnNonEmptyList() throws Exception {
        when(winnerRepository.findAll()).thenReturn(List.of(winner));

        List<Winner> winnerList = winnerService.findAllWinners();
        assertAll(
                () -> assertThat(winnerList).isNotNull(),
                () -> assertTrue(winnerList.size() > 0),
                () -> assertThat(winnerList.get(0).getId()).isEqualTo(winner.getId())
        );
    }

    @Test
    public void givenNoWinnerRecords_whenFindAll_thenShouldReturnEmptyList() throws Exception {
        when(winnerRepository.findAll()).thenReturn(Collections.emptyList());

        List<Winner> winnerList = winnerService.findAllWinners();
        assertAll(
                () -> assertThat(winnerList).isNotNull(),
                () -> assertTrue(winnerList.size() == 0)
        );
    }

}
