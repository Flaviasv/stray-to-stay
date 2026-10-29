package com.group7.straytostay.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PetSearchCriteriaTest {

    @Test
    void defaultsToFirstPageOfTwelve() {
        PetSearchCriteria c = new PetSearchCriteria();
        assertEquals(1, c.getPage());
        assertEquals(12, c.getPageSize());
        assertEquals(0, c.getOffset());
        assertFalse(c.hasFilters());
    }

    @Test
    void clampsPageAndPageSize() {
        PetSearchCriteria c = new PetSearchCriteria();
        c.setPage(-3);
        c.setPageSize(1000);
        assertEquals(1, c.getPage());
        assertEquals(PetSearchCriteria.MAX_PAGE_SIZE, c.getPageSize());
        c.setPageSize(0);
        assertEquals(1, c.getPageSize());
    }

    @Test
    void offsetFollowsPage() {
        PetSearchCriteria c = new PetSearchCriteria();
        c.setPage(3);
        assertEquals(24, c.getOffset());
    }

    @Test
    void blankTextFiltersAreIgnored() {
        PetSearchCriteria c = new PetSearchCriteria();
        c.setBreed("   ");
        c.setCity("");
        assertNull(c.getBreed());
        assertNull(c.getCity());
        assertFalse(c.hasFilters());
        c.setBreed(" Beagle ");
        assertEquals("Beagle", c.getBreed());
        assertTrue(c.hasFilters());
    }

    @Test
    void negativeMaxAgeIsIgnored() {
        PetSearchCriteria c = new PetSearchCriteria();
        c.setMaxAgeMonths(-5);
        assertNull(c.getMaxAgeMonths());
    }
}
