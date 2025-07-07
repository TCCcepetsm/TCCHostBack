package com.recorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // Para habilitar processamento assíncrono
public class RecorderSrcApplication {
    public static void main(String[] args) {
        SpringApplication.run(RecorderSrcApplication.class, args);
    }
}