package com.group7.straytostay.model;

/**
 * Filters and paging for the pet search. A null filter means "not applied".
 * Page numbers start at 1. Values outside the allowed range are clamped.
 */
public final class PetSearchCriteria {

    public static final int DEFAULT_PAGE_SIZE = 12;
    public static final int MAX_PAGE_SIZE = 48;

    private Species species;
    private Size size;
    private String breed;
    private String city;
    private Integer maxAgeMonths;
    private Boolean goodWithKids;
    private int page = 1;
    private int pageSize = DEFAULT_PAGE_SIZE;

    public Species getSpecies() { return species; }
    public void setSpecies(Species species) { this.species = species; }

    public Size getSize() { return size; }
    public void setSize(Size size) { this.size = size; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = blankToNull(breed); }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = blankToNull(city); }

    public Integer getMaxAgeMonths() { return maxAgeMonths; }
    public void setMaxAgeMonths(Integer maxAgeMonths) {
        this.maxAgeMonths = (maxAgeMonths == null || maxAgeMonths < 0) ? null : maxAgeMonths;
    }

    public Boolean getGoodWithKids() { return goodWithKids; }
    public void setGoodWithKids(Boolean goodWithKids) { this.goodWithKids = goodWithKids; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = Math.max(1, page); }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) {
        this.pageSize = Math.min(MAX_PAGE_SIZE, Math.max(1, pageSize));
    }

    public int getOffset() {
        return (page - 1) * pageSize;
    }

    public boolean hasFilters() {
        return species != null || size != null || breed != null || city != null
                || maxAgeMonths != null || goodWithKids != null;
    }

    private static String blankToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
