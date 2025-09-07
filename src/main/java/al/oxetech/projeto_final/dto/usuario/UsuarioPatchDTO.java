package al.oxetech.projeto_final.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPatchDTO {
    private String nome;
    private String email;
    private String role;
    private String senha;
}
