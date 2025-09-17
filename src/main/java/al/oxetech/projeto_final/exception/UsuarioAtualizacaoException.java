package al.oxetech.projeto_final.exception;

public class UsuarioAtualizacaoException extends RuntimeException {
    public UsuarioAtualizacaoException(String message, ReflectiveOperationException exception) {
        super(message);
    }
}
