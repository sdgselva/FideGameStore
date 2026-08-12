package com.fidegamestore.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name = "variante_producto")
public class VarianteProducto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variante_producto")
    private Integer idVarianteProducto;
    
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;
    
    @ManyToOne
    @JoinColumn(name = "id_region")
    private Region region;
    
    @ManyToOne
    @JoinColumn(name = "id_plataforma")
    private Plataforma plataforma;
}
