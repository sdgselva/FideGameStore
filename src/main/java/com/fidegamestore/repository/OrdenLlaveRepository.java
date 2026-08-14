package com.fidegamestore.repository;

import com.fidegamestore.domain.OrdenLlave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface OrdenLlaveRepository extends JpaRepository<OrdenLlave, Integer> {
    
}
