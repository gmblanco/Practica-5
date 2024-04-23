package edu.comillas.icai.gitt.pat.spring.p5.model;

import edu.comillas.icai.gitt.pat.spring.p5.entity.AppUser;

public record ProfileResponse(
    String name,
    String email,
    Role role
) { }
