package com.recorder.controller.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private static final String ROLE_PREFIX = "ROLE_";
    private final Usuario usuario;

    public UserPrincipal(Usuario usuario) {
        Objects.requireNonNull(usuario, "Usuário não pode ser nulo");
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(
                        role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role))
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

    public Long getId() {
        return usuario.getId();
    }

    public String getNome() {
        return usuario.getNome();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !usuario.isContaExpirada();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !usuario.isContaBloqueada();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !usuario.isCredencialExpirada();
    }

    @Override
    public boolean isEnabled() {
        return usuario.isAtivo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserPrincipal))
            return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(getUsername(), that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "username='" + getUsername() + '\'' +
                ", authorities=" + getAuthorities() +
                '}';
    }
}