package br.com.techchallenge.foodsys.comandos.restaurante;

import br.com.techchallenge.foodsys.compartilhado.CompartilhadoService;
import br.com.techchallenge.foodsys.dominio.restaurante.Restaurante;
import br.com.techchallenge.foodsys.dominio.restaurante.RestauranteRepository;
import br.com.techchallenge.foodsys.excpetion.BadRequestException;
import br.com.techchallenge.foodsys.utils.ValidarRestauranteExistente;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DesativarRestauranteComandoTest {

    @Mock
    private RestauranteRepository restauranteRepository;
    @Mock
    private ValidarRestauranteExistente validarRestauranteExistente;
    @Mock
    private CompartilhadoService sharedService;
    @InjectMocks
    private DesativarRestauranteComando comando;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }





}
