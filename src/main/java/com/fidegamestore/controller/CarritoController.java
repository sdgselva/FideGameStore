package com.fidegamestore.controller;

import com.fidegamestore.domain.Item;
import com.fidegamestore.domain.Orden;
import com.fidegamestore.domain.Usuario;
import com.fidegamestore.service.CarritoService;
import com.fidegamestore.service.OrdenService;
import com.fidegamestore.service.UsuarioService;
import com.fidegamestore.service.CorreoService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CarritoController {

    private final CarritoService carritoService;
    private final UsuarioService usuarioService;
    private final OrdenService ordenService;
    private final CorreoService correoService;

    public CarritoController(CarritoService carritoService, UsuarioService usuarioService, OrdenService ordenService, CorreoService correoService) {
        this.carritoService = carritoService;
        this.usuarioService = usuarioService;
        this.ordenService = ordenService;
        this.correoService = correoService;
    }

    // --- 1. MOSTRAR EL CARRITO ---
    @GetMapping("/carrito/listado")
    public String listado(HttpSession session, Model model) {
        List<Item> carrito = carritoService.obtenerCarrito(session);

        model.addAttribute("carritoItems", carrito);
        model.addAttribute("totalCarrito", carritoService.calcularTotal(carrito));

        return "/carrito/listado";
    }

    // --- 2. AGREGAR PRODUCTO AL CARRITO ---
    @PostMapping("/carrito/agregar")
    public ModelAndView agregar(
            @RequestParam("idAnuncio") Integer idAnuncio,
            HttpSession session,
            Model model) {
        try {

            System.out.println("Entro al carrito");
            // 1. Obtener el carrito de la sesión
            List<Item> carrito = carritoService.obtenerCarrito(session);

            // 2. Ejecutar la lógica de negocio (el Service asume cantidad = 1)
            carritoService.agregarAnuncio(carrito, idAnuncio);

            // 3. Guardar el carrito actualizado en la sesión
            carritoService.guardarCarrito(session, carrito);

            // 4. Recalcular y actualizar el Model con los datos necesarios
            model.addAttribute("carritoTotal", carritoService.calcularTotal(carrito));
            model.addAttribute("listaItems", carrito);

            // 5. Retornar el fragmento HTML
            return new ModelAndView("/carrito/fragmentos :: verCarrito", model.asMap());

        } catch (RuntimeException e) {
            // 6. Manejo de errores (p. ej., stock insuficiente, anuncio no existe)
            model.addAttribute("errorMensaje", e.getMessage());

            // Retorna un fragmento de error genérico que muestre el mensaje
            return new ModelAndView("/errores/fragmentos :: errorMensaje", model.asMap());
        }
    }

    // --- 3. ELIMINAR ITEM DEL CARRITO ---
    @PostMapping("/carrito/eliminar/{idAnuncio}")
    public String eliminarItem(
            @PathVariable("idAnuncio") Integer idAnuncio,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        List<Item> carrito = carritoService.obtenerCarrito(session);
        carritoService.eliminarItem(carrito, idAnuncio);
        carritoService.guardarCarrito(session, carrito);

        redirectAttributes.addFlashAttribute("mensaje", "Anuncio eliminado del carrito.");
        return "redirect:/carrito/listado";
    }

    @GetMapping("/carrito/modificar/{idAnuncio}")
    public String modificar(
            @PathVariable("idAnuncio") Integer idAnuncio,
            HttpSession session,
            Model model) {

        // 1. Obtener la lista del carrito de la sesión
        List<Item> carrito = carritoService.obtenerCarrito(session);

        // 2. Buscar el ítem en la lista del carrito
        Item item = carritoService.buscarItem(carrito, idAnuncio);

        if (item == null) {
            // Manejar el caso de que el ítem no esté en el carrito
            System.out.println("Hubo problemas");
            return "redirect:/carrito/listado";
        }

        // 3. Pasar el ítem encontrado (con su cantidad actual) al modelo
        model.addAttribute("item", item);

        // 4. Retornar la vista
        return "/carrito/modifica";
    }

    // --- 4. ACTUALIZAR CANTIDAD DESDE LA VISTA ---
    @PostMapping("/carrito/actualizar")
    public String actualizarCantidad(
            @RequestParam("anuncio.idAnuncio") Integer idAnuncio,
            @RequestParam("cantidad") int nuevaCantidad,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            List<Item> carrito = carritoService.obtenerCarrito(session);
            carritoService.actualizarCantidad(carrito, idAnuncio, nuevaCantidad);
            carritoService.guardarCarrito(session, carrito);

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/carrito/listado";
    }

    // --- 5. PROCESAR COMPRA (CHECKOUT) ---
    @GetMapping("/ordenar/carrito")
    public String ordenarCarrito(HttpSession session, RedirectAttributes redirectAttributes) {
        System.out.println("Va a ordenar");

        try {
            List<Item> carrito = carritoService.obtenerCarrito(session);

            // Obtención del usuario autenticado*
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            System.out.println("El username es:" + username);
            Usuario usuario = usuarioService.getUsuarioPorUsername(username).get();

            // 1. La lógica transaccional ocurre en el servicio
            Orden orden = carritoService.procesarCompra(carrito, usuario);

            // Obtener la orden nuevamente con todos sus detalles
            orden = ordenService.getOrdenConDetalle(orden.getIdOrden());

            System.out.println("Orden cargada con detalles.");

            // 3. Intentar enviar correo
            try {
                correoService.enviarCorreoOrden(orden);
                System.out.println("Correo enviado correctamente.");
            } catch (Exception e) {
                System.out.println("ERROR AL ENVIAR EL CORREO:");
                e.printStackTrace();
            }

            // 2. Limpiar el carrito de la sesión después de una compra exitosa
            carritoService.limpiarCarrito(session);

            // 3. Pasar el ID de la orden como Flash Attribute
            redirectAttributes.addFlashAttribute("idOrden", orden.getIdOrden());
            redirectAttributes.addFlashAttribute("mensaje", "Compra procesada con éxito. Orden Nro: " + orden.getIdOrden());

            // 4. Redirigir a una ruta nueva para ver la orden
            System.out.println("Ver la Orden");
            return "redirect:/carrito/verOrden";

        } catch (RuntimeException e) {
            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Error al procesar la compra: " + e.getMessage()
            );

            return "redirect:/carrito/listado";
        }
    }

    // Nuevo método para mostrar la orden
    @GetMapping("/carrito/verOrden")
    public String verOrden(@ModelAttribute("idOrden") Integer idOrden, Model model) {
        if (idOrden == null) {
            // Si no se pasó el ID por flash, redirigir a donde lista de ordens o index
            return "redirect:/index";
        }

        // 1. Obtener la orden COMPLETA (incluyendo ordenLlaves)        
        Orden orden = ordenService.getOrdenConDetalle(idOrden);

        model.addAttribute("orden", orden);
        return "/carrito/verOrden"; // Nombre del archivo Thymeleaf
    }
}
