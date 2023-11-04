package com.example.devops.pipeline.controllers;

import com.example.devops.pipeline.models.Owner;
import com.example.devops.pipeline.services.OwnerService;
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
public class OwnerControllerTest {

    @MockBean
    private OwnerService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /owner/create")
    void testCreateWidget() throws Exception {
        // Set up our mocked service
        Owner ownerToCreate = new Owner("Oumayma",20);
        Owner ownerToReturn = new Owner(1L, "Oumayma",20);
        doReturn(ownerToReturn).when(service).createOwner(ownerToCreate);

        // Execute the POST request
        mockMvc.perform(post("/owner/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.asJsonString(ownerToCreate)))
                        // Validate the response code and content type
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /all success")
    void testGetOwnersSuccess() throws Exception {
        Owner owner1 = new Owner("Oumayma", 24);
        Owner owner2 = new Owner("Oumayma", 25);
        doReturn(Lists.newArrayList(owner1, owner2)).when(service).getAllOwners();

        mockMvc.perform(get("/owner/all"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Oumayma")))
                .andExpect(jsonPath("$[0].age", is(24)))
                .andExpect(jsonPath("$[1].name", is("Oumayma")))
                .andExpect(jsonPath("$[1].age", is(25)));
    }

    @Test
    @DisplayName("GET /owner/1")
    void testGetOwnerById() throws Exception {
        // Set up our mocked service
        Owner owner = new Owner(1l,"Oumayma", 24);
        doReturn(owner).when(service).getOwnerById(1l);

        // Execute the GET request
        mockMvc.perform(get("/owner/{id}", 1L))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Oumayma")))
                .andExpect(jsonPath("$.age", is(24)));
    }

}