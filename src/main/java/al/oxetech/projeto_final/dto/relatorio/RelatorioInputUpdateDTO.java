package al.oxetech.projeto_final.dto.relatorio;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioInputUpdateDTO {
    @NotBlank
    private String titulo;

    @NotBlank
    private String conteudo;
}
