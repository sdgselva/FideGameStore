package com.fidegamestore.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "orden")
public class Orden implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Integer idOrden;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
        
    @ManyToOne
    @JoinColumn(name = "id_estado_orden")
    private EstadoOrden estadoOrden;
    
    private BigDecimal precioTotal;
    
    @OneToMany(mappedBy = "orden", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenLlave> ordenLlaves;
}
