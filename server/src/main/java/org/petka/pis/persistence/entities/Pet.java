package org.petka.pis.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE pet SET deleted = true WHERE id = ? AND version = ?", check = ResultCheckStyle.COUNT)
public class Pet extends BaseEntity {

    private String name;

    private String kind;

    private int age;

    @Enumerated(EnumType.STRING)
    private Type type;
}

enum Type {
    DOMESTIC("Domestic"), WILD("Wild");
    private final String value;

    Type(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
