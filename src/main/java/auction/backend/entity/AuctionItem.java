package auction.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name ="auction_items")
public class AuctionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String imageUrl;
    private Double currentPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String state;

    @OneToMany(mappedBy = "auctionItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Bids> bids;
}
