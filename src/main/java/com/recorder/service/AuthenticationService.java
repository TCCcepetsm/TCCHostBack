package com.recorder.service;

import com.recorder.controller.entity.Usuario;
import com.recorder.exception.AuthenticationException;
import com.recorder.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UsuarioRepository usuarioRepository;

    public Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Tentativa de acesso sem autenticação");
            throw new AuthenticationException(
                    "Acesso não autorizado",
                    HttpStatus.UNAUTHORIZED);
        }

        String email = authentication.getName();
        logger.debug("Buscando usuário autenticado: {}", email);

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Usuário não encontrado no banco: {}", email);
                    return new AuthenticationException(
                            "Credenciais inválidas",
                            HttpStatus.FORBIDDEN);
                });
    }

    // Novo método otimizado para PostgreSQL
    public Usuario getUsuarioComRoles() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return usuarioRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new AuthenticationException(
                        "Usuário não encontrado",
                        HttpStatus.NOT_FOUND));
    }
}