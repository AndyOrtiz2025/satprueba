package com.gestor.dto;

public record RegisterRequestDTO(
    String nombre,
    String apellido,
    String username,
    String email,
    String password
) {}
