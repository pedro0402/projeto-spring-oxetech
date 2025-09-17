package al.oxetech.projeto_final.dto.relatorio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para criação de relatórios.
 * Contém apenas os dados necessários enviados pelo cliente.
 */
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
    private Long autorId; // Identificador do autor que está criando o relatório
}