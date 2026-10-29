package com.group7.straytostay.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PetTest {

    private static Pet.Builder valid() {
        return Pet.builder().shelterId(1).name("Biscuit").species(Species.DOG);
    }

    @Test
    void buildsWithRequiredFieldsAndDefaults() {
        Pet pet = valid().build();
        assertEquals("Biscuit", pet.getName());
        assertEquals(Sex.UNKNOWN, pet.getSex());
        assertEquals(PetStatus.AVAILABLE, pet.getStatus());
        assertEquals(ListingType.ADOPTION, pet.getListingType());
    }

    @Test
    void rejectsMissingName() {
        assertThrows(IllegalStateException.class, () -> valid().name("  ").build());
    }

    @Test
    void rejectsMissingSpecies() {
        assertThrows(IllegalStateException.class, () -> valid().species(null).build());
    }

    @Test
    void rejectsMissingShelter() {
        assertThrows(IllegalStateException.class, () -> valid().shelterId(0).build());
    }

    @Test
    void rejectsNegativeAge() {
        assertThrows(IllegalStateException.class, () -> valid().ageMonths(-1).build());
    }

    @Test
    void ageDisplayFormatsYearsAndMonths() {
        assertEquals("Unknown age", valid().build().getAgeDisplay());
        assertEquals("Under 1 month", valid().ageMonths(0).build().getAgeDisplay());
        assertEquals("7 months", valid().ageMonths(7).build().getAgeDisplay());
        assertEquals("1 year", valid().ageMonths(12).build().getAgeDisplay());
        assertEquals("2 years 3 months", valid().ageMonths(27).build().getAgeDisplay());
    }

    @Test
    void toBuilderKeepsEveryField() {
        Pet pet = valid().id(5L).breed("Beagle").size(Size.MEDIUM).vaccinated(true).goodWithKids(true).build();
        Pet copy = pet.toBuilder().build();
        assertEquals(pet.getId(), copy.getId());
        assertEquals("Beagle", copy.getBreed());
        assertEquals(Size.MEDIUM, copy.getSize());
        assertEquals(true, copy.isVaccinated());
        assertEquals(Boolean.TRUE, copy.getGoodWithKids());
    }
}
