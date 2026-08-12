package com.fidegamestore.controller;

import com.fidegamestore.service.CategoriaService;
import com.fidegamestore.service.ProductoService;
import com.fidegamestore.service.AnuncioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class IndexController {
 // Las últimas versiones de Spring, recomiendan utilziar final y contructor en lugar de @autowired
    
    private final AnuncioService anuncioService;
    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    
    // (Spring inyecta automáticamente)
    public IndexController(AnuncioService anuncioService, ProductoService productoService, CategoriaService categoriaService) {
        this.anuncioService = anuncioService;
        this.productoService = productoService;
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
    
//    @GetMapping("/")
//    public String cargarPaginaInicio(Model model) {
//        var lista = productoService.getProductos(true);
//        model.addAttribute("productos", lista);
//        var categorias = categoriaService.getCategorias(true);
//        model.addAttribute("categorias", categorias);
//        return "/index";
//    }
    
    @GetMapping("/consultas/{idCategoria}")
    public String listado(@PathVariable("idCategoria") Integer idCategoria, Model model) {
        model.addAttribute("idCategoriaActual", idCategoria);
        var categoriaOptional = categoriaService.getCategoria(idCategoria);
        if (categoriaOptional.isEmpty()) {
            //Puede ser que no se exista la categoria buscada...
            model.addAttribute("productos", java.util.Collections.emptyList());
        } else {
            var categoria = categoriaOptional.get();
            var productos = categoria.getProductos();
            model.addAttribute("productos", productos);
        }
        var categorias = categoriaService.getCategorias(true);
        model.addAttribute("categorias", categorias);
        return "/index";
    }
}
