package auction.backend.DTO;

import lombok.Data;

@Data
public class BidRequestDTO {
    private String bidderName;
    private Double amount;
    private Long auctionItemId;
}
