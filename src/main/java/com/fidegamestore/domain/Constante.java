package com.fidegamestore.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "constante")
@AllArgsConstructor
@NoArgsConstructor
public class Constante {
    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_constante")
    private Integer idConstante;
    
    @Column(name="atributo", unique = true, nullable = false, length = 25)
    private String atributo;
    
    @Column(name="valor", nullable = false, length = 150)
    private String valor;
}
