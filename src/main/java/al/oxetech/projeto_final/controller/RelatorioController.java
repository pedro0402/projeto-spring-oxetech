package al.oxetech.projeto_final.controller;

import al.oxetech.projeto_final.dto.relatorio.RelatorioDTO;
import al.oxetech.projeto_final.dto.relatorio.RelatorioInputDTO;
import al.oxetech.projeto_final.service.RelatorioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Camada de apresentação dos relatórios.
 *
 * <p>Os controllers recebem as requisições HTTP e delegam o processamento para
 * as classes de serviço. Aqui, os endpoints expõem a API REST.</p>
 */
@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    /**
     * Endpoint para criação de um novo relatório.
     */
    @PostMapping
    public ResponseEntity<RelatorioDTO> criar(@RequestBody @Valid RelatorioInputDTO dto) {
        RelatorioDTO novoRelatorio = relatorioService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoRelatorio);
    }

    /**
     * Retorna todos os relatórios cadastrados.
     */
    @GetMapping
    public ResponseEntity<List<RelatorioDTO>> listarTodos() {
        List<RelatorioDTO> relatorioDTOS = relatorioService.listarTodos();
        return ResponseEntity.ok(relatorioDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<RelatorioDTO>> listarPorUsuario(@PathVariable Long id) {
        List<RelatorioDTO> relatorioDTOS = relatorioService.listarPorUsuario(id);
        return ResponseEntity.ok(relatorioDTOS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        relatorioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
