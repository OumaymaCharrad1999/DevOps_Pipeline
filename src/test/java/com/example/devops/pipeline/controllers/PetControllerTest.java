package com.example.devops.pipeline.controllers;

import com.example.devops.pipeline.models.Pet;
import com.example.devops.pipeline.services.PetService;
import com.example.devops.pipeline.utils.objectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PetControllerTest {

    @MockBean
    private PetService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /pet/create")
    void testCreatePet() throws Exception {
        // Set up our mocked service
        Long ownerId = 1l ;
        Pet petToCreate = new Pet("Pika","Pika description" );
        Pet petToReturn = new Pet(1L, "Pika","Pika description");
        doReturn(petToReturn).when(service).createPet(petToCreate,ownerId);

        // Execute the POST request
        mockMvc.perform(post("/pet/create/{id}", ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.asJsonString(petToCreate)))
                        // Validate the response code and content type
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /all success")
    void testGetPetsSuccess() throws Exception {
        Pet pet1 = new Pet(1l, "Pika", "Description of Pika test");
        Pet pet2 = new Pet(2l, "Flash", "Description of Flash test");
        doReturn(Lists.newArrayList(pet1, pet2)).when(service).getAllPets();

        mockMvc.perform(get("/pet/all"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Pika")))
                .andExpect(jsonPath("$[0].description", is("Description of Pika test")))
                .andExpect(jsonPath("$[1].name", is("Flash")))
                .andExpect(jsonPath("$[1].description", is("Description of Flash test")));
    }

    @Test
    @DisplayName("GET /pet/1")
    void testGetPetById() throws Exception {
        // Set up our mocked service
        Pet pet = new Pet(1l, "Pika", "Description of Pika test");
        doReturn(pet).when(service).getPetById(1l);

        // Execute the GET request
        mockMvc.perform(get("/pet/{id}", 1L))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Pika")))
                .andExpect(jsonPath("$.description", is("Description of Pika test")));
    }

}