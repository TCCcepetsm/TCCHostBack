package com.recorder.controller.entity.enuns;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public enum TipoMidia {
    FOTO("Foto", "image/*", Set.of("jpg", "jpeg", "png", "gif")),
    VIDEO("Vídeo", "video/*", Set.of("mp4", "mov", "avi", "mkv")),
    AUDIO("Áudio", "audio/*", Set.of("mp3", "wav", "ogg", "aac")),
    DOCUMENTO("Documento", "application/*", Set.of("pdf", "docx", "txt"));

    private static final Set<String> EXTENSOES_VALIDAS = Arrays.stream(values())
            .flatMap(tipo -> tipo.extensoesPermitidas.stream())
            .collect(Collectors.toSet());

    private final String descricao;
    private final String mimeType;
    private final Set<String> extensoesPermitidas;

    TipoMidia(String descricao, String mimeType, Set<String> extensoesPermitidas) {
        this.descricao = descricao;
        this.mimeType = mimeType;
        this.extensoesPermitidas = extensoesPermitidas;
    }

    // Getters
    public String getDescricao() {
        return descricao;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Set<String> getExtensoesPermitidas() {
        return extensoesPermitidas;
    }

    // Métodos utilitários
    public static TipoMidia fromExtensao(String extensao) {
        if (extensao == null || extensao.isBlank()) {
            throw new IllegalArgumentException("Extensão não pode ser nula ou vazia");
        }

        String ext = extensao.trim().toLowerCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(tipo -> tipo.extensoesPermitidas.contains(ext))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Extensão '" + extensao + "' não é suportada. Extensões válidas: " +
                                String.join(", ", EXTENSOES_VALIDAS)));
    }

    public static TipoMidia fromMimeType(String mimeType) {
        if (mimeType == null || mimeType.isBlank()) {
            throw new IllegalArgumentException("MIME type não pode ser nulo ou vazio");
        }

        String mime = mimeType.trim().toLowerCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(tipo -> mime.startsWith(tipo.mimeType.replace("*", "")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tipo MIME '" + mimeType + "' não é suportado"));
    }

    public static boolean isExtensaoValida(String extensao) {
        try {
            fromExtensao(extensao);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}