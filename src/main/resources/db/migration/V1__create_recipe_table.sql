CREATE SCHEMA IF NOT EXISTS recipes;

CREATE TABLE recipes.app_user (
                                  id BIGSERIAL PRIMARY KEY,
                                  username VARCHAR(50) NOT NULL UNIQUE,
                                  password VARCHAR(255) NOT NULL,
                                  email VARCHAR(100) NOT NULL UNIQUE,
                                  role VARCHAR(20) DEFAULT 'ROLE_USER',
                                  enabled BOOLEAN DEFAULT TRUE
);

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
                                category_id INT REFERENCES recipes.category(id) ON DELETE CASCADE,
                                user_id BIGINT REFERENCES recipes.app_user(id) ON DELETE CASCADE
);

CREATE TABLE recipes.recipe_steps (
                                      id SERIAL PRIMARY KEY,
                                      recipe_id INT REFERENCES recipes.recipe(id) ON DELETE CASCADE,
                                      step_number INT NOT NULL,
                                      description TEXT NOT NULL
);

CREATE INDEX idx_recipe_id ON recipes.recipe_steps(recipe_id);

CREATE TABLE recipes.comments (
                                  id SERIAL PRIMARY KEY,
                                  recipe_id INT REFERENCES recipes.recipe(id) ON DELETE CASCADE,
                                  user_id BIGINT REFERENCES recipes.app_user(id) ON DELETE CASCADE,
                                  content TEXT NOT NULL,
                                  rating INT NOT NULL
);
