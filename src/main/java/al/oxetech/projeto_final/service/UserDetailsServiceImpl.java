package al.oxetech.projeto_final.service;

import al.oxetech.projeto_final.repository.UsuarioRepository;
import al.oxetech.projeto_final.security.UsuarioAuthenticated;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByNome(username)
                .map(UsuarioAuthenticated::new)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }
}
