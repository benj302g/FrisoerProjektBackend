package com.example.frisoerprojektbackend.service;

import com.example.frisoerprojektbackend.exception.ResourceAlreadyExistsException;
import com.example.frisoerprojektbackend.exception.ResourceNotFoundException;
import com.example.frisoerprojektbackend.model.Treatment;
import com.example.frisoerprojektbackend.repository.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreatmentService {

    @Autowired
    TreatmentRepository treatmentRepository;


    public List<Treatment> getAllTreatments() {
        return treatmentRepository.findAll();
    }

    public Treatment getTreatmentById(int id) {
        return treatmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Could not find a treatment with the ID: " + id + " from the database"));
    }

    public Treatment getTreatmentByName(String name) {
        return treatmentRepository.findTreatmentByName(name).orElseThrow(
                () -> new ResourceNotFoundException("There is no treatment with the name of: " + name)
        );
    }

    public ResponseEntity<Treatment> addTreatment(Treatment treatment) {
        int treatmentId = treatment.getId();
        String treatmentName = treatment.getName();

        boolean idExists = treatmentRepository.existsById(treatmentId);
        boolean nameExists = treatmentRepository.existsByName(treatmentName);
        boolean hasNoName = treatmentName.isBlank();
        boolean hasNoPrice = treatment.getPrice() <= 0;


        if (idExists) {
            throw new ResourceAlreadyExistsException("There already exists a treatment with the ID: " + treatmentId + " and therefore can not be added to the database");
        }
        if (nameExists) {
            throw new ResourceAlreadyExistsException("There already exists a treatment with the name: " + treatmentName + " and therefore can not be added to the database");
        }
        if (hasNoName) {
            throw new ResourceNotFoundException("It is not possible to add a treatment, that has no name, to the database");
        }
        if (hasNoPrice) {
            throw new ResourceNotFoundException("It is not possible to add a treatment, with a price that is 0 or lower, to the database");
        }
        Treatment addedTreatment = treatmentRepository.save(treatment);
        return new ResponseEntity<>(addedTreatment, HttpStatus.OK);
    }

    public ResponseEntity<Treatment> updateTreatment(Treatment treatment) {
        // Vi skaffer id fra den treatment objekt vi modtager.
        int treatmentId = treatment.getId();
        // og navn
        String treatmentName = treatment.getName();

        boolean idIsUnknown = !treatmentRepository.existsById(treatmentId);
        // Vi tjekker om den treatments id eksistere, hvis ikke, thrower vi en exception
        if (idIsUnknown) {
            throw new ResourceNotFoundException("It is not possible to update a treatment with the id: " + treatmentId + " as it does not exist in the database");
        }
        // Vi går ind og finder den treatment der allerede eksistere i databasen ud fra den treatment vi modtagers id.
        // pastTreatment er hvad den behandling nuværende hedder inde i databasen.
        Treatment pastTreatment = getTreatmentById(treatmentId);
        String pastName = pastTreatment.getName();

        boolean nameExists = treatmentRepository.existsByName(treatmentName);
        boolean differentNames = !pastName.equals(treatmentName);

        // Hvis navnet på treatmenten man ønsker at update allerede eksisterer, men det er forskelligt for det navn treatmenten havde før, så må det være en anden treatment der allerede har det navn og der sendes en fejl tilbage.
        // Hvis navnet ikke allerede eksisterer, så kører koden, men hvis navnet eksisterer og det er det samme som det var før, så kører koden også
        // Hvis navnet allerede findes i databasen, og navnet vi prøver at give vores treatment vi skal update er nyt, dvs ikke det samme som pastName, så overwriter og saver vi det nye navn.
        // Hvis navnet allerede eksistere, dvs det nye navn vi ændrer vores treatment til er det samme som pastName (det den allerede hed), så saver vi også navnet.
        if (nameExists && differentNames) {
            throw new ResourceAlreadyExistsException("It is not possible to update a treatment with the name of: " + treatmentName + " as there already exists a treatment with that name in the database");
        }

        boolean hasNoName = treatmentName == null || treatmentName.isBlank();
        if (hasNoName) {
            throw new ResourceNotFoundException("It is not possible to update the treatment with the id: " + treatmentId + " without giving it a name");
        }
        boolean hasNoPrice = treatment.getPrice() <= 0;
        if (hasNoPrice) {
            throw new ResourceNotFoundException("It is not possible to update the treatment with the id: " + treatmentId + " without giving it a price higher than 0");
        }

        Treatment updatedTreatment = treatmentRepository.save(treatment);
        return new ResponseEntity<>(updatedTreatment, HttpStatus.OK);
    }
}