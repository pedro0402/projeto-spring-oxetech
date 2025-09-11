package al.oxetech.projeto_final.controller;

import al.oxetech.projeto_final.dto.usuario.UsuarioDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioInputDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioPatchDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioUpdateDTO;
import al.oxetech.projeto_final.exception.SenhaInvalidaException;
import al.oxetech.projeto_final.service.PasswordResetService;
import al.oxetech.projeto_final.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final PasswordResetService resetService;

    /**
     * Endpoint que cria um novo usuário a partir dos dados recebidos no corpo
     * da requisição.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'COLABORADOR')")
    public ResponseEntity<UsuarioDTO> criar(@RequestBody @Valid UsuarioInputDTO dto) {
        UsuarioDTO novoUsuario = usuarioService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    /**
     * Retorna todos os usuários cadastrados.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('COLABORADOR', 'GERENTE', 'ADMIN')")
    public List<UsuarioDTO> listarTodos() {
        return usuarioService.listarTodos();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioUpdateDTO dto) {
        UsuarioDTO usuarioAtualizado = usuarioService.atualizar(id, dto);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarParcial(@PathVariable Long id, @RequestBody @Valid UsuarioPatchDTO dto) {
        return ResponseEntity.ok(usuarioService.atualizarParcial(id, dto));
    }

    @PostMapping("/solicitar-codigo")
    public ResponseEntity<Void> solicitarCodigo(@RequestParam String email) {
        resetService.gerarTokenEEnviarEmail(email);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> redefinirSenha(
            @PathVariable Long id,
            @RequestParam String codigo,
            @RequestParam String novaSenha) {
        try {
            resetService.redefinirSenha(codigo, novaSenha);
            return ResponseEntity.ok().build();
        } catch (SenhaInvalidaException exception){
            return ResponseEntity.badRequest().build();
        }

    }
}
