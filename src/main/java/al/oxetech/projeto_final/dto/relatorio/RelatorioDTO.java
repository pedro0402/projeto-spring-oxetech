package al.oxetech.projeto_final.dto.relatorio;

import al.oxetech.projeto_final.model.Relatorio;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioDTO {
    private Long id;
    private String titulo;
    private String conteudo;
    private String autorNome;

    public RelatorioDTO(Relatorio r) {
        this.id = r.getId();
        this.titulo = r.getTitulo();
        this.conteudo = r.getConteudo();
        this.autorNome = r.getAutor().getNome();
    }
}