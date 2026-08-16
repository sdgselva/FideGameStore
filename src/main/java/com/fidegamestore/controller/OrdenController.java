package com.fidegamestore.controller;

import com.fidegamestore.domain.Orden;
import com.fidegamestore.domain.Usuario;
import com.fidegamestore.service.OrdenService;
import java.util.Locale;
import java.util.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orden")
public class OrdenController {
    private final OrdenService ordenService;
    private final MessageSource messageSource;
    
    public OrdenController(OrdenService ordenService, MessageSource messageSource) {
        this.ordenService = ordenService;
        this.messageSource = messageSource;
    }
    
    @GetMapping("/listado")
    public String listado(Model model) {
        var ordenes = ordenService.getAllOrdenes();
        model.addAttribute("ordenes", ordenes);
        model.addAttribute("totalOrdenes", ordenes.size());
        return "/orden/listado";
    }
    
    @GetMapping("/ordenesUsuario")
    public String ordenesUsuario(Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        var ordenes = ordenService.getOrdenesPorUsername(username);
        model.addAttribute("ordenes", ordenes);
        return "/orden/ordenesUsuario";
    }
    

    @GetMapping("/productosOrden/{idOrden}")
    public String productosOrden(@PathVariable("idOrden") Integer idOrden, Model model, RedirectAttributes redirectAttributes) {
        try {
            Orden orden = ordenService.getOrdenConDetalle(idOrden);
            model.addAttribute("orden", orden);
            return "/orden/productosOrden";
        } catch (NoSuchElementException e) {
            // Captura la excepción de 'no encontrado' del servicio
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("orden.error01", null, Locale.getDefault()));
            return "redirect:/orden/listado";
        }
    }
    
    @GetMapping("/productosOrdenUsuario/{idOrden}")
    public String productosOrdenUsuario(@PathVariable("idOrden") Integer idOrden, Model model, RedirectAttributes redirectAttributes) {
        try {
            Orden orden = ordenService.getOrdenConDetalle(idOrden);
            model.addAttribute("orden", orden);
            return "/orden/productosOrdenUsuario";
        } catch (NoSuchElementException e) {
            // Captura la excepción de 'no encontrado' del servicio
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("orden.error01", null, Locale.getDefault()));
            return "redirect:/orden/productosOrden";
        }
    }
}
