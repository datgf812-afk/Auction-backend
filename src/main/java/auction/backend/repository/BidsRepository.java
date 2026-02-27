package auction.backend.repository;

import auction.backend.entity.AuctionItem;
import auction.backend.entity.Bids;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BidsRepository extends JpaRepository<Bids, Long> {
    List<Bids> findBidsByAuctionItemIdOrderByTimestampDesc(Long auctionItemId);
    Bids findFirstByAuctionItemIdOrderByAmountDesc(Long id);
    void deleteByAuctionItem(AuctionItem auctionItem);
}
