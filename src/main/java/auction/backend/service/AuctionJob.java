package auction.backend.service;

import auction.backend.entity.AuctionHistory;
import auction.backend.entity.AuctionItem;
import auction.backend.entity.Bids;
import auction.backend.repository.AuctionHistoryRepository;
import auction.backend.repository.AuctionItemRepository;
import auction.backend.repository.BidsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuctionJob {

    private final AuctionItemRepository auctionItemRepository;
    private final BidsRepository bidsRepository;
    private final AuctionHistoryRepository historyRepository;

    @Scheduled(fixedRate = 2000)
    @Transactional
    public void auctionTask() {
        List<AuctionItem> items = auctionItemRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (AuctionItem item : items) {
            if (item.getEndTime() != null && item.getEndTime().isBefore(now)) {

                Bids topBid = bidsRepository.findFirstByAuctionItemIdOrderByAmountDesc(item.getId());

                if (topBid != null) {
                    AuctionHistory history = new AuctionHistory();
                    history.setAuctionItemId(item.getId());
                    history.setUserName(topBid.getBidderName());
                    history.setItemName(item.getName());
                    history.setImageUrl(item.getImageUrl());
                    history.setWinPrice(topBid.getAmount());
                    history.setWinTime(now);
                    historyRepository.save(history);
                    bidsRepository.deleteByAuctionItem(item);
                }
            }
        }
        boolean reset = true;
        for (AuctionItem item : items) {
            if (item.getEndTime() != null && item.getEndTime().isAfter(now)) {
                reset = false;
                break;
            }
        }

        if (reset) {
            for (AuctionItem item : items) {
                item.setCurrentPrice(Math.floor(50000 + (Math.random() * 200000)));
                if ("start".equals(item.getState())) {
                    item.setStartTime(now);
                    item.setEndTime(now.plusSeconds(60));
                } else if ("end".equals(item.getState())) {
                    item.setStartTime(now.minusSeconds(40));
                    item.setEndTime(now.minusSeconds(5));
                } else {
                    item.setStartTime(now.plusSeconds(50));
                    item.setEndTime(now.plusSeconds(90));
                }
                auctionItemRepository.save(item);
            }
        }
    }
}