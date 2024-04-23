package edu.comillas.icai.gitt.pat.spring.p5.entity;

import edu.comillas.icai.gitt.pat.spring.p5.model.Role;
import jakarta.persistence.*;

import java.sql.Timestamp;

/**
 * TODO#2
 * Completa la entidad AppUser (cuya tabla en BD se llamará APP_USER)
 * para que tenga los siguientes campos obligatorios:
 * - id, que será la clave primaria numérica y autogenerada
 * - email, que debe tener valores únicos en toda la tabla
 * - password
 * - role, modelado con la clase Role ya existente en el proyecto
 * - name
 */
@Entity
@Table(name = "APP_USER")
public class AppUser {
    @Id//primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;

    @Column(nullable = false, unique = true) public String email;

    @Column(nullable = false) public String password;

    @Column(nullable = false) public Role role;

    @Column(nullable = false) public String name;
}

