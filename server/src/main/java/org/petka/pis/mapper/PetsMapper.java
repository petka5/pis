package org.petka.pis.mapper;

import org.mapstruct.Mapper;
import org.petka.pis.model.NewPet;
import org.petka.pis.persistence.entities.Pet;

@Mapper(componentModel = "spring")
public interface PetsMapper {

    /**
     * Converts dto to entity.
     *
     * @param pet DTO
     * @return entity
     */
    Pet dtoToEntity(NewPet pet);

    /**
     * Converts entity to dto.
     *
     * @param pet entity
     * @return DTO
     */
    org.petka.pis.model.Pet entityToDto(Pet pet);
}
