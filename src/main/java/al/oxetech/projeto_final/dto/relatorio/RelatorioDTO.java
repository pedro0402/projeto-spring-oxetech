package al.oxetech.projeto_final.dto.relatorio;

import al.oxetech.projeto_final.model.Relatorio;
import lombok.*;

/**
 * Objeto de transferência de dados (DTO) para expor informações do
 * {@link Relatorio} na API sem precisar enviar a entidade completa.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioDTO {
    private Long id;
    private String titulo;
    private String conteudo;
    private String autorNome; // Apenas o nome do autor é retornado para simplificar

    public RelatorioDTO(Relatorio r) {
        this.id = r.getId();
        this.titulo = r.getTitulo();
        this.conteudo = r.getConteudo();
        this.autorNome = r.getAutor().getNome();
    }
}