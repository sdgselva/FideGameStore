package com.fidegamestore.controller;

import com.fidegamestore.service.CategoriaService;
import com.fidegamestore.service.AnuncioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {
 // Las últimas versiones de Spring, recomiendan utilziar final y contructor en lugar de @autowired
    
    private final AnuncioService anuncioService;
    private final CategoriaService categoriaService;
    
    // (Spring inyecta automáticamente)
    public IndexController(AnuncioService anuncioService, CategoriaService categoriaService) {
        this.anuncioService = anuncioService;
        this.categoriaService = categoriaService;
    }
    
    @GetMapping("/")
    public String cargarPaginaInicio(Model model) {
        var lista = anuncioService.getAnuncios(true);
        model.addAttribute("anuncios", lista);
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/index";
    }
    
    @GetMapping("/consultas/{idCategoria}")
    public String listado(@PathVariable("idCategoria") Integer idCategoria, Model model) {
        model.addAttribute("idCategoriaActual", idCategoria);
        var categoriaOptional = categoriaService.getCategoria(idCategoria);
        if (categoriaOptional.isEmpty()) {
            //Puede ser que no se exista la categoria buscada...
            model.addAttribute("anuncios", java.util.Collections.emptyList());
        } else {
            var categoria = categoriaOptional.get();
            var anuncios = anuncioService.findAnuncioByIdCategoria(categoria.getIdCategoria());
            model.addAttribute("anuncios", anuncios);
        }
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/index";
    }
}
