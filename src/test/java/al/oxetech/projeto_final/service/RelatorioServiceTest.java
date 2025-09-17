// src/test/java/al/oxetech/projeto_final/service/RelatorioServiceTest.java
package al.oxetech.projeto_final.service;

import al.oxetech.projeto_final.dto.relatorio.RelatorioDTO;
import al.oxetech.projeto_final.dto.relatorio.RelatorioInputDTO;
import al.oxetech.projeto_final.dto.relatorio.RelatorioInputUpdateDTO;
import al.oxetech.projeto_final.dto.relatorio.RelatorioPatchUpdateDTO;
import al.oxetech.projeto_final.exception.RelatorioNaoEncontradoException;
import al.oxetech.projeto_final.exception.UsuarioNaoEncontradoException;
import al.oxetech.projeto_final.helper.NullAwareBeanUtilsBean;
import al.oxetech.projeto_final.mapper.RelatorioMapper;
import al.oxetech.projeto_final.model.Relatorio;
import al.oxetech.projeto_final.model.Usuario;
import al.oxetech.projeto_final.repository.RelatorioRepository;
import al.oxetech.projeto_final.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {

    @Mock
    private RelatorioRepository relatorioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RelatorioMapper mapper;

    @Mock
    private NullAwareBeanUtilsBean nullAwareBeanUtilsBean;

    @InjectMocks
    private RelatorioService relatorioService;

    @Test
    void deveSalvarRelatorioComSucesso() {
        // Arrange
        RelatorioInputDTO inputDTO = new RelatorioInputDTO("Título", "Conteúdo", 1L);
        Usuario autor = new Usuario();
        autor.setId(1L);
        autor.setNome("Autor Teste");

        Relatorio relatorioSalvo = new Relatorio();
        relatorioSalvo.setId(1L);
        relatorioSalvo.setTitulo("Título");
        relatorioSalvo.setConteudo("Conteúdo");
        relatorioSalvo.setAutor(autor);

        RelatorioDTO relatorioDTO = new RelatorioDTO(1L, "Título", "Conteúdo", "Autor Teste");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(relatorioRepository.save(any(Relatorio.class))).thenReturn(relatorioSalvo);

        // Act
        RelatorioDTO result = relatorioService.salvar(inputDTO);

        // Assert
        assertNotNull(result);
        verify(usuarioRepository).findById(1L);
        verify(relatorioRepository).save(any(Relatorio.class));
    }

    @Test
    void deveLancarExcecaoQuandoAutorNaoExiste() {
        // Arrange
        RelatorioInputDTO inputDTO = new RelatorioInputDTO("Título", "Conteúdo", 999L);

        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            relatorioService.salvar(inputDTO);
        });

        verify(relatorioRepository, never()).save(any(Relatorio.class));
    }

    @Test
    void deveListarTodosRelatorios() {
        // Arrange
        Usuario autor = new Usuario();
        autor.setNome("Autor Teste");

        Relatorio relatorio1 = new Relatorio(1L, "Título 1", "Conteúdo 1", autor);
        Relatorio relatorio2 = new Relatorio(2L, "Título 2", "Conteúdo 2", autor);

        when(relatorioRepository.findAll()).thenReturn(List.of(relatorio1, relatorio2));

        // Act
        List<RelatorioDTO> result = relatorioService.listarTodos();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(relatorioRepository).findAll();
    }

    @Test
    void deveListarRelatoriosPorUsuario() {
        // Arrange
        Long usuarioId = 1L;
        Usuario autor = new Usuario();
        autor.setId(usuarioId);
        autor.setNome("Autor Teste");

        Relatorio relatorio1 = new Relatorio(1L, "Título 1", "Conteúdo 1", autor);
        Relatorio relatorio2 = new Relatorio(2L, "Título 2", "Conteúdo 2", autor);

        when(usuarioRepository.existsById(usuarioId)).thenReturn(true);
        when(relatorioRepository.findByAutorId(usuarioId)).thenReturn(List.of(relatorio1, relatorio2));

        // Act
        List<RelatorioDTO> result = relatorioService.listarPorUsuario(usuarioId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(usuarioRepository).existsById(usuarioId);
        verify(relatorioRepository).findByAutorId(usuarioId);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExisteAoListar() {
        // Arrange
        Long usuarioId = 999L;

        when(usuarioRepository.existsById(usuarioId)).thenReturn(false);

        // Act & Assert
        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            relatorioService.listarPorUsuario(usuarioId);
        });

        verify(relatorioRepository, never()).findByAutorId(any());
    }

    @Test
    void deveDeletarRelatorioExistente() {
        // Arrange
        Long relatorioId = 1L;
        Relatorio relatorio = new Relatorio();
        relatorio.setId(relatorioId);

        when(relatorioRepository.findById(relatorioId)).thenReturn(Optional.of(relatorio));
        doNothing().when(relatorioRepository).delete(relatorio);

        // Act
        assertDoesNotThrow(() -> relatorioService.delete(relatorioId));

        // Assert
        verify(relatorioRepository).findById(relatorioId);
        verify(relatorioRepository).delete(relatorio);
    }

    @Test
    void deveLancarExcecaoAoDeletarRelatorioInexistente() {
        // Arrange
        Long relatorioId = 999L;

        when(relatorioRepository.findById(relatorioId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RelatorioNaoEncontradoException.class, () -> {
            relatorioService.delete(relatorioId);
        });

        verify(relatorioRepository, never()).delete(any());
    }

    @Test
    void deveAtualizarRelatorioCompleto() {
        // Arrange
        Long relatorioId = 1L;
        RelatorioInputUpdateDTO updateDTO = new RelatorioInputUpdateDTO("Novo Título", "Novo Conteúdo");

        Relatorio relatorioExistente = new Relatorio();
        relatorioExistente.setId(relatorioId);
        relatorioExistente.setTitulo("Título Antigo");
        relatorioExistente.setConteudo("Conteúdo Antigo");

        Relatorio relatorioAtualizado = new Relatorio();
        relatorioAtualizado.setId(relatorioId);
        relatorioAtualizado.setTitulo("Novo Título");
        relatorioAtualizado.setConteudo("Novo Conteúdo");

        RelatorioDTO relatorioDTO = new RelatorioDTO(relatorioId, "Novo Título", "Novo Conteúdo", "Autor");

        when(relatorioRepository.findById(relatorioId)).thenReturn(Optional.of(relatorioExistente));
        when(relatorioRepository.save(any(Relatorio.class))).thenReturn(relatorioAtualizado);
        when(mapper.toRelatorioDTO(relatorioAtualizado)).thenReturn(relatorioDTO);

        // Act
        RelatorioDTO result = relatorioService.atualizar(relatorioId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Novo Título", result.getTitulo());
        assertEquals("Novo Conteúdo", result.getConteudo());

        verify(relatorioRepository).findById(relatorioId);
        verify(relatorioRepository).save(any(Relatorio.class));
    }

    @Test
    void deveLancarExcecaoNaAtualizacaoParcial() throws Exception {
        // Arrange
        Long relatorioId = 1L;
        RelatorioPatchUpdateDTO patchDTO = new RelatorioPatchUpdateDTO();

        Relatorio relatorioExistente = new Relatorio();
        relatorioExistente.setId(relatorioId);

        when(relatorioRepository.findById(relatorioId)).thenReturn(Optional.of(relatorioExistente));
        when(mapper.toEntity(any(RelatorioPatchUpdateDTO.class))).thenReturn(new Relatorio());

        doThrow(new IllegalAccessException("Erro de acesso"))
                .when(nullAwareBeanUtilsBean)
                .copyProperties(any(), any());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            relatorioService.atualizarParcial(relatorioId, patchDTO);
        });
    }

    @Test
    void deveLancarExcecaoAoBuscarRelatorioInexistente() {
        // Arrange
        Long relatorioId = 999L;

        when(relatorioRepository.findById(relatorioId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RelatorioNaoEncontradoException.class, () -> {
            relatorioService.buscarId(relatorioId);
        });
    }
}