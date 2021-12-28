package org.petka.pis.controllers;

public final class Paths {

    private Paths() {
    }

    public static final String OPERATOR_PETS = "/api/operator/pets";
    public static final String OPERATOR_PETS_ID = OPERATOR_PETS.concat("/{id}");
}
