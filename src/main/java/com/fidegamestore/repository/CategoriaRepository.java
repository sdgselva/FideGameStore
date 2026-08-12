package com.fidegamestore.repository;

import com.fidegamestore.domain.Categoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{
    @Query(nativeQuery = true,
                value = "SELECT * from categoria c;")
    public List<Categoria> findAllCategorias();
    
    @Query(nativeQuery = true,
                value = "SELECT * FROM categoria c WHERE c.activo = true;")
    public List<Categoria> findByActivoTrue();  
}
