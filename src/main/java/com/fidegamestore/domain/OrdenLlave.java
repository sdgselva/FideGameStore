package com.fidegamestore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "orden_llave")
public class OrdenLlave implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden_llave")
    private Integer idOrdenLlave;
    
    @ManyToOne
    @JoinColumn(name = "id_orden")
    private Orden orden;
    
    @ManyToOne
    @JoinColumn(name = "id_llave")
    private Llave llave;
    
    private BigDecimal precioPagado;
    
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
}
