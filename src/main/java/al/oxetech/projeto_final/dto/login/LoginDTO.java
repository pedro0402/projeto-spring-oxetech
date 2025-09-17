package al.oxetech.projeto_final.dto.login;

import jakarta.validation.constraints.NotBlank;

public class LoginDTO {
    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
