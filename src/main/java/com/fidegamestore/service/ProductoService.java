package com.fidegamestore.service;

import com.fidegamestore.domain.Producto;
import com.fidegamestore.repository.ProductoRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final FirebaseStorageService firebaseStorageService;

    public ProductoService(ProductoRepository productoRepository, FirebaseStorageService firebaseStorageService) {
        this.productoRepository = productoRepository;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean activo) {
        return productoRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public List<Producto> getProductosActivos() {          
       return productoRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }

    @Transactional(readOnly = true)
    public List<Producto> findProductoByIdCategoria(Integer idCategoria) {
        return productoRepository.findProductoByIdCategoria(idCategoria);
    }

    @Transactional
    public void save(Producto producto, MultipartFile imagenFile) {
        // If this is an existing product
        if (producto.getIdProducto() != null) {
            Producto productoExistente
                    = productoRepository.findById(producto.getIdProducto())
                            .orElseThrow();
            // Update normal fields
            productoExistente.setNombreProducto(producto.getNombreProducto());
            productoExistente.setCategoria(producto.getCategoria());
            productoExistente.setActivo(producto.isActivo());
            // Only replace the image if a new image was uploaded
            if (imagenFile != null && !imagenFile.isEmpty()) {
                try {
                    String rutaImagen = firebaseStorageService.uploadImage(
                            imagenFile,
                            "producto",
                            productoExistente.getIdProducto());
                    productoExistente.setRutaImagen(rutaImagen);
                } catch (IOException e) {
                    throw new RuntimeException("Error al subir la imagen", e);
                }
            }
            productoRepository.save(productoExistente);
        } else {
            // New product
            producto = productoRepository.save(producto);
            if (imagenFile != null && !imagenFile.isEmpty()) {
                try {
                    String rutaImagen = firebaseStorageService.uploadImage(
                            imagenFile,
                            "producto",
                            producto.getIdProducto());

                    producto.setRutaImagen(rutaImagen);
                    productoRepository.save(producto);

                } catch (IOException e) {
                    throw new RuntimeException("Error al subir la imagen", e);
                }
            }
        }
    }

    @Transactional
    public void delete(Integer idProducto) {
        // Verifica si el producto existe antes de intentar eliminarlo
        if (!productoRepository.existsById(idProducto)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException("El producto con ID " + idProducto + " no existe.");
        }
        try {
            productoRepository.deleteById(idProducto);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar el producto. Tiene datos asociados.", e);
        }
    }
}
