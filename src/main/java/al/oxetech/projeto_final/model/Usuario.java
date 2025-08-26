package al.oxetech.projeto_final.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {
    @Id @GeneratedValue
    private Long id;
    private String nome;
    @Column(unique = true)
    private String email;
    private String senha;
    private Role role;
}
