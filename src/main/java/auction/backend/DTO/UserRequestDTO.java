package auction.backend.DTO;

import jakarta.validation.constraints.Email;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UserRequestDTO {
    @NotBlank(message = "Không được để trống tên")
    private String userName;
    @NotBlank(message = "Không được để trống email")
    @Email(message = "Email không đúng định dạng")
    private String email;
    @NotBlank(message = "Không được để trống mật khẩu")
    private String password;
}
