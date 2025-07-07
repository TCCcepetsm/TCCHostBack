package com.recorder.controller.entity.enuns;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public enum TipoStatusPagamento {
    PENDENTE("Pendente", "Pagamento aguardando processamento", false),
    CONFIRMADO("Confirmado", "Pagamento aprovado e concluído", true),
    CANCELADO("Cancelado", "Pagamento cancelado ou rejeitado", false),
    REEMBOLSADO("Reembolsado", "Valor devolvido ao cliente", false),
    PROCESSANDO("Processando", "Pagamento em processamento", false);

    private static final Set<String> STATUS_VALIDOS = Arrays.stream(values())
            .map(TipoStatusPagamento::name)
            .collect(Collectors.toSet());

    private final String descricao;
    private final String detalhes;
    private final boolean pagamentoEfetivado;

    TipoStatusPagamento(String descricao, String detalhes, boolean pagamentoEfetivado) {
        this.descricao = descricao;
        this.detalhes = detalhes;
        this.pagamentoEfetivado = pagamentoEfetivado;
    }

    // Getters
    public String getDescricao() {
        return descricao;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public boolean isPagamentoEfetivado() {
        return pagamentoEfetivado;
    }

    // Métodos utilitários
    public static TipoStatusPagamento fromString(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Status de pagamento não pode ser nulo ou vazio");
        }

        String normalized = texto.trim().toUpperCase(Locale.ROOT);

        for (TipoStatusPagamento status : values()) {
            if (status.name().equals(normalized) ||
                    status.descricao.equalsIgnoreCase(texto)) {
                return status;
            }
        }

        throw new IllegalArgumentException(String.format(
                "Status de pagamento '%s' inválido. Valores permitidos: %s",
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

    public static Set<String> getStatusFinais() {
        return Arrays.stream(values())
                .filter(status -> status.pagamentoEfetivado ||
                        status == CANCELADO ||
                        status == REEMBOLSADO)
                .map(TipoStatusPagamento::name)
                .collect(Collectors.toSet());
    }
}