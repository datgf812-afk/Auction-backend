package auction.backend.service;

import auction.backend.DTO.BidRequestDTO;
import auction.backend.DTO.BidResponseDTO;
import auction.backend.Mapper.Bidmapper;
import auction.backend.Security.JwtUtil;
import auction.backend.entity.AuctionItem;
import auction.backend.entity.Bids;
import auction.backend.entity.User;
import auction.backend.repository.AuctionItemRepository;
import auction.backend.repository.BidsRepository;
import auction.backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidsRepository bidsRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final UserRepository userRepository;
    private final Bidmapper bidmapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtUtil jwtUtil;

    @Transactional
    public BidResponseDTO placeBid(BidRequestDTO requestDTO, String token){
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        String validUsername;
        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            validUsername = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Token không hợp lệ!");
        }

        AuctionItem auctionItem = auctionItemRepository.findById(requestDTO.getAuctionItemId())
                .orElseThrow(()-> new RuntimeException("Không tìm thấy vật phẩm"));

        if((requestDTO.getAmount()-auctionItem.getCurrentPrice())< 50000){
            throw new RuntimeException("Giá mới buộc lớn hơn giá cũ ít nhất 50.000 vnđ");
        }

        User user = userRepository.findByUserName(validUsername)
                .orElseThrow(()-> new RuntimeException("Không thấy tài khoản đặt giá"));

        if (user.getCash() < requestDTO.getAmount()) {
            throw new RuntimeException("Số dư không đủ!");
        }

        Bids oldTopBid = bidsRepository.findFirstByAuctionItemIdOrderByAmountDesc(requestDTO.getAuctionItemId());
        if(oldTopBid != null){
            User oldUser = userRepository.findByUserName(oldTopBid.getBidderName())
                    .orElse(null);
            if(oldUser != null){
                oldUser.setCash(oldUser.getCash()+ oldTopBid.getAmount());
                userRepository.save(oldUser);
            }
        }

        user.setCash(user.getCash()-requestDTO.getAmount());
        userRepository.save(user);

        auctionItem.setCurrentPrice(requestDTO.getAmount());
        auctionItemRepository.save(auctionItem);

        Bids bid = bidmapper.toEntity(requestDTO);
        bid.setAuctionItem(auctionItem);
        bid.setBidderName(validUsername);
        bid.setTimestamp(LocalDateTime.now());
        bidsRepository.save(bid);

        BidResponseDTO bidResponseDTO = bidmapper.toResponseDTO(bid);
        messagingTemplate.convertAndSend("/topic/bids/" + requestDTO.getAuctionItemId(), bidResponseDTO);

        return bidResponseDTO;
    }

    public List<BidResponseDTO> getAllBids(Long itemId){
        return bidsRepository.findBidsByAuctionItemIdOrderByTimestampDesc(itemId).stream().map(bidmapper::toResponseDTO).toList();
    }
}