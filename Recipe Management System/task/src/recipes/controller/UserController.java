package recipes.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import recipes.dto.RegisterDto;
import recipes.service.UserService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto registerDto) {
        return service.register(registerDto) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("User already exists");
    }
}
