package com.recorder.controller.entity;

import com.recorder.controller.entity.enuns.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
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
                    String roleName = role.name(); // Pega o nome do enum (ex: "ADMIN", "USUARIO")
                    // Garante que o papel tenha o prefixo ROLE_ exigido pelo Spring Security
                    return new SimpleGrantedAuthority(
                            roleName.startsWith(ROLE_PREFIX) ? roleName : ROLE_PREFIX + roleName);
                })
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        // CORREÇÃO: Delega para o método isAccountNonExpired() existente em Usuario
        return usuario.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        // CORREÇÃO: Delega para o método isAccountNonLocked() existente em Usuario
        return usuario.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // CORREÇÃO: Delega para o método isCredentialsNonExpired() existente em Usuario
        return usuario.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        // CORREÇÃO: Delega para o método isEnabled() existente em Usuario
        return usuario.isEnabled();
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