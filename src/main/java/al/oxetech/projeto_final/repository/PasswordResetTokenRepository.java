package al.oxetech.projeto_final.repository;

import al.oxetech.projeto_final.model.PasswordResetToken;
import al.oxetech.projeto_final.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUsuario(Usuario usuario);
}
