package al.oxetech.projeto_final.controller;

import al.oxetech.projeto_final.dto.usuario.UsuarioDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioInputDTO;
import al.oxetech.projeto_final.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelas operações relacionadas a {@link al.oxetech.projeto_final.model.Usuario}.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
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
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioDTO> listarTodos(){
        return usuarioService.listarTodos();
    }
}
