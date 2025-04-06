package br.com.techchallenge.foodsys.shared;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class SharedService {

    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
