package al.oxetech.projeto_final.exception;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(String mensagem){
        super(mensagem);
    }
}
