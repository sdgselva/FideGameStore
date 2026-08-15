package com.fidegamestore.service;

import com.fidegamestore.domain.Anuncio;
import com.fidegamestore.domain.Llave;
import com.fidegamestore.domain.Plataforma;
import com.fidegamestore.domain.Producto;
import com.fidegamestore.domain.Region;
import com.fidegamestore.domain.VarianteProducto;
import com.fidegamestore.repository.AnuncioRepository;
import com.fidegamestore.repository.LlaveRepository;
import com.fidegamestore.repository.PlataformaRepository;
import com.fidegamestore.repository.ProductoRepository;
import com.fidegamestore.repository.RegionRepository;
import com.fidegamestore.repository.VarianteProductoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnuncioService {

    private final AnuncioRepository anuncioRepository;
    private final LlaveRepository llaveRepository;
    private final VarianteProductoRepository varianteProductoRepository;
    private final ProductoRepository productoRepository;
    private final RegionRepository regionRepository;
    private final PlataformaRepository plataformaRepository;

    public AnuncioService(
            AnuncioRepository anuncioRepository,
            LlaveRepository llaveRepository,
            VarianteProductoRepository varianteProductoRepository,
            ProductoRepository productoRepository,
            RegionRepository regionRepository,
            PlataformaRepository plataformaRepository) {

        this.anuncioRepository = anuncioRepository;
        this.llaveRepository = llaveRepository;
        this.varianteProductoRepository = varianteProductoRepository;
        this.productoRepository = productoRepository;
        this.regionRepository = regionRepository;
        this.plataformaRepository = plataformaRepository;
    }

    @Transactional(readOnly = true)
    public List<Anuncio> getAnuncios(boolean activo) {
        if (activo) {
            return anuncioRepository.findByActivoTrue();
        }
        return anuncioRepository.findAll();
    }

    public List<Anuncio> getAllAnuncios() {
        return anuncioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Anuncio> getAnuncio(Integer idAnuncio) {
        return anuncioRepository.findById(idAnuncio);
    }

    @Transactional(readOnly = true)
    public List<Anuncio> findAnuncioByIdCategoria(Integer idCategoria) {
        return anuncioRepository.findAnuncioByIdCategoria(idCategoria);
    }

    private String crearLlave() {
        String tira = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder clave = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            if (i > 0 && i % 4 == 0) {
                clave.append("-");
            }

            int posicion = (int) (Math.random() * tira.length());
            clave.append(tira.charAt(posicion));
        }

        return clave.toString();
    }

    @Transactional
    public void save(
            Anuncio anuncio,
            Integer idProducto,
            Integer idRegion,
            Integer idPlataforma) {

        boolean nuevo = anuncio.getIdAnuncio() == null;

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException(
                "Producto no encontrado"
        ));

        Region region = regionRepository.findById(idRegion)
                .orElseThrow(() -> new IllegalArgumentException(
                "Región no encontrada"
        ));

        Plataforma plataforma = plataformaRepository.findById(idPlataforma)
                .orElseThrow(() -> new IllegalArgumentException(
                "Plataforma no encontrada"
        ));

        VarianteProducto variante = varianteProductoRepository
                .findByProducto_IdProductoAndRegion_IdRegionAndPlataforma_IdPlataforma(
                        idProducto,
                        idRegion,
                        idPlataforma
                )
                .orElseGet(() -> {

                    VarianteProducto nuevaVariante = new VarianteProducto();

                    nuevaVariante.setProducto(producto);
                    nuevaVariante.setRegion(region);
                    nuevaVariante.setPlataforma(plataforma);

                    return varianteProductoRepository.save(nuevaVariante);
                });

        // ==========================================
        // VERIFICAR ANUNCIO DUPLICADO
        // ==========================================
        boolean duplicado;

        if (nuevo) {

            duplicado = anuncioRepository
                    .existsByVarianteProducto_IdVarianteProducto(
                            variante.getIdVarianteProducto()
                    );

        } else {

            duplicado = anuncioRepository
                    .existsByVarianteProducto_IdVarianteProductoAndIdAnuncioNot(
                            variante.getIdVarianteProducto(),
                            anuncio.getIdAnuncio()
                    );
        }

        if (duplicado) {
            throw new IllegalArgumentException(
                    "Ya existe un anuncio para este producto, región y plataforma."
            );
        }

        // ==========================================
        // GUARDAR ANUNCIO
        // ==========================================
        anuncio.setVarianteProducto(variante);

        if (nuevo) {
            anuncio.setExistencias(1);
        }
        
        anuncio.setExistencias(1);
        anuncioRepository.save(anuncio);
        

        // ==========================================
        // CREAR LLAVE SOLO AL CREAR
        // ==========================================
        if (nuevo) {

            Llave llave = new Llave();

            llave.setVarianteProducto(variante);
            llave.setLlave(crearLlave());
            llave.setActivo(true);

            llaveRepository.save(llave);
        }
    }

    @Transactional
    public void delete(Integer idAnuncio) {
        // Verifica si el anuncio existe antes de intentar eliminarlo
        if (!anuncioRepository.existsById(idAnuncio)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException("El anuncio con ID " + idAnuncio + " no existe.");
        }
        try {
            anuncioRepository.deleteById(idAnuncio);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar el anuncio. Tiene datos asociados.", e);
        }
    }
}
