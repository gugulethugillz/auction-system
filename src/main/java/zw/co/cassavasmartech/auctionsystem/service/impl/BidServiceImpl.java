package zw.co.cassavasmartech.auctionsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.auctionsystem.model.Bid;
import zw.co.cassavasmartech.auctionsystem.model.User;
import zw.co.cassavasmartech.auctionsystem.repository.BidRepository;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.BidService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final BidRepository bidRepository;

    

    @Override
    public Optional<Bid> createBid(Bid bid) {
        if(bid instanceof Bid) {
            Bid savedBid = bidRepository.save((Bid) bid);
            return Optional.ofNullable(savedBid);
        }
        log.error("Unknown bid type");
        return Optional.empty();
    }

    @Override
    public Optional<Bid> findById(Long id) {
        return bidRepository.findById(id);
    }


    @Override
    public List<Bid> findAllBids() {

        return  bidRepository.findAll();
    }

    @Override
    public List<Bid> findByAssetIdOrderByValueDesc(Long asset_id) {
        List<Bid> bids = bidRepository.findByAssetIdOrderByValueDesc(asset_id);
//        List<Bid> convertedBids = new ArrayList<>() {};
//        String formatDateTime = new String();
//        for (Bid bid:bids) {
//           LocalDateTime lastUpdated = bid.getLastUpdated().toLocalDateTime();
//            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//            formatDateTime = lastUpdated.format(format);
//
//
//        }
        return bids;
    }

    @Override
    public List<Bid> getActiveBids(Long userId) {
        return bidRepository.getActiveBids(userId, OffsetDateTime.now());
    }

    @Override
    public List<Bid> findByUser(Long userId) {
        return bidRepository.findByUser(userId);
    }

    @Override
    public List<Bid> findAllByUser(User user) {
        return bidRepository.findAllByUser(user);
    }

    @Override
    public List<Bid> findAll() {
        return null;
    }

    @Override
    public BidRepository getRepository() {
        return bidRepository;
    }


}
