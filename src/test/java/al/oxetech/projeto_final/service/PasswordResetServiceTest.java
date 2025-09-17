// src/test/java/al/oxetech/projeto_final/service/PasswordResetServiceTest.java
package al.oxetech.projeto_final.service;

import al.oxetech.projeto_final.exception.SenhaInvalidaException;
import al.oxetech.projeto_final.exception.TokenExpiradoException;
import al.oxetech.projeto_final.exception.TokenInvalidoException;
import al.oxetech.projeto_final.exception.UsuarioNaoEncontradoException;
import al.oxetech.projeto_final.model.PasswordResetToken;
import al.oxetech.projeto_final.model.Usuario;
import al.oxetech.projeto_final.repository.PasswordResetTokenRepository;
import al.oxetech.projeto_final.repository.UsuarioRepository;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetService passwordResetService;

    @Test
    void deveGerarTokenParaUsuarioExistente() {
        // Arrange
        String email = "usuario@email.com";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);

        when(usuarioRepository.findByEmail(email.trim().toLowerCase()))
                .thenReturn(Optional.of(usuario));

        // Capturar o token salvo para verificação
        ArgumentCaptor<PasswordResetToken> tokenCaptor = ArgumentCaptor.forClass(PasswordResetToken.class);

        // Act
        assertDoesNotThrow(() -> passwordResetService.gerarTokenEEnviarEmail(email));

        // Assert
        verify(tokenRepository).deleteByUsuario(usuario);
        verify(tokenRepository).save(tokenCaptor.capture());
        verify(mailSender).send(any(SimpleMailMessage.class));

        // Verificar o token salvo
        PasswordResetToken tokenSalvo = tokenCaptor.getValue();
        assertNotNull(tokenSalvo);
        assertEquals(usuario, tokenSalvo.getUsuario());
        assertNotNull(tokenSalvo.getToken());
        assertEquals(6, tokenSalvo.getToken().length()); // 6 dígitos
        assertTrue(tokenSalvo.getExpiracao().isAfter(LocalDateTime.now()));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        // Arrange
        String email = "inexistente@email.com";

        when(usuarioRepository.findByEmail(email.trim().toLowerCase()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            passwordResetService.gerarTokenEEnviarEmail(email);
        });

        // Verificar que não tentou salvar token ou enviar email
        verify(tokenRepository, never()).deleteByUsuario(any());
        verify(tokenRepository, never()).save(any());
        verify(mailSender, never()).send((MimeMessage) any());
    }

    @Test
    void deveRedefinirSenhaComTokenValido() {
        // Arrange
        String tokenCodigo = "123456";
        String novaSenha = "novaSenha123";

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenCodigo);
        token.setExpiracao(LocalDateTime.now().plusMinutes(10));

        Usuario usuario = new Usuario();
        usuario.setSenha("senhaAntiga");
        token.setUsuario(usuario);

        when(tokenRepository.findByToken(tokenCodigo))
                .thenReturn(Optional.of(token));
        when(passwordEncoder.encode(novaSenha))
                .thenReturn("senhaCriptografada");

        // Act
        assertDoesNotThrow(() ->
                passwordResetService.redefinirSenha(tokenCodigo, novaSenha));

        // Assert
        verify(passwordEncoder).encode(novaSenha);
        verify(usuarioRepository).save(usuario);
        verify(tokenRepository).delete(token);
        assertEquals("senhaCriptografada", usuario.getSenha());
    }

    @Test
    void deveLancarExcecaoQuandoTokenNaoExiste() {
        // Arrange
        String tokenCodigo = "999999";
        String novaSenha = "novaSenha123";

        when(tokenRepository.findByToken(tokenCodigo))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TokenInvalidoException.class, () -> {
            passwordResetService.redefinirSenha(tokenCodigo, novaSenha);
        });

        verify(usuarioRepository, never()).save(any());
        verify(tokenRepository, never()).delete(any());
    }

    @Test
    void deveLancarExcecaoQuandoTokenExpirado() {
        // Arrange
        String tokenCodigo = "123456";
        String novaSenha = "novaSenha123";

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenCodigo);
        token.setExpiracao(LocalDateTime.now().minusMinutes(1)); // Token expirado

        when(tokenRepository.findByToken(tokenCodigo))
                .thenReturn(Optional.of(token));

        // Act & Assert
        assertThrows(TokenExpiradoException.class, () -> {
            passwordResetService.redefinirSenha(tokenCodigo, novaSenha);
        });

        verify(usuarioRepository, never()).save(any());
        verify(tokenRepository, never()).delete(any());
    }

    @Test
    void deveLancarExcecaoQuandoSenhaMuitoCurta() {
        // Arrange
        String tokenCodigo = "123456";
        String senhaCurta = "123"; // Senha com menos de 6 caracteres

        // Act & Assert
        assertThrows(SenhaInvalidaException.class, () -> {
            passwordResetService.redefinirSenha(tokenCodigo, senhaCurta);
        });

        // Verificar que nem procurou o token
        verify(tokenRepository, never()).findByToken(any());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoSenhaExatamente6Caracteres() {
        // Arrange
        String tokenCodigo = "123456";
        String senhaExatos6 = "123456"; // Exatamente 6 caracteres (deve passar)

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenCodigo);
        token.setExpiracao(LocalDateTime.now().plusMinutes(10));
        token.setUsuario(new Usuario());

        when(tokenRepository.findByToken(tokenCodigo))
                .thenReturn(Optional.of(token));
        when(passwordEncoder.encode(senhaExatos6))
                .thenReturn("senhaCriptografada");

        // Act & Assert - Não deve lançar exceção
        assertDoesNotThrow(() ->
                passwordResetService.redefinirSenha(tokenCodigo, senhaExatos6));

        verify(usuarioRepository).save(any());
        verify(tokenRepository).delete(any());
    }

    @Test
    void deveDeletarTokensAntigosAoGerarNovo() {
        // Arrange
        String email = "usuario@email.com";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);

        when(usuarioRepository.findByEmail(email.trim().toLowerCase()))
                .thenReturn(Optional.of(usuario));

        // Act
        assertDoesNotThrow(() -> passwordResetService.gerarTokenEEnviarEmail(email));

        // Assert - Deve deletar tokens antigos antes de salvar novo
        verify(tokenRepository).deleteByUsuario(usuario);
        verify(tokenRepository).flush(); // Verificar o flush
        verify(tokenRepository).save(any(PasswordResetToken.class));
    }
}