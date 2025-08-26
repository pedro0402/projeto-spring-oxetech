package al.oxetech.projeto_final.dto.relatorio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioInputDTO {
    @NotBlank
    private String titulo;

    @NotBlank
    private String conteudo;

    @NotNull
    private Long autorId;
}