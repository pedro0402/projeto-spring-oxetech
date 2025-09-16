package al.oxetech.projeto_final.controller.common;

import al.oxetech.projeto_final.dto.ErroResposta;
import al.oxetech.projeto_final.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta erroGenerico(Exception exception) {
        return ErroResposta.erroInterno(exception.getMessage());
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResposta usuarioNaoEncontradoException(UsuarioNaoEncontradoException exception) {
        return ErroResposta.naoEncontrado(exception.getMessage());
    }

    @ExceptionHandler(UsuarioCadastradoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta usuarioCadastradoException(UsuarioCadastradoException exception) {
        return ErroResposta.conflito(exception.getMessage());
    }

    @ExceptionHandler(UsuarioAtualizacaoException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResposta usuarioAtualizacaoException(UsuarioAtualizacaoException exception) {
        return ErroResposta.erroInterno(exception.getMessage());
    }

    @ExceptionHandler(SenhaInvalidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta senhaInvalidaException(SenhaInvalidaException exception) {
        return ErroResposta.respostaPadrao(exception.getMessage());
    }

    @ExceptionHandler(RelatorioNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResposta relatorioNaoEncontradoException(RelatorioNaoEncontradoException exception) {
        return ErroResposta.naoEncontrado(exception.getMessage());
    }

    @ExceptionHandler(TokenInvalidoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta tokenInvalidoException(TokenInvalidoException exception) {
        return ErroResposta.respostaPadrao(exception.getMessage());
    }

    @ExceptionHandler(TokenExpiradoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta tokenExpiradoException(TokenExpiradoException exception) {
        return ErroResposta.respostaPadrao(exception.getMessage());
    }
}
