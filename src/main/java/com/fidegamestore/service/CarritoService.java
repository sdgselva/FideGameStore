package com.fidegamestore.service;

import com.fidegamestore.domain.*;
import com.fidegamestore.repository.OrdenRepository;
import com.fidegamestore.repository.AnuncioRepository;
import com.fidegamestore.repository.OrdenLlaveRepository;
import com.fidegamestore.repository.LlaveRepository;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarritoService {
    private static final String ATTRIBUTE_CARRITO = "carrito";
    
    private final AnuncioRepository anuncioRepository;
    private final OrdenRepository ordenRepository;
    private final OrdenLlaveRepository ordenLlaveRepository;
    private final LlaveRepository llaveRepository;

    public CarritoService(AnuncioRepository anuncioRepository, OrdenRepository ordenRepository, OrdenLlaveRepository ordenLlaveRepository, LlaveRepository llaveRepository) {
        this.anuncioRepository = anuncioRepository;
        this.ordenRepository = ordenRepository;
        this.ordenLlaveRepository = ordenLlaveRepository;
        this.llaveRepository = llaveRepository;
    }   

    // --- 1. Gestión de Sesión ---
    public List<Item> obtenerCarrito(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Item> carrito = (List<Item>) session.getAttribute(ATTRIBUTE_CARRITO);
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        return carrito;
    }
    
    public void guardarCarrito(HttpSession session, List<Item> carrito) {
        session.setAttribute(ATTRIBUTE_CARRITO, carrito);
    }

    public void agregarAnuncio(List<Item> carrito, Integer idAnuncio) {
        // 1. Buscar el anuncio en BD
        Anuncio anuncio = anuncioRepository.findById(idAnuncio)
            .orElseThrow(() -> new RuntimeException("Anuncio no encontrado."));

        // 2. Buscar si el item ya existe en el carrito
        Optional<Item> itemExistente = carrito.stream()
            .filter(i -> i.getAnuncio().getIdAnuncio().equals(idAnuncio))
            .findFirst();

        int cantidad = 1;
        
        if (itemExistente.isPresent()) {
            Item item = itemExistente.get();
            int nuevaCantidad = item.getCantidad() + cantidad;
            
            // 3. (CRÍTICO) Validación de Stock
            if (nuevaCantidad > anuncio.getExistencias()) {
                 throw new RuntimeException("Stock insuficiente para agregar " + cantidad + " unidades.");
            }
            item.setCantidad(nuevaCantidad);
        } else {
            // 4. (Nuevo Item) Validación de Stock
            if (cantidad > anuncio.getExistencias()) {
                 throw new RuntimeException("Stock insuficiente para agregar " + cantidad + " unidades.");
            }
            
            // 5. Crear y añadir nuevo Item (Composición)
            Item nuevoItem = new Item();
            nuevoItem.setAnuncio(anuncio);
            nuevoItem.setCantidad(cantidad);
            nuevoItem.setPrecioHistorico(anuncio.getPrecio()); // Capturar precio actual
            carrito.add(nuevoItem);
        }
    }
    
    public Item buscarItem(List<Item> carrito, Integer idAnuncio) {
        if (carrito == null) {
            return null;
        }
        
        return carrito.stream()
                .filter(item -> item.getAnuncio().getIdAnuncio().equals(idAnuncio)) // Filtra por el ID
                .findFirst()                                                          // Obtiene el primer elemento
                .orElse(null);                                                        // Retorna null si no lo encuentra
    }
    
    public void eliminarItem(List<Item> carrito, Integer idAnuncio) {
        // Usar List.removeIf es una forma concisa de eliminar por condición
        carrito.removeIf(item -> item.getAnuncio().getIdAnuncio().equals(idAnuncio));
    }
    
    public void actualizarCantidad(List<Item> carrito, Integer idAnuncio, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            eliminarItem(carrito, idAnuncio);
            return;
        }

        Optional<Item> itemExistente = carrito.stream()
            .filter(i -> i.getAnuncio().getIdAnuncio().equals(idAnuncio))
            .findFirst();

        if (itemExistente.isPresent()) {
            Item item = itemExistente.get();
            Anuncio anuncio = item.getAnuncio();
            
            if (nuevaCantidad > anuncio.getExistencias()) {
                 throw new RuntimeException("No hay suficiente stock disponible.");
            }
            item.setCantidad(nuevaCantidad);
        }
    }

    public int contarUnidades(List<Item> carrito) {
        if (carrito == null || carrito.isEmpty()) {
            return 0;
        }
        return carrito.stream()
                .mapToInt(Item::getCantidad) // Mapea cada Item al valor de su campo 'cantidad'
                .sum();                      // Suma todos los valores
    }
    
    public BigDecimal calcularTotal(List<Item> carrito) {
        // Sumar todos los subtotales de la lista
        return carrito.stream()
            .map(Item::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public void limpiarCarrito(HttpSession session) {
        List<Item> carrito = obtenerCarrito(session);
        if (carrito != null) {
            carrito.clear();
        }
        guardarCarrito(session, carrito);
    }

    @Transactional
    public Orden procesarCompra(List<Item> carrito, Usuario usuario) {
        System.out.println("Se va a Procesar la Compra...");
        if (carrito == null || carrito.isEmpty()) {
            throw new RuntimeException("El carrito está vacío para procesar la compra.");
        }
        
        // 1. CREAR Y PERSISTIR FACTURA
        Orden orden = new Orden();
        EstadoOrden estadoOrden = new EstadoOrden();
        estadoOrden.setIdEstadoOrden(1);
              
        orden.setUsuario(usuario);
        orden.setPrecioTotal(calcularTotal(carrito));
        orden.setEstadoOrden(estadoOrden); 
        orden = ordenRepository.save(orden); // Persistir para obtener el idOrden

        // 2. CREAR Y PERSISTIR LINEAS DE VENTA (OrdenLlave) y ACTUALIZAR STOCK
        for (Item item : carrito) {
            // a. Verificar stock final antes de persistir (doble chequeo)
            Anuncio anuncio = anuncioRepository.findById(item.getAnuncio().getIdAnuncio()).get();
            if (item.getCantidad() > anuncio.getExistencias()) {
                throw new RuntimeException("Fallo en la compra: El anuncio " + anuncio.getVarianteProducto().getProducto().getNombreProducto() + " ya no tiene suficiente stock.");
            }
            
            // b. Crear entidad OrdenLlave (Línea de detalle)
            OrdenLlave ordenLlave = new OrdenLlave();
            Llave llave = llaveRepository.findByIdVarianteProducto(item.getAnuncio().getVarianteProducto().getIdVarianteProducto());
            
            ordenLlave.setOrden(orden);
            ordenLlave.setLlave(llave);
            ordenLlave.setPrecioPagado(item.getPrecioHistorico());
            ordenLlaveRepository.save(ordenLlave);
            
            // c. Actualizar inordenLlaverio (Stock)
            anuncio.setExistencias(anuncio.getExistencias() - item.getCantidad());
            anuncio.setActivo(false);
            anuncioRepository.save(anuncio);
        }

        // 3. Limpiar carrito (El controller se encargará de esto)
        
        return orden;
    }
}