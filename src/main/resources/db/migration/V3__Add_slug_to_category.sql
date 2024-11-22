ALTER TABLE recipes.category
    ADD COLUMN IF NOT EXISTS slug VARCHAR(255);

UPDATE recipes.category
SET slug = LOWER(
        TRANSLATE(name, ' ąćęłńóśźż', '-acelnoszz')
    )
WHERE slug IS NULL;

ALTER TABLE recipes.category
    ALTER COLUMN slug SET NOT NULL;

ALTER TABLE recipes.category
    ADD CONSTRAINT unique_slug UNIQUE (slug);
