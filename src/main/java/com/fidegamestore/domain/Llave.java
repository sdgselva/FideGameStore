package com.fidegamestore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "llave")
public class Llave implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_llave")
    private Integer idLlave;
    
    @NotBlank
    @Column(unique = true, length = 15)
    private String llave;
    
    private boolean activo;
    
    @ManyToOne
    @JoinColumn(name = "id_variante_producto")
    private VarianteProducto varianteProducto;
}
