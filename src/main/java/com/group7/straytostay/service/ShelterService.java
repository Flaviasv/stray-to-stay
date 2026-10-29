package com.group7.straytostay.service;

import com.group7.straytostay.dao.DaoFactory;
import com.group7.straytostay.dao.ShelterDao;
import com.group7.straytostay.model.Shelter;
import com.group7.straytostay.model.User;

import java.util.Optional;

public class ShelterService {

    private final ShelterDao shelterDao;

    public ShelterService() {
        this(DaoFactory.shelterDao());
    }

    public ShelterService(ShelterDao shelterDao) {
        this.shelterDao = shelterDao;
    }

    public Optional<Shelter> findById(long id) {
        return shelterDao.findById(id);
    }

    public Optional<Shelter> findByOwner(long userId) {
        return shelterDao.findByOwner(userId);
    }

    public boolean isOwner(User user, long shelterId) {
        if (user == null || user.getId() == null || !user.isShelterStaff()) {
            return false;
        }
        return shelterDao.findById(shelterId)
                .map(s -> s.getOwnerUserId() == user.getId())
                .orElse(false);
    }
}
