package com.seminario.ms_usuarios.config;

import com.seminario.ms_usuarios.service.UbicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UbicacionService ubicacionService;

    @Override
    public void run(String... args) throws Exception {
        ubicacionService.cargarDatosGeograficos();
    }
}