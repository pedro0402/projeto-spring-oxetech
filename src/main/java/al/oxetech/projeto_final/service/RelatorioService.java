package al.oxetech.projeto_final.service;

import al.oxetech.projeto_final.dto.relatorio.RelatorioDTO;
import al.oxetech.projeto_final.dto.relatorio.RelatorioInputDTO;
import al.oxetech.projeto_final.model.Relatorio;
import al.oxetech.projeto_final.model.Usuario;
import al.oxetech.projeto_final.repository.RelatorioRepository;
import al.oxetech.projeto_final.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Regras de negócio referentes aos relatórios. A camada de serviço coordena o
 * acesso ao repositório e aplica validações antes de persistir ou retornar
 * dados.
 */
@Service
public class RelatorioService {
    private final RelatorioRepository relatorioRepository;
    private final UsuarioRepository usuarioRepository;

    public RelatorioService(RelatorioRepository relatorioRepository, UsuarioRepository usuarioRepository) {
        this.relatorioRepository = relatorioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Salva um novo relatório. Primeiro busca o autor informado; se não existir,
     * lança uma {@link EntityNotFoundException}.
     */
    public RelatorioDTO salvar(RelatorioInputDTO dto){
        Usuario autor = usuarioRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new EntityNotFoundException("usuario nao encontrado"));
        Relatorio r =  new Relatorio();
        r.setTitulo(dto.getTitulo());
        r.setConteudo(dto.getConteudo());
        r.setAutor(autor);

        return new RelatorioDTO(relatorioRepository.save(r));
    }

    /**
     * Retorna todos os relatórios cadastrados no banco de dados.
     */
    public List<RelatorioDTO> listarTodos(){
        return relatorioRepository.findAll().stream().map(RelatorioDTO::new).toList();
    }

    /**
     * Lista os relatórios pertencentes a um determinado usuário.
     */
    public List<RelatorioDTO> listarPorUsuario(Long idUsuario){
        return relatorioRepository.findByAutorId(idUsuario).stream()
                .map(RelatorioDTO::new)
                .toList();
    }
}
