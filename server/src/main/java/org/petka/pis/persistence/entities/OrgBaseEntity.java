package org.petka.pis.persistence.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@MappedSuperclass
@NoArgsConstructor
public class OrgBaseEntity extends BaseEntity {

    @Column(updatable = false)
    private UUID orgId;
}