package al.oxetech.projeto_final.validator;

import al.oxetech.projeto_final.exception.UsuarioCadastradoException;
import al.oxetech.projeto_final.model.Usuario;
import al.oxetech.projeto_final.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioValidator {

    private final UsuarioRepository usuarioRepository;

    public void validar(Usuario usuario) {
        if (usuarioEstaCadastrado(usuario)) {
            throw new UsuarioCadastradoException("Um usuário com o mesmo nome ou email já existem");
        }
    }

    private boolean usuarioEstaCadastrado(Usuario usuario) {
        if (usuario.getId() == null) {
            return usuarioRepository.existsByNomeAndEmail(usuario.getNome(), usuario.getEmail());
        } else {
            return usuarioRepository.existsByNomeAndEmailAndIdNot(
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getId());
        }
    }
}
