package auction.backend.Security;

import auction.backend.DTO.UserResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
@Component
public class JwtUtil {
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("Cai-Nay-La-Bi-Mat-Quoc-Gia-Khong-Duoc-Tiet-Lo-Nhe".getBytes());
    private static final long EXPIRATION_TIME = 86400000;

    public String generateToken(UserResponseDTO dto) {
        return Jwts.builder()
                .setSubject(dto.getUserName())
                .claim("role", dto.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build()
                .parseClaimsJws(token).getBody();
    }
}