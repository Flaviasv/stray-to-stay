package com.group7.straytostay.dao;

import com.group7.straytostay.model.Shelter;
import com.group7.straytostay.util.DataSourceProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class ShelterDaoImpl implements ShelterDao {

    private static final String COLUMNS =
            "id, owner_user_id, name, description, email, phone, address_line, postal_code, city, province, website";

    @Override
    public Optional<Shelter> findById(long id) {
        return findOne("SELECT " + COLUMNS + " FROM shelters WHERE id = ?", id);
    }

    @Override
    public Optional<Shelter> findByOwner(long userId) {
        return findOne("SELECT " + COLUMNS + " FROM shelters WHERE owner_user_id = ?", userId);
    }

    @Override
    public Shelter insert(Shelter s) {
        String sql = "INSERT INTO shelters (owner_user_id, name, description, email, phone, address_line, "
                + "postal_code, city, province, website) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DataSourceProvider.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(ps, s);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return new Shelter(keys.getLong(1), s.getOwnerUserId(), s.getName(), s.getDescription(),
                        s.getEmail(), s.getPhone(), s.getAddressLine(), s.getPostalCode(), s.getCity(),
                        s.getProvince(), s.getWebsite());
            }
        } catch (SQLException e) {
            throw new DaoException("insert failed for shelter " + s.getName(), e);
        }
    }

    @Override
    public boolean update(Shelter s) {
        String sql = "UPDATE shelters SET owner_user_id = ?, name = ?, description = ?, email = ?, phone = ?, "
                + "address_line = ?, postal_code = ?, city = ?, province = ?, website = ? WHERE id = ?";
        try (Connection c = DataSourceProvider.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, s);
            ps.setLong(11, s.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException("update failed for shelter " + s.getId(), e);
        }
    }

    private Optional<Shelter> findOne(String sql, long param) {
        try (Connection c = DataSourceProvider.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("shelter lookup failed", e);
        }
    }

    private static void bind(PreparedStatement ps, Shelter s) throws SQLException {
        ps.setLong(1, s.getOwnerUserId());
        ps.setString(2, s.getName());
        ps.setString(3, s.getDescription());
        ps.setString(4, s.getEmail());
        ps.setString(5, s.getPhone());
        ps.setString(6, s.getAddressLine());
        ps.setString(7, s.getPostalCode());
        ps.setString(8, s.getCity());
        ps.setString(9, s.getProvince());
        ps.setString(10, s.getWebsite());
    }

    private static Shelter mapRow(ResultSet rs) throws SQLException {
        return new Shelter(rs.getLong("id"), rs.getLong("owner_user_id"), rs.getString("name"),
                rs.getString("description"), rs.getString("email"), rs.getString("phone"),
                rs.getString("address_line"), rs.getString("postal_code"), rs.getString("city"),
                rs.getString("province"), rs.getString("website"));
    }
}
