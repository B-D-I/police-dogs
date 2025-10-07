package com.nathan.dogs.service;

import com.nathan.dogs.dto.CreateDogInput;
import com.nathan.dogs.dto.DogOutput;
import com.nathan.dogs.dto.UpdateDogInput;
import com.nathan.dogs.mapper.DogMapper;
import com.nathan.dogs.model.Dog;
import com.nathan.dogs.model.Supplier;
import com.nathan.dogs.repository.DogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DogServiceTest {

    private static final String DOG_NAME = "Rocky";
    private static final String DOG_BREED = "Labrador";
    private static final String SUPPLIER = "SupplierA";

    @Mock
    private DogRepository dogRepository;

    @Mock
    private DogMapper dogMapper;

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private DogService dogService;

    private Dog dog;
    private DogOutput dogOutput;
    private Supplier supplier;

    @BeforeEach
    void setUp() {
        dog = new Dog();
        dog.setId(1L);
        dog.setName(DOG_NAME);
        dog.setBreed(DOG_BREED);

        dogOutput = DogOutput.builder()
                .name(DOG_NAME)
                .breed(DOG_BREED)
                .build();
    }

    @Test
    void getDogs_ReturnsAllDogs_WhenNoFilter() {
        // Given
        Page<Dog> dogPage = new PageImpl<>(List.of(dog));
        when(dogRepository.findByDeletedFalse(any(Pageable.class))).thenReturn(dogPage);
        when(dogMapper.toOutputDto(any(Dog.class))).thenReturn(dogOutput);

        // When
        Page<DogOutput> result = dogService.getDogs(Collections.emptyMap(), Pageable.unpaged());

        // Then
        assertEquals(1, result.getContent().size());
        verify(dogRepository).findByDeletedFalse(any(Pageable.class));
    }

    @Test
    void getDogById_ReturnsDog_WhenExists() {
        // Given
        when(dogRepository.findById(1L)).thenReturn(Optional.of(dog));
        when(dogMapper.toOutputDto(dog)).thenReturn(dogOutput);

        // When
        DogOutput result = dogService.getDogById(1L);

        // Then
        assertEquals(DOG_NAME, result.getName());
        verify(dogRepository).findById(1L);
    }

    @Test
    void getDogById_ThrowsException_WhenNotFound() {
        // Given
        when(dogRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(ResponseStatusException.class, () -> dogService.getDogById(1L));
    }

    @Test
    void createDog_SavesAndReturnsOutput() {
        // Given
        CreateDogInput input = CreateDogInput.builder()
                .name(DOG_NAME)
                .breed(DOG_BREED)
                .supplier(SUPPLIER)
                .build();

        supplier = new Supplier();
        supplier.setName(SUPPLIER);

        when(dogMapper.toEntity(input)).thenReturn(dog);
        when(dogRepository.save(dog)).thenReturn(dog);
        when(dogMapper.toOutputDto(dog)).thenReturn(dogOutput);
        when(supplierService.findOrCreateSupplier(SUPPLIER)).thenReturn(supplier);

        // When
        DogOutput result = dogService.createDog(input);
        // Then
        assertNotNull(result);
        assertEquals(DOG_NAME, result.getName());
        verify(dogRepository).save(dog);
    }

    @Test
    void updateDog_UpdatesFields_WhenExists() {
        // Given
        UpdateDogInput input = UpdateDogInput.builder().name("Max").breed("Pug").build();

        when(dogRepository.findById(1L)).thenReturn(Optional.of(dog));
        when(dogRepository.save(any(Dog.class))).thenReturn(dog);
        when(dogMapper.toOutputDto(any(Dog.class))).thenReturn(dogOutput);

        // When
        DogOutput result = dogService.updateDog(1L, input);
        // Then
        assertNotNull(result);
        verify(dogRepository).save(any(Dog.class));
    }

    @Test
    void updateDog_ThrowsException_WhenNotFound() {
        // Given
        when(dogRepository.findById(1L)).thenReturn(Optional.empty());
        // Then
        assertThrows(ResponseStatusException.class, () ->
                dogService.updateDog(1L, UpdateDogInput.builder().name("TEST").build()));
    }

    @Test
    void deleteDog_SetsDeletedTrue_WhenFound() {
        // Given
        dog.setDeleted(false);
        when(dogRepository.findById(1L)).thenReturn(Optional.of(dog));

        // When
        dogService.deleteDog(1L);
        // Then
        assertTrue(dog.isDeleted());
        verify(dogRepository).save(dog);
    }

    @Test
    void deleteDog_ThrowsException_WhenNotFound() {
        // Given
        when(dogRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(ResponseStatusException.class, () -> dogService.deleteDog(1L));
    }
}
