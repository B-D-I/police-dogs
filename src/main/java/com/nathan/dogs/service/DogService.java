package com.nathan.dogs.service;

import com.nathan.dogs.dto.CreateDogInput;
import com.nathan.dogs.dto.DogOutput;
import com.nathan.dogs.dto.UpdateDogInput;
import com.nathan.dogs.mapper.DogMapper;
import com.nathan.dogs.model.Dog;
import com.nathan.dogs.model.Supplier;
import com.nathan.dogs.repository.DogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static com.nathan.dogs.constant.DogConstants.BREED;
import static com.nathan.dogs.constant.DogConstants.DOG_NOT_FOUND;
import static com.nathan.dogs.constant.DogConstants.NAME;
import static com.nathan.dogs.constant.DogConstants.SUPPLIER;

@RequiredArgsConstructor
@Service
public class DogService {

    private final DogRepository dogRepository;
    private final SupplierService supplierService;
    private final DogMapper dogMapper;

    public Page<DogOutput> getDogs(Map<String, String> filter, Pageable pageable) {
        if (filter == null || filter.isEmpty()) {
            return mapPage(dogRepository.findByDeletedFalse(pageable));
        }
        // Query Dogs using filter
        if (filter.containsKey(NAME)) {
            return mapPage(findDogsByField(NAME, filter.get(NAME), pageable));
        } else if (filter.containsKey(BREED)) {
            return mapPage(findDogsByField(BREED, filter.get(BREED), pageable));
        } else if (filter.containsKey(SUPPLIER)) {
            return mapPage(findDogsByField(SUPPLIER, filter.get(SUPPLIER), pageable));
        }
        // Return all dogs if no filter
        return mapPage(dogRepository.findByDeletedFalse(pageable));
    }

    public DogOutput getDogById(Long id) {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, DOG_NOT_FOUND));
        return dogMapper.toOutputDto(dog);
    }

    public DogOutput createDog(CreateDogInput input) {
        Dog dog = dogMapper.toEntity(input);

        Supplier supplier = supplierService.findOrCreateSupplier(input.getSupplier());
        dog.setSupplier(supplier);

        Dog savedDog = dogRepository.save(dog);
        return dogMapper.toOutputDto(savedDog);
    }

    public DogOutput updateDog(Long id, UpdateDogInput input) {
        Dog existingDog = dogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, DOG_NOT_FOUND));

        // Map non-null fields from input to entity
        dogMapper.updateEntityFromDto(input, existingDog);

        Dog updatedDog = dogRepository.save(existingDog);
        return dogMapper.toOutputDto(updatedDog);
    }

    public void deleteDog(Long id) {
        Dog dog = dogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, DOG_NOT_FOUND));
        dog.setDeleted(true);
        dogRepository.save(dog);
    }

    private Page<Dog> findDogsByField(String field, String value, Pageable pageable) {
        return switch (field) {
            case NAME -> dogRepository.findByDeletedFalseAndNameContainingIgnoreCase(value, pageable);
            case BREED -> dogRepository.findByDeletedFalseAndBreedContainingIgnoreCase(value, pageable);
            case SUPPLIER -> dogRepository.findByDeletedFalseAndSupplierContainingIgnoreCase(value, pageable);
            default -> dogRepository.findByDeletedFalse(pageable);
        };
    }

    private Page<DogOutput> mapPage(Page<Dog> dogs) {
        return dogs.map(dogMapper::toOutputDto);
    }

}