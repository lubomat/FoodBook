CREATE SCHEMA IF NOT EXISTS recipes;

CREATE TABLE recipes.category (
                                  id SERIAL PRIMARY KEY,
                                  name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO recipes.category (name) VALUES ('breakfast'), ('lunch'), ('dinner'), ('snack');

CREATE TABLE recipes.recipe (
                                id SERIAL PRIMARY KEY,
                                name VARCHAR(100) NOT NULL,
                                ingredients TEXT NOT NULL,
                                steps TEXT NOT NULL,
                                category_id INT REFERENCES recipes.category(id) ON DELETE CASCADE
);
