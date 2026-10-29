package com.group7.straytostay.model;

public final class User {

    private final Long id;
    private final String email;
    private final String passwordHash;
    private final String fullName;
    private final Role role;
    private final String city;
    private final String province;
    private final boolean active;

    public User(Long id, String email, String passwordHash, String fullName, Role role,
                String city, String province, boolean active) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.city = city;
        this.province = province;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getFullName() { return fullName; }
    public Role getRole() { return role; }
    public String getCity() { return city; }
    public String getProvince() { return province; }
    public boolean isActive() { return active; }

    public boolean isShelterStaff() {
        return role == Role.SHELTER_STAFF || role == Role.ADMIN;
    }
}
