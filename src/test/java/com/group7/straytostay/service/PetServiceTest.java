package com.group7.straytostay.service;

import com.group7.straytostay.dao.PetDao;
import com.group7.straytostay.model.PageResult;
import com.group7.straytostay.model.Pet;
import com.group7.straytostay.model.PetSearchCriteria;
import com.group7.straytostay.model.PetStatus;
import com.group7.straytostay.model.Role;
import com.group7.straytostay.model.Species;
import com.group7.straytostay.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PetServiceTest {

    private final PetDao dao = mock(PetDao.class);
    private final PetService service = new PetService(dao);

    private static Pet pet(long shelterId) {
        return Pet.builder().id(10L).shelterId(shelterId).name("Luna").species(Species.CAT).build();
    }

    private static User staff() {
        return new User(1L, "staff@example.com", "hash", "Sam", Role.SHELTER_STAFF, null, null, true);
    }

    @Test
    void searchReturnsItemsAndTotal() {
        PetSearchCriteria c = new PetSearchCriteria();
        c.setPage(2);
        when(dao.search(c)).thenReturn(List.of(pet(1)));
        when(dao.countSearch(c)).thenReturn(25);

        PageResult<Pet> page = service.search(c);

        assertEquals(1, page.getItems().size());
        assertEquals(25, page.getTotal());
        assertEquals(2, page.getPage());
        assertEquals(3, page.getTotalPages());
    }

    @Test
    void staffCanCreateForOwnShelter() {
        when(dao.insert(any())).thenAnswer(inv -> inv.getArgument(0));
        Pet created = service.create(pet(1), staff(), 1);
        assertEquals("Luna", created.getName());
        verify(dao).insert(any());
    }

    @Test
    void staffCannotCreateForAnotherShelter() {
        assertThrows(SecurityException.class, () -> service.create(pet(2), staff(), 1));
        verify(dao, never()).insert(any());
    }

    @Test
    void adopterCannotCreate() {
        User adopter = new User(2L, "a@example.com", "hash", "Alex", Role.ADOPTER, null, null, true);
        assertThrows(SecurityException.class, () -> service.create(pet(1), adopter, 1));
    }

    @Test
    void changeStatusChecksOwnership() {
        when(dao.findById(10L)).thenReturn(Optional.of(pet(1)));
        when(dao.updateStatus(10L, PetStatus.ADOPTED)).thenReturn(true);

        assertTrue(service.changeStatus(10L, PetStatus.ADOPTED, staff(), 1));
        assertThrows(SecurityException.class, () -> service.changeStatus(10L, PetStatus.ADOPTED, staff(), 2));
    }
}
