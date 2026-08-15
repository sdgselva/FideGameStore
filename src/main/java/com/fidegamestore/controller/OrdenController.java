package com.fidegamestore.controller;

import com.fidegamestore.domain.Orden;
import com.fidegamestore.service.OrdenService;
import java.util.Locale;
import java.util.NoSuchElementException;
import org.springframework.context.MessageSource;
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
}
