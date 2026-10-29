package com.group7.straytostay.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PageResultTest {

    @Test
    void computesTotalPages() {
        assertEquals(1, new PageResult<>(List.of(), 1, 12, 0).getTotalPages());
        assertEquals(1, new PageResult<>(List.of(), 1, 12, 12).getTotalPages());
        assertEquals(2, new PageResult<>(List.of(), 1, 12, 13).getTotalPages());
        assertEquals(3, new PageResult<>(List.of(), 1, 12, 36).getTotalPages());
    }

    @Test
    void knowsNeighbours() {
        PageResult<String> middle = new PageResult<>(List.of("a"), 2, 12, 36);
        assertTrue(middle.isHasPrevious());
        assertTrue(middle.isHasNext());
        PageResult<String> last = new PageResult<>(List.of("a"), 3, 12, 36);
        assertFalse(last.isHasNext());
    }
}
