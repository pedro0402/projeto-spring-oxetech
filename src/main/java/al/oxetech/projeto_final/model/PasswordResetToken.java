package al.oxetech.projeto_final.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token; //código aleatório enviado por email

    @OneToOne
    private Usuario usuario;

    private LocalDateTime expiracao; //define até quando o token é valido
}
