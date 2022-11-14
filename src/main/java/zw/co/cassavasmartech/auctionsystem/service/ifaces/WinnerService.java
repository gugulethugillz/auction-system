package zw.co.cassavasmartech.auctionsystem.service.ifaces;

import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.auctionsystem.model.Asset;
import zw.co.cassavasmartech.auctionsystem.model.Winner;
import zw.co.cassavasmartech.auctionsystem.repository.BidRepository;
import zw.co.cassavasmartech.auctionsystem.repository.WinnerRepository;

import java.util.List;
import java.util.Optional;




public interface WinnerService {
    WinnerRepository getRepository();
    Optional<Winner> findById(Long id);
    List<Winner> findByUsername(String username);
    List<Winner> findAllWinners();
}
