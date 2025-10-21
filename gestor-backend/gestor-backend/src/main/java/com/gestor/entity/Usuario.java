// Usuario.java
package com.gestor.entity;

import jakarta.persistence.*;

@Entity @Table(name="usuarios")
public class Usuario {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=150)
    private String username;

    @Column(name="password_hash", nullable=false, length=255)
    private String passwordHash;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username){ this.username=username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash){ this.passwordHash=passwordHash; }
}
