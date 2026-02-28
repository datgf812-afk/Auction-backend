package auction.backend.service;

import auction.backend.entity.AuctionHistory;
import auction.backend.entity.AuctionItem;
import auction.backend.entity.Bids;
import auction.backend.repository.AuctionHistoryRepository;
import auction.backend.repository.AuctionItemRepository;
import auction.backend.repository.BidsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionItemService {
    private final AuctionItemRepository repository;
    private final BidsRepository bidsRepository;
    private final AuctionHistoryRepository auctionHistoryRepository;

    @Transactional
    public void saveWinner(Long auctionItemId) {
        if (auctionHistoryRepository.existsByAuctionItemId(auctionItemId)) {
            return;
        }
        AuctionItem item = repository.findById(auctionItemId).orElseThrow();
        Bids topBid = bidsRepository.findFirstByAuctionItemIdOrderByAmountDesc(auctionItemId);

        if (topBid != null) {
            AuctionHistory history = new AuctionHistory();
            history.setAuctionItemId(auctionItemId);
            history.setUserName(topBid.getBidderName());
            history.setItemName(item.getName());
            history.setImageUrl(item.getImageUrl());
            history.setWinPrice(topBid.getAmount());
            history.setWinTime(LocalDateTime.now());
            auctionHistoryRepository.save(history);
        }
    }
    public List<AuctionItem> getAllAuctionItems() {
        return repository.findAll();
    }
    public AuctionItem getItemById(Long id){
        AuctionItem auctionItem = repository.findById(id).orElseThrow(()->new RuntimeException("Không tìm thấy vật phẩm!"));
        return auctionItem;
    }
    public AuctionItem addAuctionItem(AuctionItem newAuctionItem){
        return repository.save(newAuctionItem);
    }
    public AuctionItem updateAuctionitem(Long id, AuctionItem newAuctionItem) {
        AuctionItem auctionItem = getItemById(id);
        auctionItem.setCurrentPrice(newAuctionItem.getCurrentPrice());
        auctionItem.setName(newAuctionItem.getName());
        auctionItem.setImageUrl(newAuctionItem.getImageUrl());
        auctionItem.setDescription(newAuctionItem.getDescription());
        auctionItem.setStartTime(newAuctionItem.getStartTime());
        auctionItem.setEndTime(newAuctionItem.getEndTime());
        return repository.save(auctionItem);
    }
    public void deleteAuctionItem(Long id){
        repository.deleteById(id);
    }
}
