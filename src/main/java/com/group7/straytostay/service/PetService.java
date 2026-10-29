package com.group7.straytostay.service;

import com.group7.straytostay.dao.DaoFactory;
import com.group7.straytostay.dao.PetDao;
import com.group7.straytostay.model.PageResult;
import com.group7.straytostay.model.Pet;
import com.group7.straytostay.model.PetSearchCriteria;
import com.group7.straytostay.model.PetStatus;
import com.group7.straytostay.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Business rules for listings. Knows nothing about HTTP.
 */
public class PetService {

    private final PetDao petDao;

    public PetService() {
        this(DaoFactory.petDao());
    }

    public PetService(PetDao petDao) {
        this.petDao = petDao;
    }

    /** One page of available pets matching the criteria, newest first. */
    public PageResult<Pet> search(PetSearchCriteria criteria) {
        List<Pet> items = petDao.search(criteria);
        int total = petDao.countSearch(criteria);
        return new PageResult<>(items, criteria.getPage(), criteria.getPageSize(), total);
    }

    public Optional<Pet> findById(long id) {
        return petDao.findById(id);
    }

    public List<Pet> findByShelter(long shelterId) {
        return petDao.findByShelter(shelterId);
    }

    /** Creates a listing for the staff member's own shelter. */
    public Pet create(Pet pet, User staff, long ownShelterId) {
        requireOwner(pet.getShelterId(), staff, ownShelterId);
        return petDao.insert(pet);
    }

    public boolean update(Pet pet, User staff, long ownShelterId) {
        requireOwner(pet.getShelterId(), staff, ownShelterId);
        return petDao.update(pet);
    }

    public boolean changeStatus(long petId, PetStatus status, User staff, long ownShelterId) {
        Pet pet = petDao.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("pet not found: " + petId));
        requireOwner(pet.getShelterId(), staff, ownShelterId);
        return petDao.updateStatus(petId, status);
    }

    private static void requireOwner(long petShelterId, User staff, long ownShelterId) {
        if (staff == null || !staff.isShelterStaff() || petShelterId != ownShelterId) {
            throw new SecurityException("user may only manage listings of their own shelter");
        }
    }
}
