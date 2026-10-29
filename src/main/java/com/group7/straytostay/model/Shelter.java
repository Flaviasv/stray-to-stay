package com.group7.straytostay.model;

public final class Shelter {

    private final Long id;
    private final long ownerUserId;
    private final String name;
    private final String description;
    private final String email;
    private final String phone;
    private final String addressLine;
    private final String postalCode;
    private final String city;
    private final String province;
    private final String website;

    public Shelter(Long id, long ownerUserId, String name, String description, String email, String phone,
                   String addressLine, String postalCode, String city, String province, String website) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        this.id = id;
        this.ownerUserId = ownerUserId;
        this.name = name;
        this.description = description;
        this.email = email;
        this.phone = phone;
        this.addressLine = addressLine;
        this.postalCode = postalCode;
        this.city = city;
        this.province = province;
        this.website = website;
    }

    public Long getId() { return id; }
    public long getOwnerUserId() { return ownerUserId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddressLine() { return addressLine; }
    public String getPostalCode() { return postalCode; }
    public String getCity() { return city; }
    public String getProvince() { return province; }
    public String getWebsite() { return website; }
}
