package com.group7.straytostay.model;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * One pet listing. Immutable; build it with {@link Builder}.
 */
public final class Pet {

    private final Long id;
    private final long shelterId;
    private final String name;
    private final Species species;
    private final String breed;
    private final Sex sex;
    private final Integer ageMonths;
    private final Size size;
    private final String description;
    private final String medicalNotes;
    private final boolean vaccinated;
    private final boolean neutered;
    private final Boolean goodWithKids;
    private final Boolean goodWithPets;
    private final PetStatus status;
    private final ListingType listingType;
    private final OffsetDateTime createdAt;

    private Pet(Builder b) {
        this.id = b.id;
        this.shelterId = b.shelterId;
        this.name = b.name;
        this.species = b.species;
        this.breed = b.breed;
        this.sex = b.sex;
        this.ageMonths = b.ageMonths;
        this.size = b.size;
        this.description = b.description;
        this.medicalNotes = b.medicalNotes;
        this.vaccinated = b.vaccinated;
        this.neutered = b.neutered;
        this.goodWithKids = b.goodWithKids;
        this.goodWithPets = b.goodWithPets;
        this.status = b.status;
        this.listingType = b.listingType;
        this.createdAt = b.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .id(id).shelterId(shelterId).name(name).species(species).breed(breed).sex(sex)
                .ageMonths(ageMonths).size(size).description(description).medicalNotes(medicalNotes)
                .vaccinated(vaccinated).neutered(neutered).goodWithKids(goodWithKids).goodWithPets(goodWithPets)
                .status(status).listingType(listingType).createdAt(createdAt);
    }

    public Long getId() { return id; }
    public long getShelterId() { return shelterId; }
    public String getName() { return name; }
    public Species getSpecies() { return species; }
    public String getBreed() { return breed; }
    public Sex getSex() { return sex; }
    public Integer getAgeMonths() { return ageMonths; }
    public Size getSize() { return size; }
    public String getDescription() { return description; }
    public String getMedicalNotes() { return medicalNotes; }
    public boolean isVaccinated() { return vaccinated; }
    public boolean isNeutered() { return neutered; }
    public Boolean getGoodWithKids() { return goodWithKids; }
    public Boolean getGoodWithPets() { return goodWithPets; }
    public PetStatus getStatus() { return status; }
    public ListingType getListingType() { return listingType; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    /** Age as text for the views, for example "2 years 3 months" or "Unknown age". */
    public String getAgeDisplay() {
        if (ageMonths == null) {
            return "Unknown age";
        }
        int years = ageMonths / 12;
        int months = ageMonths % 12;
        StringBuilder sb = new StringBuilder();
        if (years > 0) {
            sb.append(years).append(years == 1 ? " year" : " years");
        }
        if (months > 0) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(months).append(months == 1 ? " month" : " months");
        }
        if (sb.length() == 0) {
            sb.append("Under 1 month");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pet)) return false;
        Pet other = (Pet) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pet{id=" + id + ", name='" + name + "', species=" + species + ", status=" + status + '}';
    }

    public static final class Builder {
        private Long id;
        private long shelterId;
        private String name;
        private Species species;
        private String breed;
        private Sex sex = Sex.UNKNOWN;
        private Integer ageMonths;
        private Size size;
        private String description;
        private String medicalNotes;
        private boolean vaccinated;
        private boolean neutered;
        private Boolean goodWithKids;
        private Boolean goodWithPets;
        private PetStatus status = PetStatus.AVAILABLE;
        private ListingType listingType = ListingType.ADOPTION;
        private OffsetDateTime createdAt;

        private Builder() { }

        public Builder id(Long id) { this.id = id; return this; }
        public Builder shelterId(long shelterId) { this.shelterId = shelterId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder species(Species species) { this.species = species; return this; }
        public Builder breed(String breed) { this.breed = breed; return this; }
        public Builder sex(Sex sex) { this.sex = sex; return this; }
        public Builder ageMonths(Integer ageMonths) { this.ageMonths = ageMonths; return this; }
        public Builder size(Size size) { this.size = size; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder medicalNotes(String medicalNotes) { this.medicalNotes = medicalNotes; return this; }
        public Builder vaccinated(boolean vaccinated) { this.vaccinated = vaccinated; return this; }
        public Builder neutered(boolean neutered) { this.neutered = neutered; return this; }
        public Builder goodWithKids(Boolean goodWithKids) { this.goodWithKids = goodWithKids; return this; }
        public Builder goodWithPets(Boolean goodWithPets) { this.goodWithPets = goodWithPets; return this; }
        public Builder status(PetStatus status) { this.status = status; return this; }
        public Builder listingType(ListingType listingType) { this.listingType = listingType; return this; }
        public Builder createdAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }

        /**
         * Checks the required fields and returns the pet.
         * @throws IllegalStateException if a required field is missing or a value is out of range
         */
        public Pet build() {
            if (name == null || name.isBlank()) {
                throw new IllegalStateException("name is required");
            }
            if (name.length() > 80) {
                throw new IllegalStateException("name must be 80 characters or less");
            }
            if (species == null) {
                throw new IllegalStateException("species is required");
            }
            if (shelterId <= 0) {
                throw new IllegalStateException("shelterId is required");
            }
            if (ageMonths != null && ageMonths < 0) {
                throw new IllegalStateException("ageMonths cannot be negative");
            }
            if (sex == null) sex = Sex.UNKNOWN;
            if (status == null) status = PetStatus.AVAILABLE;
            if (listingType == null) listingType = ListingType.ADOPTION;
            return new Pet(this);
        }
    }
}
