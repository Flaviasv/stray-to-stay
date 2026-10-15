-- Sample data for local development only.
-- Both sample users have the password: Password1!   (BCrypt, cost 12)

INSERT INTO users (email, password_hash, full_name, role, city, province) VALUES
  ('staff@ottawahumane.example',  '$2b$12$dadpmWefh1zXEYiv.JnunOWrLSShb8rttSvjORv/J1zfK0RIwhp6q', 'Sam Shelter',  'SHELTER_STAFF', 'Ottawa', 'ON'),
  ('adopter@example.com',         '$2b$12$dadpmWefh1zXEYiv.JnunOWrLSShb8rttSvjORv/J1zfK0RIwhp6q', 'Alex Adopter', 'ADOPTER',       'Ottawa', 'ON');

INSERT INTO shelters (owner_user_id, name, description, email, phone, address_line, postal_code, city, province, website) VALUES
  (1, 'Ottawa Sample Shelter', 'Demo shelter used for local development.', 'contact@ottawahumane.example', '613-555-0100',
   '100 Example St', 'K1A 0A1', 'Ottawa', 'ON', 'https://example.org');

INSERT INTO pets (shelter_id, name, species, breed, sex, age_months, size, description, vaccinated, neutered, good_with_kids, good_with_pets, status, listing_type) VALUES
  (1, 'Biscuit', 'DOG', 'Beagle',             'MALE',   30, 'MEDIUM', 'Friendly beagle who loves walks.',          TRUE,  TRUE,  TRUE,  TRUE,  'AVAILABLE', 'ADOPTION'),
  (1, 'Luna',    'CAT', 'Domestic Shorthair', 'FEMALE', 14, 'SMALL',  'Quiet lap cat, prefers a calm home.',       TRUE,  TRUE,  TRUE,  FALSE, 'AVAILABLE', 'BOTH'),
  (1, 'Max',     'DOG', 'Labrador Retriever', 'MALE',   60, 'LARGE',  'Energetic lab, needs an active family.',    TRUE,  FALSE, TRUE,  TRUE,  'AVAILABLE', 'FOSTER'),
  (1, 'Clover',  'RABBIT', NULL,              'FEMALE', 8,  'SMALL',  'Litter-trained house rabbit.',              FALSE, TRUE,  NULL,  NULL,  'PENDING',   'ADOPTION');
