package com.fidegamestore.repository;

import com.fidegamestore.domain.Anuncio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnuncioRepository extends JpaRepository<Anuncio, Integer> {
    @Query(nativeQuery = true,
                value = "SELECT * FROM anuncio a WHERE a.activo=true;")
    public List<Anuncio> findByActivoTrue();
    
    @Query("SELECT a FROM Anuncio a " +
        "LEFT JOIN FETCH a.varianteProducto vp " +       // Carga usuario
        "LEFT JOIN FETCH vp.producto p " +       // Carga las ordenesLlave que tengan el mismo ID
        "LEFT JOIN FETCH p.categoria c " +       // Carga las ordenesLlave que tengan el mismo ID
        "WHERE c.idCategoria = :idCategoria AND a.activo = true ")
    List<Anuncio> findAnuncioByIdCategoria(@Param("idCategoria") Integer idCategoria);
    
    @Query("SELECT a FROM Anuncio a " +
        "LEFT JOIN FETCH a.varianteProducto vp " +       // Carga usuario
        "LEFT JOIN FETCH vp.producto p " +       // Carga las ordenesLlave que tengan el mismo ID
        "LEFT JOIN FETCH p.categoria c " )
    List<Anuncio> findAllAnuncios();
    
    
    boolean existsByVarianteProducto_IdVarianteProducto(Integer idVarianteProducto);

    boolean existsByVarianteProducto_IdVarianteProductoAndIdAnuncioNot(Integer idVarianteProducto,Integer idAnuncio);
    
   
}
