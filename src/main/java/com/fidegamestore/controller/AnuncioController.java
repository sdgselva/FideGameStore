package com.fidegamestore.controller;

import com.fidegamestore.domain.Anuncio;
import com.fidegamestore.service.RegionService;
import com.fidegamestore.service.PlataformaService;
import com.fidegamestore.service.CategoriaService;
import com.fidegamestore.service.AnuncioService;
import com.fidegamestore.service.ProductoService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.context.MessageSource;

@Controller
@RequestMapping("/anuncio")
public class AnuncioController {

    private final AnuncioService anuncioService;
    private final CategoriaService categoriaService;
    private final RegionService regionService;
    private final PlataformaService plataformaService;
    private final ProductoService productoService;
    private final MessageSource messageSource;

    public AnuncioController(AnuncioService anuncioService, CategoriaService categoriaService,
            RegionService regionService, PlataformaService plataformaService,
            ProductoService productoService, MessageSource messageSource) {
        this.anuncioService = anuncioService;
        this.categoriaService = categoriaService;
        this.regionService = regionService;
        this.plataformaService = plataformaService;
        this.productoService = productoService;
        this.messageSource = messageSource;

    }

    @GetMapping("/listado")
    public String cargarListaTodosAnuncios(Model model) {

        var lista = anuncioService.getAllAnuncios();
        model.addAttribute("anuncios", lista);
        model.addAttribute("totalAnuncios", lista.size());

        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);

        var regiones = regionService.getRegionesActivas();
        model.addAttribute("regiones", regiones);

        var plataformas = plataformaService.getPlataformasActivas();
        model.addAttribute("plataformas", plataformas);

        var productos = productoService.getProductosActivos();
        model.addAttribute("productos", productos);

        return "/anuncio/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Anuncio anuncio,
            @RequestParam Integer idProducto,
            @RequestParam Integer idRegion,
            @RequestParam Integer idPlataforma,
            RedirectAttributes redirectAttributes) {
        anuncioService.save(anuncio, idProducto, idRegion, idPlataforma);
        redirectAttributes.addFlashAttribute(
                "todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault())
        );
        return "redirect:/anuncio/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idAnuncio,
            RedirectAttributes redirectAttributes) {
        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";

        try {
            anuncioService.delete(idAnuncio);
        } catch (IllegalArgumentException e) {
            titulo = "error"; 
            detalle = "anuncio.error01";
        } catch (IllegalStateException e) {
            titulo = "error";  
            detalle = "anuncio.error02";
        } catch (Exception e) {
            titulo = "error";
            detalle = "anuncio.error03";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault())
        );
        return "redirect:/anuncio/listado";
    }

    @GetMapping("/modificar/{idAnuncio}")
    public String modificar(@PathVariable("idAnuncio") Integer idAnuncio,
            Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Anuncio> anuncioOpt = anuncioService.getAnuncio(idAnuncio);

        if (anuncioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage("error", null, Locale.getDefault())
            );
            return "redirect:/anuncio/listado";
        }

        model.addAttribute("anuncio", anuncioOpt.get());

        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);

        var regiones = regionService.getRegionesActivas();
        model.addAttribute("regiones", regiones);

        var plataformas = plataformaService.getPlataformasActivas();
        model.addAttribute("plataformas", plataformas);

        var productos = productoService.getProductosActivos();
        model.addAttribute("productos", productos);

        return "/anuncio/modifica";
    }

    @GetMapping("/consultas/{idCategoria}")
    public String listado(@PathVariable("idCategoria") Integer idCategoria, Model model) {
        model.addAttribute("idCategoriaActual", idCategoria);
        var categoriaOptional = categoriaService.getCategoria(idCategoria);
        if (categoriaOptional.isEmpty()) {

            model.addAttribute("anuncios", java.util.Collections.emptyList());
        } else {
            var categoria = categoriaOptional.get();
            var anuncios = anuncioService.findAnuncioByIdCategoria(categoria.getIdCategoria());
            model.addAttribute("anuncios", anuncios);
        }
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);

        var regiones = regionService.getRegionesActivas();
        model.addAttribute("regiones", regiones);

        var plataformas = plataformaService.getPlataformasActivas();
        model.addAttribute("plataformas", plataformas);

        var productos = productoService.getProductosActivos();
        model.addAttribute("productos", productos);

        return "/anuncio/listado";
    }
}
