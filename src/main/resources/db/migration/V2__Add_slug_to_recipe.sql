ALTER TABLE recipes.recipe
ADD COLUMN IF NOT EXISTS slug VARCHAR(255);

UPDATE recipes.recipe
SET slug = LOWER(
        TRANSLATE(name, ' ąćęłńóśźż', '-acelnoszz')
    )
WHERE slug IS NULL;
