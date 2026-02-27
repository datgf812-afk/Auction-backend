package auction.backend.controller;

import auction.backend.DTO.BidRequestDTO;
import auction.backend.DTO.BidResponseDTO;
import auction.backend.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping
    public BidResponseDTO placeBid(@RequestBody BidRequestDTO requestDTO) {
        return bidService.placeBid(requestDTO);
    }

    @GetMapping("/{itemId}")
    public List<BidResponseDTO> getBidHistory(@PathVariable Long itemId) {
        return bidService.getAllBids(itemId);
    }
}