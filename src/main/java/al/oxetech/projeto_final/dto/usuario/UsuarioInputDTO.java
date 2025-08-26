package al.oxetech.projeto_final.dto.usuario;

import al.oxetech.projeto_final.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO usado para receber os dados do cliente ao criar um novo usuário.
 * Inclui anotações de validação para garantir a integridade das informações.
 */
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
    private String senha; // Senha em texto puro recebida da requisição

    private Role role; // Papel escolhido para o novo usuário
}
