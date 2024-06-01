CREATE TABLE pets (
    name TEXT,
    species TEXT,
    cuteness INT,
    CONSTRAINT unique_species_name UNIQUE (species, name)
);