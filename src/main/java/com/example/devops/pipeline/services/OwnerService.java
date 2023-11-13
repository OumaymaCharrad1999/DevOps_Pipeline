package com.example.devops.pipeline.services;

import com.example.devops.pipeline.exceptions.ResourceNotFoundException;
import com.example.devops.pipeline.models.Owner;
import com.example.devops.pipeline.repositories.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    @Autowired
    OwnerRepository ownerRepository;

    public Owner createOwner(Owner owner) throws ResourceNotFoundException {
        Owner createdOwner = this.ownerRepository.save(owner);
        return createdOwner;
    }

    public List<Owner> getAllOwners() throws ResourceNotFoundException {
        List<Owner> owners = this.ownerRepository.findAll();
        return owners;
    }

    public Owner getOwnerById(Long ownerId) throws ResourceNotFoundException {
        Optional<Owner> ownerData = this.ownerRepository.findById(ownerId);
        if (ownerData.isPresent()) {
            Owner owner = ownerData.orElseThrow(()-> new ResourceNotFoundException("Owner not found"));
            return owner;
        }
        else {
            throw new ResourceNotFoundException("Owner with this ID not found");
        }
    }

    public void deleteOwner(Long ownerId) throws ResourceNotFoundException {
        if (!ownerRepository.existsById(ownerId)) {
            throw new ResourceNotFoundException("Owner not found with ID: " + ownerId);
        }
        ownerRepository.deleteById(ownerId);
    }

}