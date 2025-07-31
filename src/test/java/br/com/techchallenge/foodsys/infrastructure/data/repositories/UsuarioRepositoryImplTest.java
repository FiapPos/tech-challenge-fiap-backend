package br.com.techchallenge.foodsys.infrastructure.data.repositories;

import br.com.techchallenge.foodsys.core.domain.entities.Usuario;
import br.com.techchallenge.foodsys.infrastructure.data.entities.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryImplTest {

    @Mock
    private UsuarioJpaRepository jpaRepository;

    @InjectMocks
    private UsuarioRepositoryImpl repository;

    private Usuario usuario;
    private UsuarioEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao123");
        usuario.setSenha("senha123");
        usuario.setAtivo(true);
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setDataAtualizacao(LocalDateTime.now());

        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(1L);
        usuarioEntity.setNome("João Silva");
        usuarioEntity.setEmail("joao@email.com");
        usuarioEntity.setLogin("joao123");
        usuarioEntity.setSenha("senha123");
        usuarioEntity.setAtivo(true);
        usuarioEntity.setDataCriacao(LocalDateTime.now());
        usuarioEntity.setDataAtualizacao(LocalDateTime.now());
    }

    @Test
    void deveSalvarUsuarioComSucesso() {
        // Given
        when(jpaRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        // When
        Usuario resultado = repository.save(usuario);

        // Then
        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
        assertEquals(usuario.getNome(), resultado.getNome());
        assertEquals(usuario.getEmail(), resultado.getEmail());
        assertEquals(usuario.getLogin(), resultado.getLogin());
        verify(jpaRepository).save(any(UsuarioEntity.class));
    }

    @Test
    void deveEncontrarUsuarioPorId() {
        // Given
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(usuarioEntity));

        // When
        Optional<Usuario> resultado = repository.findById(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(usuario.getId(), resultado.get().getId());
        assertEquals(usuario.getNome(), resultado.get().getNome());
        assertEquals(usuario.getEmail(), resultado.get().getEmail());
        verify(jpaRepository).findById(1L);
    }

    @Test
    void deveRetornarEmptyQuandoUsuarioNaoEncontrado() {
        // Given
        when(jpaRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Usuario> resultado = repository.findById(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(jpaRepository).findById(999L);
    }

    @Test
    void deveListarTodosOsUsuarios() {
        // Given
        UsuarioEntity usuario2 = new UsuarioEntity();
        usuario2.setId(2L);
        usuario2.setNome("Maria Silva");
        usuario2.setEmail("maria@email.com");
        usuario2.setLogin("maria123");
        usuario2.setSenha("senha456");
        usuario2.setAtivo(true);

        List<UsuarioEntity> entities = Arrays.asList(usuarioEntity, usuario2);
        when(jpaRepository.findAll()).thenReturn(entities);

        // When
        List<Usuario> resultado = repository.findAll();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(usuario.getId(), resultado.get(0).getId());
        assertEquals(usuario2.getId(), resultado.get(1).getId());
        verify(jpaRepository).findAll();
    }

    @Test
    void deveDeletarUsuarioPorId() {
        // Given
        doNothing().when(jpaRepository).deleteById(1L);

        // When
        repository.deleteById(1L);

        // Then
        verify(jpaRepository).deleteById(1L);
    }

    @Test
    void deveVerificarSeExistePorEmail() {
        // Given
        when(jpaRepository.existsByEmail("joao@email.com")).thenReturn(true);
        when(jpaRepository.existsByEmail("inexistente@email.com")).thenReturn(false);

        // When
        boolean existe1 = repository.existsByEmail("joao@email.com");
        boolean existe2 = repository.existsByEmail("inexistente@email.com");

        // Then
        assertTrue(existe1);
        assertFalse(existe2);
        verify(jpaRepository).existsByEmail("joao@email.com");
        verify(jpaRepository).existsByEmail("inexistente@email.com");
    }

    @Test
    void deveVerificarSeExistePorLogin() {
        // Given
        when(jpaRepository.existsByLogin("joao123")).thenReturn(true);
        when(jpaRepository.existsByLogin("inexistente")).thenReturn(false);

        // When
        boolean existe1 = repository.existsByLogin("joao123");
        boolean existe2 = repository.existsByLogin("inexistente");

        // Then
        assertTrue(existe1);
        assertFalse(existe2);
        verify(jpaRepository).existsByLogin("joao123");
        verify(jpaRepository).existsByLogin("inexistente");
    }

    @Test
    void deveListarUsuariosPorAtivo() {
        // Given
        UsuarioEntity usuario2 = new UsuarioEntity();
        usuario2.setId(2L);
        usuario2.setNome("Maria Silva");
        usuario2.setEmail("maria@email.com");
        usuario2.setLogin("maria123");
        usuario2.setSenha("senha456");
        usuario2.setAtivo(true);

        List<UsuarioEntity> entities = Arrays.asList(usuarioEntity, usuario2);
        when(jpaRepository.findByAtivo(true, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(entities);

        // When
        List<Usuario> resultado = repository.findByAtivo(true);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(Usuario::isAtivo));
        verify(jpaRepository).findByAtivo(true, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Test
    void deveListarUsuariosInativos() {
        // Given
        UsuarioEntity usuarioInativo = new UsuarioEntity();
        usuarioInativo.setId(3L);
        usuarioInativo.setNome("Pedro Silva");
        usuarioInativo.setEmail("pedro@email.com");
        usuarioInativo.setLogin("pedro123");
        usuarioInativo.setSenha("senha789");
        usuarioInativo.setAtivo(false);

        List<UsuarioEntity> entities = Arrays.asList(usuarioInativo);
        when(jpaRepository.findByAtivo(false, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(entities);

        // When
        List<Usuario> resultado = repository.findByAtivo(false);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).isAtivo());
        verify(jpaRepository).findByAtivo(false, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Test
    void deveEncontrarUsuarioPorLogin() {
        // Given
        when(jpaRepository.findByLogin("joao123")).thenReturn(Optional.of(usuarioEntity));

        // When
        Optional<Usuario> resultado = repository.findByLogin("joao123");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(usuario.getLogin(), resultado.get().getLogin());
        assertEquals(usuario.getEmail(), resultado.get().getEmail());
        verify(jpaRepository).findByLogin("joao123");
    }

    @Test
    void deveRetornarEmptyQuandoUsuarioNaoEncontradoPorLogin() {
        // Given
        when(jpaRepository.findByLogin("inexistente")).thenReturn(Optional.empty());

        // When
        Optional<Usuario> resultado = repository.findByLogin("inexistente");

        // Then
        assertFalse(resultado.isPresent());
        verify(jpaRepository).findByLogin("inexistente");
    }

    @Test
    void deveDeletarTodosOsUsuarios() {
        // Given
        doNothing().when(jpaRepository).deleteAll();

        // When
        repository.deleteAll();

        // Then
        verify(jpaRepository).deleteAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNenhumUsuarioEncontrado() {
        // Given
        when(jpaRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Usuario> resultado = repository.findAll();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNenhumUsuarioAtivoEncontrado() {
        // Given
        when(jpaRepository.findByAtivo(true, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(Arrays.asList());

        // When
        List<Usuario> resultado = repository.findByAtivo(true);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findByAtivo(true, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Test
    void deveRetornarListaVaziaQuandoNenhumUsuarioInativoEncontrado() {
        // Given
        when(jpaRepository.findByAtivo(false, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(Arrays.asList());

        // When
        List<Usuario> resultado = repository.findByAtivo(false);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findByAtivo(false, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Test
    void deveSalvarUsuarioComDadosCompletos() {
        // Given
        Usuario usuarioCompleto = new Usuario();
        usuarioCompleto.setId(null); // Novo usuário
        usuarioCompleto.setNome("Novo Usuário");
        usuarioCompleto.setEmail("novo@email.com");
        usuarioCompleto.setLogin("novo123");
        usuarioCompleto.setSenha("senha123");
        usuarioCompleto.setAtivo(true);
        usuarioCompleto.setDataCriacao(LocalDateTime.now());
        usuarioCompleto.setDataAtualizacao(LocalDateTime.now());

        UsuarioEntity entitySalvo = new UsuarioEntity();
        entitySalvo.setId(5L); // ID gerado
        entitySalvo.setNome("Novo Usuário");
        entitySalvo.setEmail("novo@email.com");
        entitySalvo.setLogin("novo123");
        entitySalvo.setSenha("senha123");
        entitySalvo.setAtivo(true);

        when(jpaRepository.save(any(UsuarioEntity.class))).thenReturn(entitySalvo);

        // When
        Usuario resultado = repository.save(usuarioCompleto);

        // Then
        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals("Novo Usuário", resultado.getNome());
        assertEquals("novo@email.com", resultado.getEmail());
        assertEquals("novo123", resultado.getLogin());
        assertTrue(resultado.isAtivo());
        verify(jpaRepository).save(any(UsuarioEntity.class));
    }

    @Test
    void deveSalvarUsuarioComDadosMinimos() {
        // Given
        Usuario usuarioMinimo = new Usuario();
        usuarioMinimo.setNome("Usuário Mínimo");
        usuarioMinimo.setEmail("minimo@email.com");
        usuarioMinimo.setLogin("minimo123");
        usuarioMinimo.setSenha("senha123");

        UsuarioEntity entitySalvo = new UsuarioEntity();
        entitySalvo.setId(6L);
        entitySalvo.setNome("Usuário Mínimo");
        entitySalvo.setEmail("minimo@email.com");
        entitySalvo.setLogin("minimo123");
        entitySalvo.setSenha("senha123");
        entitySalvo.setAtivo(true);

        when(jpaRepository.save(any(UsuarioEntity.class))).thenReturn(entitySalvo);

        // When
        Usuario resultado = repository.save(usuarioMinimo);

        // Then
        assertNotNull(resultado);
        assertEquals(6L, resultado.getId());
        assertEquals("Usuário Mínimo", resultado.getNome());
        assertEquals("minimo@email.com", resultado.getEmail());
        assertEquals("minimo123", resultado.getLogin());
        assertTrue(resultado.isAtivo()); // Valor padrão
        verify(jpaRepository).save(any(UsuarioEntity.class));
    }
} 