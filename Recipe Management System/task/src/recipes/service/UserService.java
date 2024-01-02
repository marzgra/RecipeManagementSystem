package recipes.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.dto.RegisterDto;
import recipes.entity.User;
import recipes.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean register(RegisterDto registerDto) {
        if(userRepository.findByEmail(registerDto.getEmail()).isEmpty()) {
            var user = new User();
            user.setEmail(registerDto.getEmail());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
