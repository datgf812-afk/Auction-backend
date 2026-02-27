package auction.backend.controller;

import auction.backend.Security.JwtUtil;
import auction.backend.entity.AuctionItem;
import auction.backend.service.AuctionItemService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionItemController {
    private final AuctionItemService service;
    private final JwtUtil jwtUtil;
    private void validateAdmin(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        token = token.trim();
        try {
        Claims claims = jwtUtil.extractAllClaims(token);
        String role = claims.get("role", String.class);
        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("Không có quền Admin");
        }
        } catch (Exception e) {
            throw new RuntimeException("Token không hợp lệ!");
        }
    }
    @GetMapping
    public List<AuctionItem> getAll(){
        return service.getAllAuctionItems();
    }

    @GetMapping("/{id}")
    public AuctionItem getById(@PathVariable Long id){
        return service.getItemById(id);
    }
    @PostMapping("/add")
    public AuctionItem addItem(@RequestBody AuctionItem newAuctionItem,
                               @RequestHeader("Authorization") String token) {
        validateAdmin(token);
        return service.addAuctionItem(newAuctionItem);
    }

    @PutMapping("/update/{id}")
    public AuctionItem updateItem(@PathVariable Long id,
                                  @RequestBody AuctionItem newAuctionItem,
                                  @RequestHeader("Authorization") String token) {
        validateAdmin(token);
        return service.updateAuctionitem(id, newAuctionItem);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id,
                             @RequestHeader("Authorization") String token) {
        validateAdmin(token);
        service.deleteAuctionItem(id);
        return "Đã xóa vật phẩm id là: " + id;
    }
    @PostMapping("/save/{id}")
    public void saveWinner(@PathVariable Long id) {
        service.saveWinner(id);
    }
}
