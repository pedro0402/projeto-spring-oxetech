package al.oxetech.projeto_final.security;

import al.oxetech.projeto_final.dto.relatorio.RelatorioDTO;
import al.oxetech.projeto_final.dto.relatorio.RelatorioInputDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioInputDTO;
import al.oxetech.projeto_final.model.Relatorio;
import al.oxetech.projeto_final.model.Usuario;
import al.oxetech.projeto_final.repository.RelatorioRepository;
import al.oxetech.projeto_final.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioService {
    private final RelatorioRepository relatorioRepository;
    private final UsuarioRepository usuarioRepository;

    public RelatorioService(RelatorioRepository relatorioRepository, UsuarioRepository usuarioRepository) {
        this.relatorioRepository = relatorioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public RelatorioDTO salvar(RelatorioInputDTO dto){
        Usuario autor = usuarioRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new EntityNotFoundException("usuario nao encontrado"));
        Relatorio r =  new Relatorio();
        r.setTitulo(dto.getTitulo());
        r.setConteudo(dto.getConteudo());
        r.setAutor(autor);

        return new RelatorioDTO(relatorioRepository.save(r));
    }

    public List<RelatorioDTO> listarTodos(){
        return relatorioRepository.findAll().stream().map(RelatorioDTO::new).toList();
    }

    public List<RelatorioDTO> listarPorUsuario(Long idUsuario){
        return relatorioRepository.findByAutorId(idUsuario).stream()
                .map(RelatorioDTO::new)
                .toList();
    }
}
