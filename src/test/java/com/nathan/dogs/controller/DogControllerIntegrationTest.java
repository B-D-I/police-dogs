package com.nathan.dogs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nathan.dogs.dto.CreateDogInput;
import com.nathan.dogs.dto.DogOutput;
import com.nathan.dogs.dto.UpdateDogInput;
import com.nathan.dogs.model.CurrentStatus;
import com.nathan.dogs.service.DogService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DogController.class)
class DogControllerIntegrationTest {

    private static final String DOG_NAME = "Rocky";
    private static final String DOG_BREED = "Labrador";
    private static final String SUPPLIER = "SupplierA";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DogService dogService;

    @Test
    void createDog_ReturnsCreatedDog() throws Exception {
        CreateDogInput input = CreateDogInput.builder()
                .name(DOG_NAME)
                .breed(DOG_BREED)
                .supplier(SUPPLIER)
                .currentStatus(CurrentStatus.IN_SERVICE)
                .build();
        DogOutput dogOutput = DogOutput.builder()
                .name(DOG_NAME)
                .breed(DOG_BREED)
                .build();

        Mockito.when(dogService.createDog(any(CreateDogInput.class))).thenReturn(dogOutput);

        mockMvc.perform(post("/api/dogs/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(DOG_NAME));
    }

    @Test
    void createDog_InvalidInput_ReturnsBadRequest() throws Exception {
        // Missing required fields: name, breed, supplier
        CreateDogInput invalidDog = CreateDogInput.builder()
                .badgeId("B-123")
                .build();

        mockMvc.perform(post("/api/dogs/dogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDog)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    void getAllDogs_ReturnsPagedDogs() throws Exception {
        DogOutput dogOutput = DogOutput.builder()
                .name(DOG_NAME)
                .breed(DOG_BREED)
                .build();
        Page<DogOutput> page = new PageImpl<>(Collections.singletonList(dogOutput));

        Mockito.when(dogService.getDogs(anyMap(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/dogs/dogs")
                        .param("name", DOG_NAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(DOG_NAME));
    }

    @Test
    void getDogById_ReturnsDog() throws Exception {
        DogOutput dogOutput = DogOutput.builder()
                .name(DOG_NAME)
                .breed(DOG_BREED)
                .build();
        Mockito.when(dogService.getDogById(1L)).thenReturn(dogOutput);

        mockMvc.perform(get("/api/dogs/dogs/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(DOG_NAME))
                .andExpect(jsonPath("$.breed").value(DOG_BREED));
    }

    @Test
    void updateDog_ReturnsUpdatedDog() throws Exception {
        UpdateDogInput input = UpdateDogInput.builder().name("Max").breed("Pug").build();
        DogOutput dogOutput = DogOutput.builder().name("Max").breed("Pug").build();

        Mockito.when(dogService.updateDog(eq(1L), any(UpdateDogInput.class))).thenReturn(dogOutput);

        mockMvc.perform(put("/api/dogs/dogs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Max"))
                .andExpect(jsonPath("$.breed").value("Pug"));
    }

    @Test
    void deleteDog_ReturnsNoContent() throws Exception {
        Mockito.doNothing().when(dogService).deleteDog(1L);

        mockMvc.perform(delete("/api/dogs/dogs/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
