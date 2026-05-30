package com.arquitectura.motor_decisiones.config;

import com.arquitectura.motor_decisiones.entity.Usuario;
import com.arquitectura.motor_decisiones.repository.UsuarioRepository;
import com.arquitectura.motor_decisiones.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }
    //0 Este metodo es la puerta principal, recibe la peticion, y decide si la deja continuar por la cadena de filtros
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,//El filtro recibe esa petición en request
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain//Cadena de filtros
    ) throws ServletException, IOException {

        // 1. Estas líneas preparan y extraen la información inicial necesaria para identificar al usuario mediante JWT.
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        // 1. Ver si el header llega
        System.out.println("HEADER AUTHORIZATION: " + authHeader);

        // 2. Si no hay header o no empieza con "Bearer ", lo dejamos pasar.
        // (Spring Security lo bloqueará más adelante si la ruta es privada)
        //2 mio Este bloque revisa si la petición trae un JWT con formato correcto y, si falta, permite que la petición siga su flujo normal para que Spring Security tome la decisión final.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraemos el token (cortamos los primeros 7 caracteres: "Bearer ")
        jwt = authHeader.substring(7);

        // 4. Extraemos el email desde el token
        userEmail = jwtService.extractUsername(jwt);

        System.out.println("USUARIO EXTRAÍDO DEL TOKEN: " + userEmail);
        // 5. Si hay un email y el usuario aún no está autenticado en el contexto actual
        // 5. MIO Este bloque valida el JWT, verifica al usuario en la base de datos y registra oficialmente al usuario autenticado dentro de Spring Security.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Buscamos al usuario en la base de datos
            Usuario usuario = this.usuarioRepository.findByEmail(userEmail).orElse(null);

            // 6. Si el usuario existe y el token es válido criptográficamente
            if (usuario != null && jwtService.isTokenValid(jwt, usuario)) {

                // Creamos un "Gafete Oficial" de Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        null // Aquí irían los roles/autorizaciones, lo agregaremos después
                );
                //Agrega detalles extra del request: Por ejemplo: IP, sesión, origen
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 7. Le decimos a Spring Security: "Conozco a este tipo, déjalo pasar"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 8. Continuar con el resto de la cadena de filtros
        filterChain.doFilter(request, response);
    }
}