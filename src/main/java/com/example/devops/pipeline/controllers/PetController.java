package com.example.devops.pipeline.controllers;

import com.example.devops.pipeline.models.Pet;
import com.example.devops.pipeline.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet")
@CrossOrigin(origins = "*")
public class PetController {

    @Autowired
    PetService petService;

    @PostMapping("/create/{ownerId}")
    public Pet createNewPet(@RequestBody Pet newPet, @PathVariable("ownerId") Long ownerId) {
        Pet pet = this.petService.createPet(newPet, ownerId);
        return pet;
    }

    @GetMapping("/all")
    public List<Pet> getPets() {
        List<Pet> petsList = this.petService.getAllPets();
        return petsList;
    }

    @GetMapping("/{petId}")
    public Pet getPetById(@PathVariable("petId") Long petId) {
        Pet pet = this.petService.getPetById(petId);
        return pet;
    }

}