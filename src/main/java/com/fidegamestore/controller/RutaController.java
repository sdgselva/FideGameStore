package com.fidegamestore.controller;

import com.fidegamestore.domain.Ruta;
import com.fidegamestore.service.RutaService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ruta")
public class RutaController {
    
    private final RutaService rutaService;
    private final MessageSource messageSource;
    
    public RutaController(RutaService rutaService,
        MessageSource messageSource) {
        this.rutaService = rutaService;
        this.messageSource = messageSource;
    }
    
    @GetMapping("/listado")
    public String listado(Model model) {
        var rutas = rutaService.getRutas();
        model.addAttribute("rutas", rutas);
        model.addAttribute("totalRutas", rutas.size());
        return "/ruta/listado";
    }
    
    @PostMapping("/guardar")
    public String guardar(@Valid Ruta ruta, RedirectAttributes redirectAttributes) {
        rutaService.save(ruta);
        redirectAttributes.addFlashAttribute("todoOk", messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));
        return "redirect:/ruta/listado";
    }
    
    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idRuta, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            rutaService.delete(idRuta);
        } catch (IllegalArgumentException e) {
            titulo = "error"; 
            detalle = "ruta.error01";
        } catch (IllegalStateException e) {
            titulo = "error"; 
            detalle = "ruta.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "ruta.error03";
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/ruta/listado";
    }

    @GetMapping("/modificar/{idRuta}")
    public String modificar(@PathVariable("idRuta") Integer idRuta, Model model, RedirectAttributes redirectAttributes) {
        try {
            Ruta ruta = rutaService.getRuta(idRuta);
            model.addAttribute("ruta", ruta);
            return "/ruta/modifica";
        } catch (NoSuchElementException e) {
            // Captura la excepción de 'no encontrado' del servicio
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("ruta.error01", null, Locale.getDefault()));
            return "redirect:/ruta/listado";
        }
    }
}
