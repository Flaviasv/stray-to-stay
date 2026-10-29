package com.group7.straytostay.dao;

import com.group7.straytostay.model.Pet;
import com.group7.straytostay.model.PetSearchCriteria;
import com.group7.straytostay.model.PetStatus;

import java.util.List;
import java.util.Optional;

public interface PetDao {

    Optional<Pet> findById(long id);

    /** Available pets matching the criteria, newest first, one page. */
    List<Pet> search(PetSearchCriteria criteria);

    /** Number of available pets matching the criteria, ignoring paging. */
    int countSearch(PetSearchCriteria criteria);

    List<Pet> findByShelter(long shelterId);

    /** Inserts the pet and returns a copy with the generated id. */
    Pet insert(Pet pet);

    boolean update(Pet pet);

    boolean updateStatus(long id, PetStatus status);
}
