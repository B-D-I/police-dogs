package com.nathan.dogs.controller;

import com.nathan.dogs.dto.CreateDogInput;
import com.nathan.dogs.dto.DogOutput;
import com.nathan.dogs.dto.UpdateDogInput;
import com.nathan.dogs.service.DogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dogs")
public class DogController {

    private final DogService dogService;

    /**
     * Query all dogs
     * @param filter: query parameter for searching by:
     *              - name
     *              - breed
     *              - supplier
     * @param pageable: pagination query params.
     * @return: queried dog data.
     */
    @GetMapping("/dogs")
    public ResponseEntity<Page<DogOutput>> getAllDogs(
            // map of provided search parameters
            @RequestParam Map<String, String> filter,
            Pageable pageable
    ) {
        Page<DogOutput> dogs = dogService.getDogs(filter, pageable);
        return ResponseEntity.ok(dogs);
    }

    /**
     * Query dogs by entity id.
     * @param id: id of the dog entity.
     * @return the queried dog.
     */
    @GetMapping("/dogs/{id}")
    public ResponseEntity<DogOutput> getDogById(@PathVariable Long id) {
        DogOutput dog = dogService.getDogById(id);
        return ResponseEntity.ok(dog);
    }

    /**
     * Create a dog entity.
     * @param dog: dog data.
     * @return the created dog object, on success.
     */
    @PostMapping("/dogs")
    public ResponseEntity<DogOutput> createDog(@Valid @RequestBody CreateDogInput dog) {
        DogOutput createdDog = dogService.createDog(dog);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDog);
    }

    /**
     * Updates a dog entity.
     * Note: A dog is associated to a single supplier,
     * and this cannot be updated. A new Dog would need
     * to be created for this scenario.
     * @param id: id of the dog entity.
     * @param dog: provided dog fields for update .
     * @return the updated dog object, on success.
     */
    @PutMapping("/dogs/{id}")
    public ResponseEntity<DogOutput> updateDog(@PathVariable Long id, @RequestBody UpdateDogInput dog) {
        DogOutput updatedDog = dogService.updateDog(id, dog);
        return ResponseEntity.ok(updatedDog);
    }

    /**
     * Mark a dog entity for deletion. The entity remains persisted,
     * but a deleted flag is set.
     * @param id: id of the dog entity.
     * @return 204 response if successful, or 404 if not found.
     */
    @DeleteMapping("/dogs/{id}")
    public ResponseEntity<Void> deleteDog(@PathVariable Long id) {
        dogService.deleteDog(id);
        return ResponseEntity.noContent().build();
    }

}
