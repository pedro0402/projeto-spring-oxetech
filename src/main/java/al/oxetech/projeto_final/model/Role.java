package al.oxetech.projeto_final.model;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Enumeração que define os papéis que um usuário pode assumir no sistema.
 *
 * <p>Enums representam um conjunto fixo de valores constantes. Ao usar um
 * {@code enum} para os papéis, garantimos que o campo {@code role} do
 * {@link al.oxetech.projeto_final.model.Usuario} somente possa receber um dos
 * valores aqui definidos, evitando erros e facilitando a leitura do código.</p>
 */
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Role {
    ADMIN,
    GERENTE,
    COLABORADOR;
}
