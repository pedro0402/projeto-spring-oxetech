package al.oxetech.projeto_final.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa um usuário do sistema.
 *
 * <p>Os campos anotados com JPA (@Id, @Column, etc.) são mapeados para a
 * tabela correspondente no banco de dados. A anotação {@code @Entity}
 * indica ao Hibernate que esta classe deve gerar uma tabela.</p>
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {
    @Id @GeneratedValue
    private Long id; // Identificador único gerado automaticamente

    private String nome;

    @Column(unique = true)
    private String email; // Email deve ser único no sistema

    /**
     * Senha do usuário. Ela é armazenada no banco em formato criptografado
     * (hash) para garantir a segurança das credenciais.
     */
    private String senha;

    /**
     * Papel do usuário no sistema. Utilizamos o enum {@link Role} para limitar
     * os valores possíveis e manter a consistência dos dados.
     */
    private Role role;
}
