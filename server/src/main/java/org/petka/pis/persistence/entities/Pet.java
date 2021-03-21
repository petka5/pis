package org.petka.pis.persistence.entities;

import javax.persistence.Entity;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE pet SET deleted = true WHERE id = ? AND version = ?", check = ResultCheckStyle.COUNT)
public class Pet extends BaseEntity {

    private String name;

    private String kind;

    private int age;
}
