package edu.comillas.icai.gitt.pat.spring.p5.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
    @NotBlank
    String email,
    @NotBlank
    String password
) {}
