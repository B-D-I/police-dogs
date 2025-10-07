package com.nathan.dogs.service;

import com.nathan.dogs.model.Supplier;
import com.nathan.dogs.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to handle Supplier entities, ensuring
 * each Dog entity has valid supplier.
 * No current requirement to expose as API.
 */
@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    /**
     * Query a Supplier entity, or create if not exists.
     * Transactional to ensure consistency of suppliers created
     * from Dog API.
     * @param name: of supplier.
     * @return Supplier entity.
     */
    @Transactional
    public Supplier findOrCreateSupplier(String name) {
        return supplierRepository.findByName(name)
                .orElseGet(() -> {
                    Supplier supplier = new Supplier();
                    supplier.setName(name);
                    return supplierRepository.save(supplier);
                });
    }
}