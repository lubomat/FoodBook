CREATE SCHEMA IF NOT EXISTS recipes;

CREATE TABLE recipes.category (
                                  id SERIAL PRIMARY KEY,
                                  name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO recipes.category (name)
VALUES ('breakfast'), ('lunch'), ('dinner'), ('snack');

CREATE TABLE recipes.recipe (
                                id SERIAL PRIMARY KEY,
                                name VARCHAR(100) NOT NULL,
                                ingredients TEXT NOT NULL,
                                image_url VARCHAR(255),
                                category_id INT REFERENCES recipes.category(id) ON DELETE CASCADE
);

CREATE TABLE recipes.recipe_steps (
                                      id SERIAL PRIMARY KEY,
                                      recipe_id INT REFERENCES recipes.recipe(id) ON DELETE CASCADE,
                                      step_number INT NOT NULL,
                                      description TEXT NOT NULL
);
