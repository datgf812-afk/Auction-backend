package auction.backend.service;

import auction.backend.DTO.LoginRequestDTO;
import auction.backend.DTO.UserRequestDTO;
import auction.backend.DTO.UserResponseDTO;
import auction.backend.Mapper.UserMapper;
import auction.backend.Security.JwtUtil;
import auction.backend.entity.User;
import auction.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public User getUserByUserName(String userName) {
        return repository.findByUserName(userName).orElseGet(() -> {
            if (userName.startsWith("User_")) {
                User demoUser = new User();
                demoUser.setUserName(userName);
                demoUser.setEmail(userName + "@demo.com");
                demoUser.setPassword("demo123");
                demoUser.setRole("USER");
                demoUser.setCash(5000000000d);
                return repository.save(demoUser);
            }
            throw new RuntimeException("Không tìm thấy tài khoản: " + userName);
        });
    }

    public User registerUser(UserRequestDTO userRequestDTO){
        if(repository.existsByUserName(userRequestDTO.getUserName())
                || repository.existsByEmail(userRequestDTO.getEmail())){
            throw new RuntimeException("Tên đăng nhập hoặc email đã tồn tại!");
        }

        String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
        userRequestDTO.setPassword(encodedPassword);
        return repository.save(userMapper.toEntity(userRequestDTO));
    }

    public UserResponseDTO loginUser(LoginRequestDTO loginDTO) {
        User user = repository.findByUserName(loginDTO.getUserName())
                .orElseThrow(() -> new RuntimeException("Tên đăng nhập không tồn tại!"));
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu không chính xác!");
        }
        UserResponseDTO responseDTO = userMapper.toResponseDTo(user);
        responseDTO.setRole(user.getRole());
        String token = jwtUtil.generateToken(responseDTO);
        responseDTO.setToken(token);
        return responseDTO;
    }
}