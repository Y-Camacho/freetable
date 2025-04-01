package com.camacho.proyecto.freetable.controllers;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.camacho.proyecto.freetable.models.Cliente;
import com.camacho.proyecto.freetable.models.Usuario;
import com.camacho.proyecto.freetable.models.Usuario.Rol;
import com.camacho.proyecto.freetable.repositories.UsuarioRepository;
import com.camacho.proyecto.freetable.security.CustomUserDetailsService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final CustomUserDetailsService userService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthController(CustomUserDetailsService userService, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute Cliente cliente, @RequestParam("role") String role, @RequestParam("repassw") String repassw, Model model) {
        if (usuarioRepository.findByUsername(cliente.getUsername()).isPresent()) {
            model.addAttribute("error", "Ya existe un usuario con este correo.");
            return "auth/register"; // Redirigir a la página de registro con un mensaje de error
        }

        if (!cliente.getPassword().equals(repassw)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "auth/register"; // Redirigir a la página de registro con un mensaje de error
        }

        cliente.setRole(Rol.valueOf(role.toUpperCase())); // Convertir String a Enum
        userService.saveUser(cliente);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "logout", required = false) String logout){
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Verificamos la contraseña
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                // Autenticamos al usuario usando Spring Security
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        usuario.getUsername(), password);
                authentication = authenticationManager.authenticate(authentication);

                // Establecer el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Redirigir según el rol del usuario
                switch (usuario.getRole()) {
                    case ADMIN:
                        return "redirect:/admin/dashboard";
                    case GESTOR:
                        return "redirect:/gestor/dashboard";
                    case CLIENT:
                        return "redirect:/home";
                }
            } else {
                return "redirect:/auth/login?error";
            }
        } else {
            return "redirect:/auth/login?error";
        }
    
        return "redirect:/auth/login"; // Redirige con mensaje de error
    }




}