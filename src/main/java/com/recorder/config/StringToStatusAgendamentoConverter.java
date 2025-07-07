package com.recorder.config;

import com.recorder.controller.entity.enuns.StatusAgendamento;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class StringToStatusAgendamentoConverter implements Converter<String, StatusAgendamento> {

    @Override
    public StatusAgendamento convert(String source) {
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("Status de agendamento não pode ser nulo ou vazio");
        }

        try {
            return StatusAgendamento.valueOf(source.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            String valoresAceitos = Arrays.stream(StatusAgendamento.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            throw new IllegalArgumentException(
                    String.format("Status de agendamento inválido: '%s'. Valores aceitos: %s",
                            source, valoresAceitos),
                    e);
        }
    }
}