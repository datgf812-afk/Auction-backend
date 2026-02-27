package auction.backend.controller;

import auction.backend.DTO.LoginRequestDTO;
import auction.backend.DTO.UserRequestDTO;
import auction.backend.DTO.UserResponseDTO;
import auction.backend.Mapper.UserMapper;
import auction.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public UserResponseDTO login(@RequestBody LoginRequestDTO loginDTO) {
        return userService.loginUser(loginDTO);
    }

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody UserRequestDTO userRequestDTO) {
        return userMapper.toResponseDTo(userService.registerUser(userRequestDTO));
    }
    @GetMapping("/users/{userName}")
    public UserResponseDTO getUserInfo(@PathVariable String userName) {
        return userMapper.toResponseDTo(userService.getUserByUserName(userName));
    }
}