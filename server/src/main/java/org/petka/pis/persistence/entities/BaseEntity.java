package org.petka.pis.persistence.entities;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Id
    //@GeneratedValue(generator = "uuid4")
    //@GenericGenerator(name = "UUID", strategy = "uuid4")
    //@Type(type = "org.hibernate.type.UUIDBinaryType")
    //@Column(columnDefinition = "UUID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Version
    private long version;

    @CreationTimestamp
    private OffsetDateTime createDateTime;

    @UpdateTimestamp
    private OffsetDateTime updateDateTime;

    private boolean deleted;
}
