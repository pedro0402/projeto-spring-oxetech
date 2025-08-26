package al.oxetech.projeto_final.dto.usuario;

import al.oxetech.projeto_final.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioInputDTO {
    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String senha;

    private Role role;
}
