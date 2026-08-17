package com.fidegamestore.controller;

import com.fidegamestore.domain.Plataforma;
import com.fidegamestore.service.PlataformaService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/plataforma")
public class PlataformaController {

    private final PlataformaService plataformaService;
    private final MessageSource messageSource;

    public PlataformaController(PlataformaService plataformaService, MessageSource messageSource) {
        this.plataformaService = plataformaService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var plataformas = plataformaService.getPlataformas();
        model.addAttribute("plataformas", plataformas);
        model.addAttribute("totalPlataformas", plataformas.size());
        
        return "/plataforma/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Plataforma plataforma, RedirectAttributes redirectAttributes) {

        plataformaService.save(plataforma);
        redirectAttributes.addFlashAttribute("todoOk", messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));

        return "redirect:/plataforma/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idPlataforma, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            plataformaService.delete(idPlataforma);
        } catch (IllegalArgumentException e) {
            titulo = "error"; 
            detalle = "plataforma.error01";
        } catch (IllegalStateException e) {
            titulo = "error"; 
            detalle = "plataforma.error02";
        } catch (Exception e) {
            titulo = "error";  
            detalle = "plataforma.error03";
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/plataforma/listado";
    }

    @GetMapping("/modificar/{idPlataforma}")
    public String modificar(@PathVariable("idPlataforma") Integer idPlataforma, Model model, RedirectAttributes redirectAttributes) {
        Optional<Plataforma> plataformaOpt = plataformaService.getPlataforma(idPlataforma);
        if (plataformaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("plataforma.error01", null, Locale.getDefault()));
            return "redirect:/plataforma/listado";
        }
        model.addAttribute("plataforma", plataformaOpt.get());
        return "/plataforma/modifica";
    }
}
