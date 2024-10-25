package com.api.reservavuelos.Models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "metodos_pagos")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Metodos_pagos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdMetodoPago;
    @Column
    private String nombreTitular;
    @Column
    private String numero;
    @Column
    private String fechaVencimiento;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuarios usuarios;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Metodos_pagos that = (Metodos_pagos) o;
        return getIdMetodoPago() != null && Objects.equals(getIdMetodoPago(), that.getIdMetodoPago());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
