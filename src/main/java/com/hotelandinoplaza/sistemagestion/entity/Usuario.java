    package com.hotelandinoplaza.sistemagestion.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Entity
    @Table(name = "usuarios")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Usuario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String email;

        @Column(nullable = false)
        private String password;

        // Aquí guardaremos "ADMIN" o "USER"
        @Column(nullable = false)
        private String rol;

        private String nombre;

        @Column(name = "tipo_documento", nullable = false)
        private String tipoDocumento; // "DNI", "RUC", "PASAPORTE"

        @Column(name = "numero_documento", nullable = false)
        private String numeroDocumento;
    }