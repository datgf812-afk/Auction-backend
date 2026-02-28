package auction.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "auction_history")
public class AuctionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long auctionItemId;
    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String itemName;

    private String imageUrl;

    @Column(nullable = false)
    private Double winPrice;

    @Column(nullable = false)
    private LocalDateTime winTime;
}