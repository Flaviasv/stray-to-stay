package com.group7.straytostay.dao;

import com.group7.straytostay.model.Shelter;

import java.util.Optional;

public interface ShelterDao {

    Optional<Shelter> findById(long id);

    Optional<Shelter> findByOwner(long userId);

    Shelter insert(Shelter shelter);

    boolean update(Shelter shelter);
}
