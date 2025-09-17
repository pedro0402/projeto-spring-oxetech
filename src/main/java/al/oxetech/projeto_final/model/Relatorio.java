package al.oxetech.projeto_final.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa um relatório criado por um usuário do sistema.
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Relatorio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador do relatório

    private String titulo;   // Título do documento
    private String conteudo; // Texto do relatório

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor; // Relacionamento: muitos relatórios para um usuário
}
