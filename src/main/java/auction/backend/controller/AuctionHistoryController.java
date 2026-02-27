package auction.backend.controller;

import auction.backend.entity.AuctionHistory;
import auction.backend.repository.AuctionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class AuctionHistoryController {

    private final AuctionHistoryRepository historyRepository;

    @GetMapping("/{userName}")
    public List<AuctionHistory> getUserHistory(@PathVariable String userName) {
        return historyRepository.findByUserNameOrderByWinTimeDesc(userName);
    }
}