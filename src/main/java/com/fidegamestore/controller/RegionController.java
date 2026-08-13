package com.fidegamestore.controller;

import com.fidegamestore.domain.Region;
import com.fidegamestore.service.RegionService;
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
@RequestMapping("/region")
public class RegionController {

    private final RegionService regionService;
    private final MessageSource messageSource;

    public RegionController(RegionService regionService, MessageSource messageSource) {
        this.regionService = regionService;
        this.messageSource = messageSource;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var regions = regionService.getRegions();
        model.addAttribute("regions", regions);
        model.addAttribute("totalRegions", regions.size());
        
        return "/region/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Region region, RedirectAttributes redirectAttributes) {

        regionService.save(region);
        redirectAttributes.addFlashAttribute("todoOk", messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));

        return "redirect:/region/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idRegion, RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";
        try {
            regionService.delete(idRegion);
        } catch (IllegalArgumentException e) {
            titulo = "error"; // Captura la excepción de argumento inválido para el mensaje de "no existe"
            detalle = "region.error01";
        } catch (IllegalStateException e) {
            titulo = "error"; // Captura la excepción de estado ilegal para el mensaje de "datos asociados"
            detalle = "region.error02";
        } catch (Exception e) {
            titulo = "error";  // Captura cualquier otra excepción inesperada
            detalle = "region.error03";
        }
        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(detalle, null, Locale.getDefault()));
        return "redirect:/region/listado";
    }

    @GetMapping("/modificar/{idRegion}")
    public String modificar(@PathVariable("idRegion") Integer idRegion, Model model, RedirectAttributes redirectAttributes) {
        Optional<Region> regionOpt = regionService.getRegion(idRegion);
        if (regionOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", messageSource.getMessage("region.error01", null, Locale.getDefault()));
            return "redirect:/region/listado";
        }
        model.addAttribute("region", regionOpt.get());
        return "/region/modifica";
    }
}
