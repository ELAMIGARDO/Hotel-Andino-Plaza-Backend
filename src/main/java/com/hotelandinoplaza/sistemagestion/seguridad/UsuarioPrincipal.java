package com.hotelandinoplaza.sistemagestion.seguridad;

import com.hotelandinoplaza.sistemagestion.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UsuarioPrincipal implements UserDetails {

    private final Usuario usuario;

    public UsuarioPrincipal(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombreCompleto() {
        return usuario.getNombre();
    }

    public String getRol() {
        return usuario.getRol();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String rolLimpio = usuario.getRol().toUpperCase().trim();

        // Si por algún motivo ya traía el prefijo "ROLE_", lo limpiamos para no duplicarlo
        if (rolLimpio.startsWith("ROLE_")) {
            rolLimpio = rolLimpio.replace("ROLE_", "");
        }

        // Forzamos el formato estándar estricto que requiere .hasRole()
        String roleName = "ROLE_" + rolLimpio;

        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }
}