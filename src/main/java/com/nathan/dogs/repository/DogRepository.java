package com.nathan.dogs.repository;

import com.nathan.dogs.model.Dog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DogRepository extends JpaRepository<Dog, Long> {

    Page<Dog> findByDeletedFalse(Pageable pageable);

    Page<Dog> findByDeletedFalseAndNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Dog> findByDeletedFalseAndBreedContainingIgnoreCase(String breed, Pageable pageable);

    @Query("""
    SELECT DISTINCT d
    FROM Dog d
    JOIN d.supplier s
    WHERE d.deleted = false
      AND LOWER(s) LIKE LOWER(CONCAT('%', :supplier, '%'))
""")
    Page<Dog> findByDeletedFalseAndSupplierContainingIgnoreCase(@Param("supplier") String supplier, Pageable pageable);

}
