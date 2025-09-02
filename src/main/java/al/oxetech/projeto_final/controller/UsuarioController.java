package al.oxetech.projeto_final.controller;

import al.oxetech.projeto_final.dto.usuario.UsuarioDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioInputDTO;
import al.oxetech.projeto_final.service.JwtService;
import al.oxetech.projeto_final.service.UsuarioService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelas operações relacionadas a {@link al.oxetech.projeto_final.model.Usuario}.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public UsuarioController(UsuarioService usuarioService,JwtService jwtService){
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    /**
     * Endpoint que cria um novo usuário a partir dos dados recebidos no corpo
     * da requisição.
     */
    @PostMapping
    public ResponseEntity<UsuarioDTO> criar(@RequestBody @Valid UsuarioInputDTO dto){
        UsuarioDTO novoUsuario = usuarioService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    /**
     * Retorna todos os usuários cadastrados.
     */
    @GetMapping
    public List<UsuarioDTO> listarTodos(){
        return usuarioService.listarTodos();
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioDTO> dadosDoUsuarioLogado(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ","");
        Claims claims = jwtService.validarToken(token);
        Long id = Long.parseLong(claims.getSubject());

        UsuarioDTO usuarioDTO = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuarioDTO);
    }
}
