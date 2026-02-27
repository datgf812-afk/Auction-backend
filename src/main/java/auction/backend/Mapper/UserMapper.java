package auction.backend.Mapper;

import auction.backend.DTO.UserRequestDTO;
import auction.backend.DTO.UserResponseDTO;
import auction.backend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toResponseDTo(User user);
    User toEntity(UserRequestDTO requestDTO);
}
