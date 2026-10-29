package com.group7.straytostay.dao;

import com.group7.straytostay.model.ListingType;
import com.group7.straytostay.model.Pet;
import com.group7.straytostay.model.PetSearchCriteria;
import com.group7.straytostay.model.PetStatus;
import com.group7.straytostay.model.Sex;
import com.group7.straytostay.model.Size;
import com.group7.straytostay.model.Species;
import com.group7.straytostay.util.DataSourceProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation. Every query is a PreparedStatement; user input is never
 * concatenated into SQL.
 */
public class PetDaoImpl implements PetDao {

    private static final String COLUMNS =
            "p.id, p.shelter_id, p.name, p.species, p.breed, p.sex, p.age_months, p.size, "
            + "p.description, p.medical_notes, p.vaccinated, p.neutered, p.good_with_kids, "
            + "p.good_with_pets, p.status, p.listing_type, p.created_at";

    @Override
    public Optional<Pet> findById(long id) {
        String sql = "SELECT " + COLUMNS + " FROM pets p WHERE p.id = ?";
        try (Connection c = DataSourceProvider.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("findById failed for pet " + id, e);
        }
    }

    @Override
    public List<Pet> search(PetSearchCriteria criteria) {
        Filter filter = buildFilter(criteria);
        String sql = "SELECT " + COLUMNS + " FROM pets p JOIN shelters s ON s.id = p.shelter_id "
                + filter.where + " ORDER BY p.created_at DESC, p.id DESC LIMIT ? OFFSET ?";
        try (Connection c = DataSourceProvider.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            int next = bind(ps, filter.values);
            ps.setInt(next++, criteria.getPageSize());
            ps.setInt(next, criteria.getOffset());
            try (ResultSet rs = ps.executeQuery()) {
                List<Pet> pets = new ArrayList<>();
                while (rs.next()) {
                    pets.add(mapRow(rs));
                }
                return pets;
            }
        } catch (SQLException e) {
            throw new DaoException("search failed", e);
        }
    }

    @Override
    public int countSearch(PetSearchCriteria criteria) {
        Filter filter = buildFilter(criteria);
        String sql = "SELECT COUNT(*) FROM pets p JOIN shelters s ON s.id = p.shelter_id " + filter.where;
        try (Connection c = DataSourceProvider.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, filter.values);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new DaoException("countSearch failed", e);
        }
    }

    @Override
    public List<Pet> findByShelter(long shelterId) {
        String sql = "SELECT " + COLUMNS + " FROM pets p WHERE p.shelter_id = ? ORDER BY p.created_at DESC";
        try (Connection c = DataSourceProvider.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, shelterId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Pet> pets = new ArrayList<>();
                while (rs.next()) {
                    pets.add(mapRow(rs));
                }
                return pets;
            }
        } catch (SQLException e) {
            throw new DaoException("findByShelter failed for shelter " + shelterId, e);
        }
    }

    @Override
    public Pet insert(Pet pet) {
        String sql = "INSERT INTO pets (shelter_id, name, species, breed, sex, age_months, size, description, "
                + "medical_notes, vaccinated, neutered, good_with_kids, good_with_pets, status, listing_type) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DataSourceProvider.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindPet(ps, pet);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return pet.toBuilder().id(keys.getLong(1)).build();
            }
        } catch (SQLException e) {
            throw new DaoException("insert failed for pet " + pet.getName(), e);
        }
    }

    @Override
    public boolean update(Pet pet) {
        String sql = "UPDATE pets SET shelter_id = ?, name = ?, species = ?, breed = ?, sex = ?, age_months = ?, "
                + "size = ?, description = ?, medical_notes = ?, vaccinated = ?, neutered = ?, good_with_kids = ?, "
                + "good_with_pets = ?, status = ?, listing_type = ?, updated_at = NOW() WHERE id = ?";
        try (Connection c = DataSourceProvider.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            bindPet(ps, pet);
            ps.setLong(16, pet.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException("update failed for pet " + pet.getId(), e);
        }
    }

    @Override
    public boolean updateStatus(long id, PetStatus status) {
        String sql = "UPDATE pets SET status = ?, updated_at = NOW() WHERE id = ?";
        try (Connection c = DataSourceProvider.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setLong(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException("updateStatus failed for pet " + id, e);
        }
    }

    // ---- helpers ----------------------------------------------------------

    /** WHERE clause plus the values to bind, built only from filters that are set. */
    private static Filter buildFilter(PetSearchCriteria cr) {
        StringBuilder where = new StringBuilder("WHERE p.status = ?");
        List<Object> values = new ArrayList<>();
        values.add(PetStatus.AVAILABLE.name());

        if (cr.getSpecies() != null) {
            where.append(" AND p.species = ?");
            values.add(cr.getSpecies().name());
        }
        if (cr.getSize() != null) {
            where.append(" AND p.size = ?");
            values.add(cr.getSize().name());
        }
        if (cr.getBreed() != null) {
            where.append(" AND p.breed ILIKE ?");
            values.add("%" + cr.getBreed() + "%");
        }
        if (cr.getCity() != null) {
            where.append(" AND s.city ILIKE ?");
            values.add(cr.getCity());
        }
        if (cr.getMaxAgeMonths() != null) {
            where.append(" AND p.age_months <= ?");
            values.add(cr.getMaxAgeMonths());
        }
        if (cr.getGoodWithKids() != null) {
            where.append(" AND p.good_with_kids = ?");
            values.add(cr.getGoodWithKids());
        }
        return new Filter(where.toString(), values);
    }

    /** Binds the values in order and returns the next free parameter index. */
    private static int bind(PreparedStatement ps, List<Object> values) throws SQLException {
        int i = 1;
        for (Object v : values) {
            ps.setObject(i++, v);
        }
        return i;
    }

    private static void bindPet(PreparedStatement ps, Pet p) throws SQLException {
        ps.setLong(1, p.getShelterId());
        ps.setString(2, p.getName());
        ps.setString(3, p.getSpecies().name());
        ps.setString(4, p.getBreed());
        ps.setString(5, p.getSex().name());
        if (p.getAgeMonths() == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, p.getAgeMonths());
        ps.setString(7, p.getSize() == null ? null : p.getSize().name());
        ps.setString(8, p.getDescription());
        ps.setString(9, p.getMedicalNotes());
        ps.setBoolean(10, p.isVaccinated());
        ps.setBoolean(11, p.isNeutered());
        if (p.getGoodWithKids() == null) ps.setNull(12, Types.BOOLEAN); else ps.setBoolean(12, p.getGoodWithKids());
        if (p.getGoodWithPets() == null) ps.setNull(13, Types.BOOLEAN); else ps.setBoolean(13, p.getGoodWithPets());
        ps.setString(14, p.getStatus().name());
        ps.setString(15, p.getListingType().name());
    }

    private static Pet mapRow(ResultSet rs) throws SQLException {
        int age = rs.getInt("age_months");
        Integer ageMonths = rs.wasNull() ? null : age;
        boolean kids = rs.getBoolean("good_with_kids");
        Boolean goodWithKids = rs.wasNull() ? null : kids;
        boolean pets = rs.getBoolean("good_with_pets");
        Boolean goodWithPets = rs.wasNull() ? null : pets;
        String size = rs.getString("size");

        return Pet.builder()
                .id(rs.getLong("id"))
                .shelterId(rs.getLong("shelter_id"))
                .name(rs.getString("name"))
                .species(Species.valueOf(rs.getString("species")))
                .breed(rs.getString("breed"))
                .sex(Sex.valueOf(rs.getString("sex")))
                .ageMonths(ageMonths)
                .size(size == null ? null : Size.valueOf(size))
                .description(rs.getString("description"))
                .medicalNotes(rs.getString("medical_notes"))
                .vaccinated(rs.getBoolean("vaccinated"))
                .neutered(rs.getBoolean("neutered"))
                .goodWithKids(goodWithKids)
                .goodWithPets(goodWithPets)
                .status(PetStatus.valueOf(rs.getString("status")))
                .listingType(ListingType.valueOf(rs.getString("listing_type")))
                .createdAt(rs.getObject("created_at", OffsetDateTime.class))
                .build();
    }

    private static final class Filter {
        final String where;
        final List<Object> values;

        Filter(String where, List<Object> values) {
            this.where = where;
            this.values = values;
        }
    }
}
