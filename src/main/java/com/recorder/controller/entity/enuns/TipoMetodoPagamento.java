package com.recorder.controller.entity.enuns;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public enum TipoMetodoPagamento {
    CARTAO_CREDITO("Cartão de Crédito", "Pagamento com cartão de crédito", true),
    CARTAO_DEBITO("Cartão de Débito", "Pagamento com cartão de débito", true),
    PIX("PIX", "Pagamento instantâneo via PIX", true),
    BOLETO("Boleto", "Pagamento via boleto bancário", false),
    DINHEIRO("Dinheiro", "Pagamento em espécie", true),
    TRANSFERENCIA("Transferência", "Transferência bancária", false),
    PARCELADO("Parcelado", "Pagamento parcelado", false);

    private static final Set<String> METODOS_VALIDOS = Arrays.stream(values())
            .map(TipoMetodoPagamento::name)
            .collect(Collectors.toSet());

    private final String descricao;
    private final String detalhes;
    private final boolean pagamentoInstantaneo;

    TipoMetodoPagamento(String descricao, String detalhes, boolean pagamentoInstantaneo) {
        this.descricao = descricao;
        this.detalhes = detalhes;
        this.pagamentoInstantaneo = pagamentoInstantaneo;
    }

    // Getters
    public String getDescricao() {
        return descricao;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public boolean isPagamentoInstantaneo() {
        return pagamentoInstantaneo;
    }

    // Conversão de String para enum
    public static TipoMetodoPagamento fromString(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Método de pagamento não pode ser nulo ou vazio");
        }

        String normalized = texto.trim().toUpperCase(Locale.ROOT).replace(" ", "_");

        for (TipoMetodoPagamento metodo : values()) {
            if (metodo.name().equals(normalized) ||
                    metodo.descricao.equalsIgnoreCase(texto)) {
                return metodo;
            }
        }

        throw new IllegalArgumentException(String.format(
                "Método de pagamento '%s' inválido. Valores permitidos: %s",
                texto,
                String.join(", ", METODOS_VALIDOS)));
    }

    // Validação
    public static boolean isValid(String texto) {
        try {
            fromString(texto);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // Métodos utilitários
    public static Set<String> getMetodosInstantaneos() {
        return Arrays.stream(values())
                .filter(TipoMetodoPagamento::isPagamentoInstantaneo)
                .map(TipoMetodoPagamento::name)
                .collect(Collectors.toSet());
    }

    public static Set<String> getTodosMetodos() {
        return METODOS_VALIDOS;
    }
}