package al.oxetech.projeto_final.service;

import al.oxetech.projeto_final.exception.SenhaInvalidaException;
import al.oxetech.projeto_final.exception.UsuarioNaoEncontradoException;
import al.oxetech.projeto_final.mapper.UsuarioMapper;
import al.oxetech.projeto_final.model.PasswordResetToken;
import al.oxetech.projeto_final.model.Usuario;
import al.oxetech.projeto_final.repository.PasswordResetTokenRepository;
import al.oxetech.projeto_final.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final PasswordResetTokenRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    @Transactional
    public void gerarTokenEEnviarEmail(String email) {


        //buscando meu usuário por email (que passei no cadastro)
        Usuario usuario = usuarioRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        //deleta algum token antigo baseado no usuário (caso exista)
        tokenRepository.deleteByUsuario(usuario);
        tokenRepository.flush();

        //gerando um token aleatorio de 6 digitos (posso gera com + ou -)
        String codigo = String.format("%06d", new Random().nextInt(999999));

        //criando meu token e passando dados do token
        PasswordResetToken token = new PasswordResetToken();
        token.setUsuario(usuario);
        token.setToken(codigo);
        token.setExpiracao(LocalDateTime.now().plusMinutes(5));

        //salvando meu token
        tokenRepository.save(token);

        SimpleMailMessage message = new SimpleMailMessage();
        //aqui eu digo q é pra ele enviar para o email que peguei do meu usuario que foi encontrado no começo
        message.setTo(usuario.getEmail());
        //aqui eu to definindo meu titulo do email
        message.setSubject("Código para redefinir sua senha");
        //aq eu digo o corpo do meu email
        message.setText("Seu código para redefinir sua senha é: " + codigo + " (válido por 5 minutos)");

        //aqui eu defino o envio da mensagem do meu email
        mailSender.send(message);

    }

    public void redefinirSenha(String tokenCodigo, String novaSenha) {
        if (novaSenha.length() <= 6){
            throw new SenhaInvalidaException("senha deve ter no minimo 6 caracteres");
        }

        //procuro no meu banco o token passado. caso nao exista, o token é inválido
        PasswordResetToken token = tokenRepository.findByToken(tokenCodigo)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        //se o meu token tiver vencido, vai lançar uma exceção
        if (token.getExpiracao().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token expirado");
        }

        //aqui eu pego o usuário que está associado ao meu token e defino a nova senha dele
        Usuario usuario = token.getUsuario();

        usuario.setSenha(passwordEncoder.encode(novaSenha));

        usuarioRepository.save(usuario);

        tokenRepository.delete(token);
    }
}
