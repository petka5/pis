package org.petka.pis.configuration;

import lombok.Getter;

@Getter
public enum Role {

    OPERATOR(Names.OPERATOR), ORGANIZATION(Names.ORGANIZATION);
    private final String name;

    Role(final String name) {
        this.name = name;
    }

    public static class Names {

        public static final String OPERATOR = "operator";
        public static final String ORGANIZATION = "organization";
    }
}
