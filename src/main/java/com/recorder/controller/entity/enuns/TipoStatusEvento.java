package com.recorder.controller.entity.enuns;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public enum TipoStatusEvento {
    // AGENDADO("Agendado", "Evento agendado mas não confirmado", false),
    CONFIRMADO("Confirmado", "Evento confirmado e ativo", true),
    CONCLUIDO("Concluído", "Evento finalizado com sucesso", true),
    CANCELADO("Cancelado", "Evento cancelado", false),
    EM_ANDAMENTO("Em Andamento", "Evento atualmente em progresso", true);

    private static final Set<String> STATUS_VALIDOS = Arrays.stream(values())
            .map(TipoStatusEvento::name)
            .collect(Collectors.toSet());

    private final String descricao;
    private final String detalhes;
    private final boolean statusAtivo;

    TipoStatusEvento(String descricao, String detalhes, boolean statusAtivo) {
        this.descricao = descricao;
        this.detalhes = detalhes;
        this.statusAtivo = statusAtivo;
    }

    // Getters
    public String getDescricao() {
        return descricao;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public boolean isStatusAtivo() {
        return statusAtivo;
    }

    // Métodos utilitários
    public static TipoStatusEvento fromString(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Status não pode ser nulo ou vazio");
        }

        String normalized = texto.trim().toUpperCase(Locale.ROOT).replace(" ", "_");

        for (TipoStatusEvento status : values()) {
            if (status.name().equals(normalized) ||
                    status.descricao.equalsIgnoreCase(texto)) {
                return status;
            }
        }

        throw new IllegalArgumentException(String.format(
                "Status '%s' inválido. Valores permitidos: %s",
                texto,
                String.join(", ", STATUS_VALIDOS)));
    }

    public static boolean isValid(String texto) {
        try {
            fromString(texto);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static Set<String> getStatusAtivos() {
        return Arrays.stream(values())
                .filter(TipoStatusEvento::isStatusAtivo)
                .map(TipoStatusEvento::name)
                .collect(Collectors.toSet());
    }
}