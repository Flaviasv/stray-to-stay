-- StrayToStay database schema (PostgreSQL 16)
-- Owner: backend team. Any change here needs a matching change in the ER diagram (docs/diagrams).

CREATE TABLE users (
    id             BIGSERIAL PRIMARY KEY,
    email          VARCHAR(254) NOT NULL UNIQUE,
    password_hash  VARCHAR(100) NOT NULL,
    full_name      VARCHAR(120) NOT NULL,
    role           VARCHAR(20)  NOT NULL CHECK (role IN ('ADOPTER', 'SHELTER_STAFF', 'ADMIN')),
    city           VARCHAR(80),
    province       VARCHAR(40),
    active         BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE shelters (
    id             BIGSERIAL PRIMARY KEY,
    owner_user_id  BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name           VARCHAR(160) NOT NULL,
    description    TEXT,
    email          VARCHAR(254),
    phone          VARCHAR(30),
    address_line   VARCHAR(200),
    postal_code    VARCHAR(10),
    city           VARCHAR(80),
    province       VARCHAR(40),
    website        VARCHAR(200),
    CONSTRAINT uq_shelter_owner UNIQUE (owner_user_id)
);

CREATE TABLE pets (
    id              BIGSERIAL PRIMARY KEY,
    shelter_id      BIGINT       NOT NULL REFERENCES shelters(id) ON DELETE CASCADE,
    name            VARCHAR(80)  NOT NULL,
    species         VARCHAR(20)  NOT NULL CHECK (species IN ('DOG', 'CAT', 'RABBIT', 'BIRD', 'OTHER')),
    breed           VARCHAR(80),
    sex             VARCHAR(10)  NOT NULL DEFAULT 'UNKNOWN' CHECK (sex IN ('MALE', 'FEMALE', 'UNKNOWN')),
    age_months      INTEGER      CHECK (age_months IS NULL OR age_months >= 0),
    size            VARCHAR(10)  CHECK (size IS NULL OR size IN ('SMALL', 'MEDIUM', 'LARGE')),
    description     TEXT,
    medical_notes   TEXT,
    vaccinated      BOOLEAN      NOT NULL DEFAULT FALSE,
    neutered        BOOLEAN      NOT NULL DEFAULT FALSE,
    good_with_kids  BOOLEAN,
    good_with_pets  BOOLEAN,
    status          VARCHAR(20)  NOT NULL DEFAULT 'AVAILABLE'
                    CHECK (status IN ('AVAILABLE', 'PENDING', 'ADOPTED', 'FOSTERED', 'WITHDRAWN')),
    listing_type    VARCHAR(20)  NOT NULL DEFAULT 'ADOPTION'
                    CHECK (listing_type IN ('ADOPTION', 'FOSTER', 'BOTH')),
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE pet_photos (
    id            BIGSERIAL PRIMARY KEY,
    pet_id        BIGINT       NOT NULL REFERENCES pets(id) ON DELETE CASCADE,
    stored_name   VARCHAR(160) NOT NULL UNIQUE,   -- server-generated file name, never the upload name
    content_type  VARCHAR(60)  NOT NULL,
    size_bytes    BIGINT       NOT NULL,
    is_primary    BOOLEAN      NOT NULL DEFAULT FALSE,
    uploaded_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE inquiries (
    id            BIGSERIAL PRIMARY KEY,
    pet_id        BIGINT      NOT NULL REFERENCES pets(id) ON DELETE CASCADE,
    user_id       BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    message       TEXT        NOT NULL,
    inquiry_type  VARCHAR(20) NOT NULL CHECK (inquiry_type IN ('ADOPTION', 'FOSTER')),
    status        VARCHAR(20) NOT NULL DEFAULT 'NEW'
                  CHECK (status IN ('NEW', 'READ', 'REPLIED', 'CLOSED')),
    created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_inquiry_per_user_pet UNIQUE (pet_id, user_id)
);

-- Indexes on the search / filter columns used by PetSearchServlet
CREATE INDEX ix_pets_species    ON pets (species);
CREATE INDEX ix_pets_breed      ON pets (breed);
CREATE INDEX ix_pets_size       ON pets (size);
CREATE INDEX ix_pets_status     ON pets (status);
CREATE INDEX ix_pets_created_at ON pets (created_at DESC);
CREATE INDEX ix_pets_shelter    ON pets (shelter_id);
CREATE INDEX ix_shelters_city   ON shelters (city);
CREATE INDEX ix_inquiries_pet   ON inquiries (pet_id);
CREATE INDEX ix_inquiries_user  ON inquiries (user_id);
