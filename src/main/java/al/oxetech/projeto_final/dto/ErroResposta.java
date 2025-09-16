package al.oxetech.projeto_final.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResposta(int status,
                           String mensagem,
                           List<ErroCampo> erros,
                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
                           LocalDateTime timestamp) {


    public ErroResposta(HttpStatus httpStatus, String mensagem, List<ErroCampo> erros) {
        this(httpStatus.value(), mensagem, erros, LocalDateTime.now());
    }

    public static ErroResposta respostaPadrao(String mensagem) {
        return new ErroResposta(HttpStatus.BAD_REQUEST, mensagem, List.of());
    }

    public static ErroResposta conflito(String mensagem) {
        return new ErroResposta(HttpStatus.CONFLICT, mensagem, List.of());
    }

    public static ErroResposta naoEncontrado(String message) {
        return new ErroResposta(HttpStatus.NOT_FOUND, message, List.of());
    }

    public static ErroResposta erroInterno(String mensagem) {
        return new ErroResposta(HttpStatus.INTERNAL_SERVER_ERROR, mensagem, List.of());
    }
}
