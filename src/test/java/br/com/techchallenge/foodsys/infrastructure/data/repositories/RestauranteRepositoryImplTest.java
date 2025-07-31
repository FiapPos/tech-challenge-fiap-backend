package br.com.techchallenge.foodsys.infrastructure.data.repositories;

import br.com.techchallenge.foodsys.core.domain.entities.Restaurante;
import br.com.techchallenge.foodsys.infrastructure.data.entities.RestauranteEntity;
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
class RestauranteRepositoryImplTest {

    @Mock
    private RestauranteJpaRepository jpaRepository;

    @InjectMocks
    private RestauranteRepositoryImpl repository;

    private Restaurante restaurante;
    private RestauranteEntity restauranteEntity;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setHorarioAbertura("08:00");
        restaurante.setHorarioFechamento("22:00");
        restaurante.setAtivo(true);
        restaurante.setUsuarioId(1L);
        restaurante.setDataCriacao(LocalDateTime.now());
        restaurante.setDataAtualizacao(LocalDateTime.now());

        restauranteEntity = new RestauranteEntity();
        restauranteEntity.setId(1L);
        restauranteEntity.setNome("Restaurante Teste");
        restauranteEntity.setTipoCozinha("Italiana");
        restauranteEntity.setHorarioAbertura("08:00");
        restauranteEntity.setHorarioFechamento("22:00");
        restauranteEntity.setAtivo(true);
        restauranteEntity.setUsuarioId(1L);
        restauranteEntity.setDataCriacao(LocalDateTime.now());
        restauranteEntity.setDataAtualizacao(LocalDateTime.now());
    }

    @Test
    void deveSalvarRestauranteComSucesso() {
        // Given
        when(jpaRepository.save(any(RestauranteEntity.class))).thenReturn(restauranteEntity);

        // When
        Restaurante resultado = repository.save(restaurante);

        // Then
        assertNotNull(resultado);
        assertEquals(restaurante.getId(), resultado.getId());
        assertEquals(restaurante.getNome(), resultado.getNome());
        verify(jpaRepository).save(any(RestauranteEntity.class));
    }

    @Test
    void deveEncontrarRestaurantePorId() {
        // Given
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(restauranteEntity));

        // When
        Optional<Restaurante> resultado = repository.findById(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(restaurante.getId(), resultado.get().getId());
        assertEquals(restaurante.getNome(), resultado.get().getNome());
        verify(jpaRepository).findById(1L);
    }

    @Test
    void deveRetornarEmptyQuandoRestauranteNaoEncontrado() {
        // Given
        when(jpaRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Restaurante> resultado = repository.findById(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(jpaRepository).findById(999L);
    }

    @Test
    void deveListarTodosOsRestaurantes() {
        // Given
        RestauranteEntity restaurante2 = new RestauranteEntity();
        restaurante2.setId(2L);
        restaurante2.setNome("Restaurante 2");
        restaurante2.setTipoCozinha("Japonesa");
        restaurante2.setAtivo(true);

        List<RestauranteEntity> entities = Arrays.asList(restauranteEntity, restaurante2);
        when(jpaRepository.findAll()).thenReturn(entities);

        // When
        List<Restaurante> resultado = repository.findAll();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(restaurante.getId(), resultado.get(0).getId());
        assertEquals(restaurante2.getId(), resultado.get(1).getId());
        verify(jpaRepository).findAll();
    }

    @Test
    void deveDeletarRestaurantePorId() {
        // Given
        doNothing().when(jpaRepository).deleteById(1L);

        // When
        repository.deleteById(1L);

        // Then
        verify(jpaRepository).deleteById(1L);
    }

    @Test
    void deveEncontrarRestaurantePorNome() {
        // Given
        when(jpaRepository.findRestauranteByNome("Restaurante Teste")).thenReturn(restauranteEntity);

        // When
        Restaurante resultado = repository.findRestauranteByNome("Restaurante Teste");

        // Then
        assertNotNull(resultado);
        assertEquals(restaurante.getNome(), resultado.getNome());
        verify(jpaRepository).findRestauranteByNome("Restaurante Teste");
    }

    @Test
    void deveRetornarNullQuandoRestauranteNaoEncontradoPorNome() {
        // Given
        when(jpaRepository.findRestauranteByNome("Restaurante Inexistente")).thenReturn(null);

        // When
        Restaurante resultado = repository.findRestauranteByNome("Restaurante Inexistente");

        // Then
        assertNull(resultado);
        verify(jpaRepository).findRestauranteByNome("Restaurante Inexistente");
    }

    @Test
    void deveEncontrarRestaurantePorUsuarioId() {
        // Given
        when(jpaRepository.findByUsuarioId(1L, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(restauranteEntity);

        // When
        Restaurante resultado = repository.findByUsuarioId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(restaurante.getUsuarioId(), resultado.getUsuarioId());
        verify(jpaRepository).findByUsuarioId(1L, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Test
    void deveRetornarNullQuandoRestauranteNaoEncontradoPorUsuarioId() {
        // Given
        when(jpaRepository.findByUsuarioId(999L, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(null);

        // When
        Restaurante resultado = repository.findByUsuarioId(999L);

        // Then
        assertNull(resultado);
        verify(jpaRepository).findByUsuarioId(999L, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Test
    void deveListarRestaurantesPorAtivo() {
        // Given
        RestauranteEntity restaurante2 = new RestauranteEntity();
        restaurante2.setId(2L);
        restaurante2.setNome("Restaurante 2");
        restaurante2.setAtivo(true);

        List<RestauranteEntity> entities = Arrays.asList(restauranteEntity, restaurante2);
        when(jpaRepository.findByAtivo(true, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(entities);

        // When
        List<Restaurante> resultado = repository.findByAtivo(true);

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(Restaurante::isAtivo));
        verify(jpaRepository).findByAtivo(true, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Test
    void deveListarRestaurantesPorTipoCozinha() {
        // Given
        RestauranteEntity restaurante2 = new RestauranteEntity();
        restaurante2.setId(2L);
        restaurante2.setNome("Restaurante 2");
        restaurante2.setTipoCozinha("Italiana");

        List<RestauranteEntity> entities = Arrays.asList(restauranteEntity, restaurante2);
        when(jpaRepository.findByTipoCozinha("Italiana")).thenReturn(entities);

        // When
        List<Restaurante> resultado = repository.findByTipoCozinha("Italiana");

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(r -> "Italiana".equals(r.getTipoCozinha())));
        verify(jpaRepository).findByTipoCozinha("Italiana");
    }

    @Test
    void deveListarRestaurantesPorAtivoETipoCozinha() {
        // Given
        RestauranteEntity restaurante2 = new RestauranteEntity();
        restaurante2.setId(2L);
        restaurante2.setNome("Restaurante 2");
        restaurante2.setTipoCozinha("Italiana");
        restaurante2.setAtivo(true);

        List<RestauranteEntity> entities = Arrays.asList(restauranteEntity, restaurante2);
        when(jpaRepository.findByAtivoAndTipoCozinha(true, "Italiana")).thenReturn(entities);

        // When
        List<Restaurante> resultado = repository.findByAtivoAndTipoCozinha(true, "Italiana");

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(r -> r.isAtivo() && "Italiana".equals(r.getTipoCozinha())));
        verify(jpaRepository).findByAtivoAndTipoCozinha(true, "Italiana");
    }

    @Test
    void deveVerificarSeExistePorUsuarioId() {
        // Given
        when(jpaRepository.existsByUsuarioId(1L)).thenReturn(true);
        when(jpaRepository.existsByUsuarioId(999L)).thenReturn(false);

        // When
        boolean existe1 = repository.existsByUsuarioId(1L);
        boolean existe2 = repository.existsByUsuarioId(999L);

        // Then
        assertTrue(existe1);
        assertFalse(existe2);
        verify(jpaRepository).existsByUsuarioId(1L);
        verify(jpaRepository).existsByUsuarioId(999L);
    }

    @Test
    void deveVerificarSeExistePorId() {
        // Given
        when(jpaRepository.existsById(1L)).thenReturn(true);
        when(jpaRepository.existsById(999L)).thenReturn(false);

        // When
        boolean existe1 = repository.existsById(1L);
        boolean existe2 = repository.existsById(999L);

        // Then
        assertTrue(existe1);
        assertFalse(existe2);
        verify(jpaRepository).existsById(1L);
        verify(jpaRepository).existsById(999L);
    }

    @Test
    void deveDeletarTodosOsRestaurantes() {
        // Given
        doNothing().when(jpaRepository).deleteAll();

        // When
        repository.deleteAll();

        // Then
        verify(jpaRepository).deleteAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNenhumRestauranteEncontrado() {
        // Given
        when(jpaRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Restaurante> resultado = repository.findAll();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNenhumRestauranteAtivoEncontrado() {
        // Given
        when(jpaRepository.findByAtivo(true, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(Arrays.asList());

        // When
        List<Restaurante> resultado = repository.findByAtivo(true);

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findByAtivo(true, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Test
    void deveRetornarListaVaziaQuandoNenhumRestaurantePorTipoCozinhaEncontrado() {
        // Given
        when(jpaRepository.findByTipoCozinha("Inexistente")).thenReturn(Arrays.asList());

        // When
        List<Restaurante> resultado = repository.findByTipoCozinha("Inexistente");

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(jpaRepository).findByTipoCozinha("Inexistente");
    }
} 