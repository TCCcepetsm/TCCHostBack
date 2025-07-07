package com.recorder.controller.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    // Se o seu enum Roles.getAuthority() já retorna a string "ROLE_NOME_DA_ROLE",
    // a variável ROLE_PREFIX aqui é desnecessária.
    // private static final String ROLE_PREFIX = "ROLE_";

    private final Usuario usuario;

    public UserPrincipal(Usuario usuario) {
        Objects.requireNonNull(usuario, "Usuário não pode ser nulo");
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // CORREÇÃO: Chame getAuthority() diretamente no enum 'role'.
        // Assumimos que role.getAuthority() já retorna a string no formato exigido pelo
        // Spring Security (ex: "ROLE_ADMIN").
        return usuario.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return usuario.getSenha(); // Delega para o método getSenha() do Usuario
    }

    @Override
    public String getUsername() {
        return usuario.getEmail(); // Delega para o método getEmail() do Usuario
    }

    // Métodos de acesso para detalhes do usuário, se necessário
    public Long getId() {
        return usuario.getId();
    }

    public String getNome() {
        return usuario.getNome();
    }

    // CORREÇÃO: Delega para os métodos isAccountNonExpired(), isAccountNonLocked(),
    // isCredentialsNonExpired() e isEnabled() que já existem na sua entidade
    // Usuario
    // (já que Usuario implementa UserDetails e você já definiu esses métodos lá).

    @Override
    public boolean isAccountNonExpired() {
        return usuario.isAccountNonExpired(); // Delega para o Usuario
    }

    @Override
    public boolean isAccountNonLocked() {
        return usuario.isAccountNonLocked(); // Delega para o Usuario
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return usuario.isCredentialsNonExpired(); // Delega para o Usuario
    }

    @Override
    public boolean isEnabled() {
        return usuario.isEnabled(); // Delega para o Usuario
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserPrincipal))
            return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(getUsername(), that.getUsername()); // Compara pelo username (email)
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