package auction.backend.repository;

import auction.backend.entity.AuctionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionHistoryRepository extends JpaRepository<AuctionHistory, Long> {
    List<AuctionHistory> findByUserNameOrderByWinTimeDesc(String userName);
    boolean existsByAuctionItemId(Long auctionItemId);
}