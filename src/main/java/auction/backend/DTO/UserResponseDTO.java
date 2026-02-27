package auction.backend.DTO;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String userName;
    private Double cash;
    private String token;
    private String role;
}
