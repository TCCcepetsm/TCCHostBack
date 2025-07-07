// src/main/java/com/recorder/controller/entity/enuns/Roles.java
package com.recorder.controller.entity.enuns;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Enum que representa os tipos de roles/perfis de usuário no sistema.
 * Segue o padrão do Spring Security (ROLE_*) e fornece métodos utilitários.
 */
public enum Roles {

    ROLE_USUARIO("ROLE_USUARIO", "Usuário comum do sistema"),
    ROLE_ADMIN("ROLE_ADMIN", "Administrador com acesso total"),
    ROLE_PROFISSIONAL("ROLE_PROFISSIONAL", "Profissional com acesso especializado");

    private static final String ROLE_PREFIX = "ROLE_";
    private final String authority;
    private final String descricao;

    Roles(String authority, String descricao) {
        if (!authority.startsWith(ROLE_PREFIX)) {
            throw new IllegalArgumentException("Toda role deve começar com " + ROLE_PREFIX);
        }
        this.authority = authority;
        this.descricao = descricao;
    }

    public String getAuthority() {
        return authority;
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Retorna o nome da role sem o prefixo "ROLE_".
     * Ex: "ROLE_ADMIN" -> "ADMIN"
     */
    public String getRoleSemPrefix() {
        return authority.substring(ROLE_PREFIX.length());
    }

    /**
     * Converte uma string de autoridade (com ou sem "ROLE_" e case-insensitive)
     * para o enum Roles correspondente.
     * 
     * @param authority A string da autoridade (ex: "admin", "ROLE_USUARIO").
     * @return O enum Roles correspondente.
     * @throws IllegalArgumentException Se a autoridade não for válida ou não
     *                                  encontrada.
     */
    public static Roles fromAuthority(String authority) {
        if (authority == null || authority.isBlank()) {
            throw new IllegalArgumentException("Authority não pode ser nula ou vazia");
        }

        String normalized = authority.toUpperCase(Locale.ROOT);
        if (!normalized.startsWith(ROLE_PREFIX)) {
            normalized = ROLE_PREFIX + normalized;
        }

        for (Roles role : values()) {
            if (role.authority.equals(normalized)) {
                return role;
            }
        }

        throw new IllegalArgumentException(String.format(
                "Authority '%s' inválida. Valores permitidos: %s",
                authority,
                Arrays.stream(values())
                        .map(Roles::getAuthority)
                        .collect(Collectors.joining(", "))));
    }

    /**
     * Verifica se uma string de autoridade é um valor válido de Roles.
     * 
     * @param authority A string da autoridade.
     * @return true se a autoridade for válida, false caso contrário.
     */
    public static boolean isValid(String authority) {
        try {
            fromAuthority(authority);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}