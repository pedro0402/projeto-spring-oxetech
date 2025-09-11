package al.oxetech.projeto_final.dto.usuario;

import al.oxetech.projeto_final.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {
    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    private Role role; // Papel escolhido para o novo usuário
}
