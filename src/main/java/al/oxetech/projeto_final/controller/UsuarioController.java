package al.oxetech.projeto_final.controller;

import al.oxetech.projeto_final.dto.usuario.UsuarioDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioInputDTO;
import al.oxetech.projeto_final.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> criar(@RequestBody @Valid UsuarioInputDTO dto){
        UsuarioDTO novoUsuario = usuarioService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @GetMapping
    public List<UsuarioDTO> listarTodos(){
        return usuarioService.listarTodos();
    }
}
