package com.camacho.proyecto.freetable.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    // Páginas públicas
                    .requestMatchers("/home", "/about", "/contact", "/public/**").permitAll()
                    .requestMatchers("/auth/register", "/auth/login").permitAll()  // Páginas de login y registro públicas
                    // Permitir el acceso a recursos estáticos sin autenticación
                    .requestMatchers("/img/**", "/css/**", "/js/**").permitAll()  // Permite el acceso a recursos estáticos
                    // Rutas protegidas por roles
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/gestor/**").hasRole("GESTOR")
                    .requestMatchers("/client/**").hasRole("CLIENT")
                    // Todo lo demás requiere autenticación
                    .anyRequest().authenticated()
            )
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/auth/login")  // Página de login personalizada
                    .defaultSuccessUrl("/home", true)  // Redirige a home tras login exitoso
                    .permitAll()
            )
            .logout(logout ->
                logout
                    .logoutUrl("/logout")  // URL para hacer logout
                    .logoutSuccessUrl("/auth/login?logout")  // Página de éxito después del logout
                    .invalidateHttpSession(true)  // Invalida la sesión HTTP
                    .clearAuthentication(true)  // Borra la autenticación
                    .permitAll()  // Permitido para todos
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
            .userDetailsService(userDetailsService)  // Inyecta el servicio de UserDetails
            .passwordEncoder(passwordEncoder());  // Inyecta el PasswordEncoder
        return authenticationManagerBuilder.build();  // Construye el AuthenticationManager
    }

}
