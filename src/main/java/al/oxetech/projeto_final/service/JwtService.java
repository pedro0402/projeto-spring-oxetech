package al.oxetech.projeto_final.service;

import al.oxetech.projeto_final.model.Usuario;
import al.oxetech.projeto_final.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.security.KeyStoreUtil;
import org.hibernate.validator.internal.engine.valueextraction.ValueExtractorDescriptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {
    private final String jwtSecret = "e7ba27d0bde10c607b2267c9cbd0b4a860e3e319c5f550148eadd2274e3e9aff";
    private final Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

    public String gerarToken(Usuario u){
        return Jwts.builder()
                .setSubject(String.valueOf(u.getId()))
                .claim("email",u.getEmail())
                .claim("role",u.getRole())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validarToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
