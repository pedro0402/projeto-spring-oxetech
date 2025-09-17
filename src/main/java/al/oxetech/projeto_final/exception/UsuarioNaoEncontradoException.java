package al.oxetech.projeto_final.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(String mensagem){
        super(mensagem);
    }
}
