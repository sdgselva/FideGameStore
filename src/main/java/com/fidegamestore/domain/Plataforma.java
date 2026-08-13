package com.fidegamestore.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "plataforma")
public class Plataforma implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plataforma")
    private Integer idPlataforma;

    @Column(unique = true, nullable = false, length = 50)
    @NotNull
    @Size(max = 50)
    private String nombrePlataforma;
    
    @Column(name = "activo")
    private Boolean activo;
}
