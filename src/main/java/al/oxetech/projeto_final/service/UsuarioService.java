package al.oxetech.projeto_final.service;

import al.oxetech.projeto_final.dto.usuario.UsuarioDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioInputDTO;
import al.oxetech.projeto_final.model.Usuario;
import al.oxetech.projeto_final.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;

/**
 * Camada de serviço responsável por aplicar as regras de negócio relacionadas
 * a {@link Usuario}. É aqui que decidimos como a aplicação deverá agir ao
 * receber uma requisição antes de acessar o banco de dados.
 */
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository; // Interface de acesso aos dados
    private final PasswordEncoder passwordEncoder;     // Responsável pela criptografia das senhas

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo usuário. A senha é codificada com um algoritmo de hash antes
     * de ser salva, garantindo que não seja armazenada em texto puro.
     */
    public UsuarioDTO salvar(UsuarioInputDTO dto){
        Usuario u = new Usuario();
        u.setNome(dto.getNome());
        u.setEmail(dto.getEmail());
        // passwordEncoder.encode gera o hash da senha fornecida
        u.setSenha(passwordEncoder.encode(dto.getSenha()));
        u.setRole(dto.getRole());

        return new UsuarioDTO(usuarioRepository.save(u));
    }

    /**
     * Lista todos os usuários cadastrados no sistema.
     */
    public List<UsuarioDTO> listarTodos(){
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDTO::new)
                .toList();
    }

    public UsuarioDTO buscarPorId(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        return new UsuarioDTO(u);
    }
}
