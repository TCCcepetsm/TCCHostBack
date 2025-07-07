package com.recorder.controller.entity.enuns;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public enum StatusAgendamento {
    PENDENTE("Pendente", "Agendamento aguardando confirmação", false),
    CONFIRMADO("Confirmado", "Agendamento confirmado e válido", true),
    RECUSADO("Recusado", "Agendamento recusado ou cancelado", false);

    private static final Set<String> VALID_STATUS = Arrays.stream(values())
            .map(StatusAgendamento::name)
            .collect(Collectors.toSet());

    private final String descricao;
    private final String detalhes;
    private final boolean statusPositivo;

    StatusAgendamento(String descricao, String detalhes, boolean statusPositivo) {
        this.descricao = descricao;
        this.detalhes = detalhes;
        this.statusPositivo = statusPositivo;
    }

    // Getters
    public String getDescricao() {
        return descricao;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public boolean isStatusPositivo() {
        return statusPositivo;
    }

    // Métodos de negócio
    public boolean permiteAlteracao() {
        return this == PENDENTE;
    }

    // Conversão e validação
    public static StatusAgendamento fromString(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Status não pode ser nulo ou vazio");
        }

        String normalized = text.trim().toUpperCase(Locale.ROOT);

        for (StatusAgendamento status : values()) {
            if (status.name().equals(normalized) ||
                    status.descricao.equalsIgnoreCase(text)) {
                return status;
            }
        }

        throw new IllegalArgumentException(String.format(
                "Status '%s' inválido. Valores permitidos: %s",
                text,
                String.join(", ", VALID_STATUS)));
    }

    public static boolean isValid(String text) {
        try {
            fromString(text);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static Set<String> getValidStatusNames() {
        return VALID_STATUS;
    }
}