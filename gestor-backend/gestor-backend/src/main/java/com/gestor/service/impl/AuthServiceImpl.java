// impl/AuthServiceImpl.java
package com.gestor.service.impl;

import com.gestor.entity.Usuario;
import com.gestor.repository.UsuarioRepository;
import com.gestor.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UsuarioRepository usuarioRepository;
    public AuthServiceImpl(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public String login(String username, String password) {
        // Login simple: si existe el usuario, devolvemos token dummy
        // (tu frontend sólo guarda el token; no validamos JWT aquí)
        Usuario u = usuarioRepository.findByUsuario(username).orElse(null);
        if(u == null) return null;
        return "dummy-token-" + username;
    }
}
