package com.nathan.dogs.service;

import com.nathan.dogs.model.Supplier;
import com.nathan.dogs.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    private static final String SUPPLIER = "SupplierA";

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;


    @Test
    void findOrCreateSupplier_WhenSupplierExists_ReturnsExistingSupplier() {
        // Given
        Supplier existingSupplier = new Supplier();
        existingSupplier.setId(1L);
        existingSupplier.setName(SUPPLIER);

        when(supplierRepository.findByName(SUPPLIER)).thenReturn(Optional.of(existingSupplier));

        // When
        Supplier result = supplierService.findOrCreateSupplier(SUPPLIER);

        // Then
        assertNotNull(result);
        assertEquals(SUPPLIER, result.getName());
        assertEquals(1L, result.getId());
        verify(supplierRepository, times(1)).findByName(SUPPLIER);
        verify(supplierRepository, never()).save(any(Supplier.class));
    }

    @Test
    void findOrCreateSupplier_WhenSupplierDoesNotExist_CreatesNewSupplier() {
        // Given
        when(supplierRepository.findByName(SUPPLIER)).thenReturn(Optional.empty());

        Supplier newSupplier = new Supplier();
        newSupplier.setId(10L);
        newSupplier.setName(SUPPLIER);

        when(supplierRepository.save(any(Supplier.class))).thenReturn(newSupplier);

        // When
        Supplier result = supplierService.findOrCreateSupplier(SUPPLIER);

        // Then
        assertNotNull(result);
        assertEquals(SUPPLIER, result.getName());
        assertEquals(10L, result.getId());
        verify(supplierRepository, times(1)).findByName(SUPPLIER);
        verify(supplierRepository, times(1)).save(any(Supplier.class));
    }
}