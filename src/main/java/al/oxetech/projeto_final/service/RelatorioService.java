package al.oxetech.projeto_final.service;

import al.oxetech.projeto_final.dto.relatorio.RelatorioDTO;
import al.oxetech.projeto_final.dto.relatorio.RelatorioInputDTO;
import al.oxetech.projeto_final.dto.relatorio.RelatorioInputUpdateDTO;
import al.oxetech.projeto_final.exception.RelatorioNaoEncontradoException;
import al.oxetech.projeto_final.exception.UsuarioNaoEncontradoException;
import al.oxetech.projeto_final.mapper.RelatorioMapper;
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
    private final RelatorioMapper mapper;

    public RelatorioService(RelatorioRepository relatorioRepository, UsuarioRepository usuarioRepository, RelatorioMapper mapper) {
        this.relatorioRepository = relatorioRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    /**
     * Salva um novo relatório. Primeiro busca o autor informado; se não existir,
     * lança uma {@link EntityNotFoundException}.
     */
    public RelatorioDTO salvar(RelatorioInputDTO dto) {
        Usuario autor = usuarioRepository.findById(dto.getAutorId())
                .orElseThrow(() -> new EntityNotFoundException("usuario nao encontrado"));
        Relatorio r = new Relatorio();
        r.setTitulo(dto.getTitulo());
        r.setConteudo(dto.getConteudo());
        r.setAutor(autor);

        return new RelatorioDTO(relatorioRepository.save(r));
    }

    /**
     * Retorna todos os relatórios cadastrados no banco de dados.
     */
    public List<RelatorioDTO> listarTodos() {
        return relatorioRepository.findAll().stream().map(RelatorioDTO::new).toList();
    }

    /**
     * Lista os relatórios pertencentes a um determinado usuário.
     */
    public List<RelatorioDTO> listarPorUsuario(Long idUsuario) {

        if (!usuarioRepository.existsById(idUsuario)) {
            throw new UsuarioNaoEncontradoException("Usuário: " + idUsuario + " não encontrado no banco");
        }

        return relatorioRepository.findByAutorId(idUsuario).stream()
                .map(RelatorioDTO::new)
                .toList();
    }

    public void delete(Long id) {
        Relatorio relatorio = buscarPorId(id);

        relatorioRepository.delete(relatorio);
    }

    public RelatorioDTO atualizar(Long id, RelatorioInputUpdateDTO updateDTO) {
        Relatorio relatorio = buscarPorId(id);

        relatorio.setTitulo(updateDTO.getTitulo());
        relatorio.setConteudo(updateDTO.getConteudo());


        Relatorio save = relatorioRepository.save(relatorio);

        return mapper.toRelatorioDTO(save);
    }

    private Relatorio buscarPorId(Long id) {
        return relatorioRepository.findById(id)
                .orElseThrow(() -> new RelatorioNaoEncontradoException("Relatório com ID: " + id + " não foi encontrado"));
    }
}
