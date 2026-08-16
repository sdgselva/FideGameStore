package com.fidegamestore.controller;

import com.fidegamestore.domain.Usuario;
import com.fidegamestore.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final MessageSource messageSource;
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    public UsuarioController(UsuarioService usuarioService,
            MessageSource messageSource) {
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String inicio(Model model) {
        var usuarios = usuarioService.getUsuarios(false);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        return "/usuario/listado";
    }

    @GetMapping("/perfil")
    public String listarUsuario(Model model, RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Usuario usuario = usuarioService.getUsuarioPorUsername(username).orElse(null);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "El usuario no fue encontrado."
            );
            return "redirect:/";
        }

        model.addAttribute("usuario", usuario);

        return "usuario/perfil";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Usuario usuario,
            BindingResult bindingResult,
            @RequestParam MultipartFile imagenFile,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Redirige al formulario de edición/creación para mostrar errores
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error04", null, Locale.getDefault()));
            // Si no hay idUsuario, redirige al listado con modal para agregar
            if (usuario.getIdUsuario() == null) {
                return "redirect:/usuario/listado";
            }
            // Si hay idUsuario, redirige al formulario de modificación
            return "redirect:/usuario/modificar/" + usuario.getIdUsuario();
        }
        usuarioService.save(usuario, imagenFile, true);
        redirectAttributes.addFlashAttribute("todoOk",
                messageSource.getMessage("mensaje.actualizado",
                        null, Locale.getDefault()));
        return "redirect:/usuario/listado";
    }

    @PostMapping("/guardarSelf")
    public String guardarSelf(
            @Valid Usuario usuario,
            BindingResult bindingResult,
            @RequestParam(required = false) MultipartFile imagenFile,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage(
                            "usuario.error04",
                            null,
                            Locale.getDefault()
                    )
            );

            return "redirect:/usuario/perfil";
        }

        usuarioService.saveSelf(
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getCorreo(),
                imagenFile
        );

        redirectAttributes.addFlashAttribute(
                "todoOk",
                "Información actualizada. Por favor, inicie sesión nuevamente."
        );

        logoutHandler.logout(
                request,
                response,
                SecurityContextHolder.getContext().getAuthentication()
        );

        return "redirect:/login";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idUsuario,
            RedirectAttributes redirectAttributes) {
        try {
            usuarioService.delete(idUsuario);
            redirectAttributes.addFlashAttribute("todoOk",
                    messageSource.getMessage("mensaje.eliminado", null,
                            Locale.getDefault()));
        } catch (IllegalArgumentException e) {
            // Captura argumento inválido para el mensaje de "no existe"
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error01", null,
                            Locale.getDefault()));
        } catch (IllegalStateException e) {
            // Captura estado ilegal para el mensaje de "datos asociados"
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error02", null,
                            Locale.getDefault()));
        } catch (NoSuchMessageException e) {
            // Captura cualquier otra excepción inesperada
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error03", null,
                            Locale.getDefault()));
        }
        return "redirect:/usuario/listado";
    }

    @GetMapping("/modificar/{idUsuario}")
    public String modificar(@PathVariable("idUsuario") Integer idUsuario,
            Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.getUsuario(idUsuario);
        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    "El usuario no fue encontrado.");
            return "redirect:/usuario/listado";
        }
        Usuario usuario = usuarioOpt.get();
        usuario.setPassword("");
        model.addAttribute("usuario", usuario);
        return "usuario/perfil";
    }
}
