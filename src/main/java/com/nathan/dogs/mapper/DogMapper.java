package com.nathan.dogs.mapper;

import com.nathan.dogs.dto.CreateDogInput;
import com.nathan.dogs.dto.DogOutput;
import com.nathan.dogs.dto.UpdateDogInput;
import com.nathan.dogs.model.Dog;
import com.nathan.dogs.model.Supplier;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DogMapper {

    // Map CreateDogInput to Dog entity
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "stringToSupplier")
    Dog toEntity(CreateDogInput input);

    // Map Dog entity to DogOutput
    DogOutput toOutputDto(Dog dog);

    // Map provided UpdateDogInput fields to existing Dog entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UpdateDogInput dto, @MappingTarget Dog entity);

    @Named("stringToSupplier")
    default Supplier stringToSupplier(String name) {
        if (name == null) return null;
        Supplier supplier = new Supplier();
        supplier.setName(name);
        return supplier;
    }

}
