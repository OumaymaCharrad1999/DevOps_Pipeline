package com.example.devops.pipeline.services;

import com.example.devops.pipeline.exceptions.ResourceNotFoundException;
import com.example.devops.pipeline.models.Owner;
import com.example.devops.pipeline.models.Pet;
import com.example.devops.pipeline.repositories.OwnerRepository;
import com.example.devops.pipeline.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    OwnerRepository ownerRepository;

    public Pet createPet(Pet pet, Long ownerId) throws ResourceNotFoundException {
        Optional<Owner> ownerData = this.ownerRepository.findById(ownerId);
        if (ownerData.isPresent()) {
            Owner owner = ownerData.orElseThrow(()-> new ResourceNotFoundException("Owner not found"));
            pet.setOwner(owner);
            Pet createdPet = this.petRepository.save(pet);
            return createdPet;
        }
        else {
            throw new  ResourceNotFoundException("No owner found with that ID");
        }
    }

    public List<Pet> getAllPets() throws ResourceNotFoundException {
        List<Pet> pets = this.petRepository.findAll();
        return pets;
    }

    public Pet getPetById(Long petId) throws ResourceNotFoundException {
        Optional<Pet> petData = this.petRepository.findById(petId);
        if (petData.isPresent()) {
            Pet pet = petData.orElseThrow(()-> new ResourceNotFoundException("Owner not found"));
            return pet;
        }
        else {
            throw new  ResourceNotFoundException("Pet with this ID not found");
        }
    }

}