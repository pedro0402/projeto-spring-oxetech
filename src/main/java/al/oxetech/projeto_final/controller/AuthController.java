package al.oxetech.projeto_final.controller;

import al.oxetech.projeto_final.dto.login.LoginDTO;
import al.oxetech.projeto_final.dto.login.TokenResponseDTO;
import al.oxetech.projeto_final.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginDTO dto){
        String token = authService.autenticarEgerarToken(dto);
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}
