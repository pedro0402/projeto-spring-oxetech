package al.oxetech.projeto_final.service;

import al.oxetech.projeto_final.dto.usuario.UsuarioDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioInputDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioPatchDTO;
import al.oxetech.projeto_final.exception.UsuarioAtualizacaoException;
import al.oxetech.projeto_final.exception.UsuarioNaoEncontradoException;
import al.oxetech.projeto_final.helper.NullAwareBeanUtilsBean;
import al.oxetech.projeto_final.mapper.UsuarioMapper;
import al.oxetech.projeto_final.model.Usuario;
import al.oxetech.projeto_final.repository.UsuarioRepository;
import al.oxetech.projeto_final.validator.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * Camada de serviço responsável por aplicar as regras de negócio relacionadas
 * a {@link Usuario}. É aqui que decidimos como a aplicação deverá agir ao
 * receber uma requisição antes de acessar o banco de dados.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository; // Interface de acesso aos dados
    private final PasswordEncoder passwordEncoder;     // Responsável pela criptografia das senhas
    private final UsuarioValidator usuarioValidator;
    private final UsuarioMapper usuarioMapper;
    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean;


    /**
     * Cria um novo usuário. A senha é codificada com um algoritmo de hash antes
     * de ser salva, garantindo que não seja armazenada em texto puro.
     */
    public UsuarioDTO salvar(UsuarioInputDTO dto) {
        Usuario u = new Usuario();
        u.setNome(dto.getNome());
        u.setEmail(dto.getEmail());
        // passwordEncoder.encode gera o hash da senha fornecida
        u.setSenha(passwordEncoder.encode(dto.getSenha()));
        u.setRole(dto.getRole());
        usuarioValidator.validar(u);
        return usuarioMapper.toUsuarioDTO(usuarioRepository.save(u));
    }

    /**
     * Lista todos os usuários cadastrados no sistema.
     */
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDTO::new)
                .toList();
    }

    public void delete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario com id: " + id + " não foi encontrado"));
        usuarioRepository.delete(usuario);
    }

    public UsuarioDTO atualizar(Long id, UsuarioInputDTO dto) {
        Usuario usuario = buscarPorId(id);

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setRole(dto.getRole());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        usuarioValidator.validar(usuario);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return usuarioMapper.toUsuarioDTO(usuarioSalvo);
    }

    public UsuarioDTO atualizarParcial(Long id, UsuarioPatchDTO usuarioPatchDTO) {
        try {
            Usuario usuarioExistente = buscarPorId(id);
            nullAwareBeanUtilsBean.copyProperties(usuarioExistente, usuarioMapper.toEntity(usuarioPatchDTO));
            if (usuarioPatchDTO.getSenha() != null && !usuarioPatchDTO.getSenha().isBlank()) {
                usuarioExistente.setSenha(passwordEncoder.encode(usuarioExistente.getSenha()));
            }
            usuarioRepository.save(usuarioExistente);
            return usuarioMapper.toUsuarioDTO(usuarioExistente);
        } catch (InvocationTargetException | IllegalAccessException exception) {
            throw new UsuarioAtualizacaoException("Erro ao atualizar parcialmente o usuário com id: " + id, exception);
        }
    }

    private Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com id: " + id + " não encontrado"));
    }
}
