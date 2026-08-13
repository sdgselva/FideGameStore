package com.fidegamestore.repository;

import com.fidegamestore.domain.VarianteProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VarianteProductoRepository extends JpaRepository<VarianteProducto, Integer>{
        public List<VarianteProducto> findAll();
} 
