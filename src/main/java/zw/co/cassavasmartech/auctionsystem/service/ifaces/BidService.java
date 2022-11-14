package zw.co.cassavasmartech.auctionsystem.service.ifaces;

import zw.co.cassavasmartech.auctionsystem.model.Bid;
import zw.co.cassavasmartech.auctionsystem.model.User;
import zw.co.cassavasmartech.auctionsystem.repository.BidRepository;

import java.util.List;
import java.util.Optional;

public interface BidService {

    Optional<Bid> createBid(Bid bid);
    Optional<Bid> findById(Long id);
    List<Bid> findAllBids();
    List<Bid> findByAssetIdOrderByValueDesc(Long asset_id);
    List<Bid> getActiveBids(Long userId);
    List<Bid> findByUser(Long userId);
    List<Bid> findAllByUser(User user);
    List<Bid> findAll();

    BidRepository getRepository();
}
