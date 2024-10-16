package com.gugulethugillz.auctionsystem.bid.service;


import com.gugulethugillz.auctionsystem.bid.model.Bid;
import com.gugulethugillz.auctionsystem.person.model.User;
import com.gugulethugillz.auctionsystem.bid.repository.BidRepository;

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
