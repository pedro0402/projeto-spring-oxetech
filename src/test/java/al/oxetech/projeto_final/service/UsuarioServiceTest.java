// src/test/java/al/oxetech/projeto_final/service/UsuarioServiceTest.java
package al.oxetech.projeto_final.service;

import al.oxetech.projeto_final.dto.usuario.UsuarioDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioInputDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioPatchDTO;
import al.oxetech.projeto_final.dto.usuario.UsuarioUpdateDTO;
import al.oxetech.projeto_final.exception.UsuarioAtualizacaoException;
import al.oxetech.projeto_final.exception.UsuarioCadastradoException;
import al.oxetech.projeto_final.exception.UsuarioNaoEncontradoException;
import al.oxetech.projeto_final.helper.NullAwareBeanUtilsBean;
import al.oxetech.projeto_final.mapper.UsuarioMapper;
import al.oxetech.projeto_final.model.Role;
import al.oxetech.projeto_final.model.Usuario;
import al.oxetech.projeto_final.repository.UsuarioRepository;
import al.oxetech.projeto_final.validator.UsuarioValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioValidator usuarioValidator;

    @Mock // ← ADICIONAR ESTE MOCK!
    private UsuarioMapper usuarioMapper;

    @Mock // ← E ESTE TAMBÉM!
    private NullAwareBeanUtilsBean nullAwareBeanUtilsBean;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveSalvarUsuarioComSenhaCriptografada() {
        // Arrange (Preparação)
        UsuarioInputDTO dto = new UsuarioInputDTO(
                "João Silva",
                "joao@email.com",
                "senha123",
                Role.COLABORADOR
        );

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setNome("João Silva");
        usuarioSalvo.setEmail("joao@email.com");
        usuarioSalvo.setSenha("senha_criptografada");
        usuarioSalvo.setRole(Role.COLABORADOR);

        UsuarioDTO usuarioDTO = new UsuarioDTO(1L, "João Silva", "joao@email.com", Role.COLABORADOR);

        when(passwordEncoder.encode("senha123")).thenReturn("senha_criptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);
        when(usuarioMapper.toUsuarioDTO(usuarioSalvo)).thenReturn(usuarioDTO); // ← CONFIGURAR O MOCK!

        // Act (Ação)
        var result = usuarioService.salvar(dto);

        // Assert (Verificação)
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("João Silva", result.getNome());
        assertEquals("joao@email.com", result.getEmail());

        // Verifica se a senha foi criptografada
        verify(passwordEncoder).encode("senha123");
        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioMapper).toUsuarioDTO(usuarioSalvo); // ← VERIFICAR SE FOI CHAMADO!
    }

    @Test
    void deveLancarExcecaoAoSalvarUsuarioComEmailDuplicado() {
        UsuarioInputDTO dto = new UsuarioInputDTO(
                "João Silva",
                "joao@email.com",
                "senha123",
                Role.COLABORADOR
        );

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setEmail("joao@email.com");

        doThrow(new UsuarioCadastradoException("Email já existe"))
                .when(usuarioValidator)
                .validar(any(Usuario.class));

        assertThrows(UsuarioCadastradoException.class, () -> {
            usuarioService.salvar(dto);
        });

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveBuscarUsuarioPorIdExistente() {
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setNome("João da Silva");

        UsuarioDTO usuarioDTO = new UsuarioDTO(usuarioId, "João da Silva", "joao@email.com", Role.COLABORADOR);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toUsuarioDTO(usuario)).thenReturn(usuarioDTO);

        UsuarioDTO result = usuarioService.buscarId(usuarioId);

        assertNotNull(result);
        assertEquals(usuarioId, result.getId());
    }

    @Test
    void deveCriptografarSenhaCorretamente() {
        String senha = "senha123";

        when(passwordEncoder.encode(senha)).thenReturn("hash_criptografado");

        String resultado = passwordEncoder.encode(senha);

        assertEquals("hash_criptografado", resultado);
        verify(passwordEncoder).encode(senha);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> {
            usuarioService.buscarId(999L);
        });
    }

    @Test
    void deveValidarUsuarioAntesDeSalvar() {
        UsuarioInputDTO usuarioInputDTO = new UsuarioInputDTO("João", "joao@email.com", "senha", Role.COLABORADOR);

        Usuario usuario = new Usuario();
        usuario.setNome("João");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("senha");
        usuario.setRole(Role.COLABORADOR);

        usuarioService.salvar(usuarioInputDTO);

        verify(usuarioValidator).validar(any(Usuario.class));

    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        Long usuarioId = 1L;
        UsuarioUpdateDTO updateDTO = new UsuarioUpdateDTO(
          "João Atualizado", "joao.novo@email.com", Role.ADMIN
        );

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(usuarioId);
        usuarioExistente.setNome("João Antigo");
        usuarioExistente.setEmail("joao.antigo@email.com");
        usuarioExistente.setRole(Role.COLABORADOR);

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId(usuarioId);
        usuarioAtualizado.setNome("João Atualizado");
        usuarioAtualizado.setEmail("joao.novo@email.com");
        usuarioAtualizado.setRole(Role.ADMIN);

        UsuarioDTO usuarioDTO = new UsuarioDTO(usuarioId, "João Atualizado", "joao.novo@email.com", Role.ADMIN);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);
        when(usuarioMapper.toUsuarioDTO(usuarioAtualizado)).thenReturn(usuarioDTO);

        UsuarioDTO result = usuarioService.atualizar(usuarioId, updateDTO);

        assertNotNull(result);
        assertEquals("João Atualizado", result.getNome());
        assertEquals("joao.novo@email.com", result.getEmail());
        assertEquals(Role.ADMIN, result.getRole());

        verify(usuarioValidator).validar(any(Usuario.class));
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void deveAtualizarParcialmenteUsuario() throws InvocationTargetException, IllegalAccessException {
        // Arrange
        Long usuarioId = 1L;
        UsuarioPatchDTO patchDTO = new UsuarioPatchDTO();
        patchDTO.setNome("Novo Nome");

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(usuarioId);
        usuarioExistente.setNome("Nome Antigo");
        usuarioExistente.setEmail("email@antigo.com");
        usuarioExistente.setSenha("senha123");
        usuarioExistente.setRole(Role.COLABORADOR);

        UsuarioDTO usuarioDTO = new UsuarioDTO(usuarioId, "Novo Nome", "email@antigo.com", Role.COLABORADOR);

        // Configurações dos mocks
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuarioSalvo = invocation.getArgument(0);
            return usuarioSalvo; // Retorna o próprio objeto salvo
        });

        when(usuarioMapper.toUsuarioDTO(any(Usuario.class))).thenReturn(usuarioDTO);

        // Act
        UsuarioDTO result = usuarioService.atualizarParcial(usuarioId, patchDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Novo Nome", result.getNome());
        // Email e role devem permanecer os mesmos (não foram alterados no patch)
        assertEquals("email@antigo.com", result.getEmail());
        assertEquals(Role.COLABORADOR, result.getRole());

        verify(usuarioRepository).findById(usuarioId);
        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioMapper).toUsuarioDTO(any(Usuario.class));

        // Verificar que o NullAwareBeanUtilsBean foi usado
        verify(nullAwareBeanUtilsBean).copyProperties(any(), any());
    }

    @Test
    void deveDeletarUsuarioComSucesso() {
        // Arrange
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        // Act
        assertDoesNotThrow(() -> usuarioService.delete(usuarioId));

        // Assert
        verify(usuarioRepository).findById(usuarioId);
        verify(usuarioRepository).delete(usuario);

        assertDoesNotThrow(() -> usuarioService.delete(usuarioId));
    }

    @Test
    void deveListarTodosUsuarios() {
        // Arrange
        Usuario usuario1 = new Usuario(1L, "João", "joao@email.com", "senha123", Role.COLABORADOR);
        Usuario usuario2 = new Usuario(2L, "Maria", "maria@email.com", "senha456", Role.ADMIN);

        List<Usuario> usuarios = List.of(usuario1, usuario2);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Act
        List<UsuarioDTO> result = usuarioService.listarTodos();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("João", result.get(0).getNome());
        assertEquals("Maria", result.get(1).getNome());

        verify(usuarioRepository).findAll();
    }

    @Test
    void deveLancarExcecaoNaAtualizacaoParcial() throws InvocationTargetException, IllegalAccessException {
        // Arrange
        Long usuarioId = 1L;
        UsuarioPatchDTO patchDTO = new UsuarioPatchDTO();

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(usuarioId);

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioMapper.toEntity(any(UsuarioPatchDTO.class))).thenReturn(new Usuario());

        // Simular erro no copyProperties
        doThrow(new IllegalAccessException("Erro de acesso"))
                .when(nullAwareBeanUtilsBean)
                .copyProperties(any(), any());

        // Act & Assert
        assertThrows(UsuarioAtualizacaoException.class, () -> {
            usuarioService.atualizarParcial(usuarioId, patchDTO);
        });
    }
}