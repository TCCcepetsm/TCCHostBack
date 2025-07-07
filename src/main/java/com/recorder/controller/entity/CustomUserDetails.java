package com.recorder.controller.entity;

import com.recorder.controller.entity.enuns.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private static final String ROLE_PREFIX = "ROLE_";
    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        Objects.requireNonNull(usuario, "Usuário não pode ser nulo");
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.getRoles().stream()
                .map(role -> {
                    String roleName = role.name();
                    // Garante que o papel tenha o prefixo ROLE_
                    return new SimpleGrantedAuthority(
                            roleName.startsWith(ROLE_PREFIX) ? roleName : ROLE_PREFIX + roleName);
                })
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return usuario.getSenha(); // Ou getPassword() dependendo do seu campo
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !usuario.isExpirado(); // Implementar método na classe Usuario se necessário
    }

    @Override
    public boolean isAccountNonLocked() {
        return !usuario.isBloqueado(); // Implementar método na classe Usuario se necessário
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Ou implementar lógica específica
    }

    @Override
    public boolean isEnabled() {
        return usuario.isAtivo(); // Implementar método na classe Usuario
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CustomUserDetails))
            return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(getUsername(), that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }
}