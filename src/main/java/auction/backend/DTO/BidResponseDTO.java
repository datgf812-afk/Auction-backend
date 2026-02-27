package auction.backend.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class BidResponseDTO {
    private String bidderName;
    private Double amount;
    private Long id;
    private LocalDateTime timestamp;
}
