package com.api.reservavuelos.Models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name = "usuarios")
public class Usuarios  {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(length = 10)
    private String primer_nombre;
    @Column(length = 10)
    private String segundo_nombre;
    @Column(length = 10)
    private String primer_apellido;
    @Column(length = 10)
    private String segundo_apellido;
    @Column(length = 30)
    private String email;
    @Column (length = 10)
    private String telefono;
    @Column()
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecha_nacimiento;
    @Column()
    private String genero;
     @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
     @JoinColumn(name = "id_credencial")
    private Credenciales credenciales;
     @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
     @JoinTable(
             name = "usuarios_roles",
             joinColumns = @JoinColumn(name = "id_usuario"),
             inverseJoinColumns = @JoinColumn(name = "id_rol")
     )
     private List<Roles> roles;
     @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
     @JoinColumn(name = "id_profile_image")
     private Profile_image profile_image;
    @OneToMany(mappedBy = "usuarios")
    private List<Metodos_pagos> metodoPagos;
    @OneToOne(mappedBy = "usuarios")
    private TwoFactorAuth twoFactorAuth;
    @OneToOne(mappedBy = "usuarios")
    private Reservas reservas;
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Usuarios usuarios = (Usuarios) o;
        return getId() != null && Objects.equals(getId(), usuarios.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
