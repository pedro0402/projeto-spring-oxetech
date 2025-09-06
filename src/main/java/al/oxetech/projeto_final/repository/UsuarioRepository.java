package al.oxetech.projeto_final.repository;

import al.oxetech.projeto_final.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByNome(String nome);

    boolean existsByNomeAndEmail(String nome, String email);

    boolean existsByNomeAndEmailAndIdNot(String nome, String email, Long id);
}
