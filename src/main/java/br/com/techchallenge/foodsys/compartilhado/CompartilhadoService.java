package br.com.techchallenge.foodsys.compartilhado;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class CompartilhadoService {

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
