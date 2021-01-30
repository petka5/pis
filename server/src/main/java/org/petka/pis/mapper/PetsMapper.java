package org.petka.pis.mapper;

import java.util.List;

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


    /**
     * Converts List of entities to List of dto's.
     *
     * @param pet entity
     * @return DTO
     */
    List<org.petka.pis.model.Pet> entitiesToDtos(List<Pet> pet);
}
