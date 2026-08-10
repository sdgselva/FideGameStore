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
@Table(name = "ticket")
public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Integer idTicket;
    
    @ManyToOne
    @JoinColumn(name = "id_orden")
    private Orden orden;
    
    @ManyToOne
    @JoinColumn(name = "id_estado_ticket")
    private EstadoTicket estadoTicket;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
}
