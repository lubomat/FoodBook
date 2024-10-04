document.addEventListener("DOMContentLoaded", function () {
    const viewRecipesBtn = document.getElementById("view-recipes-btn");
    const addRecipeBtn = document.getElementById("add-recipe-btn");
    const recipesSection = document.getElementById("recipes-section");
    const addRecipeSection = document.getElementById("add-recipe-section");
    const recipesList = document.getElementById("recipes-list");
    const recipeForm = document.getElementById("recipe-form");

    // Pokaż listę przepisów po kliknięciu "Przepisy"
    viewRecipesBtn.addEventListener("click", function () {
        recipesSection.classList.remove("hidden");
        addRecipeSection.classList.add("hidden");

        // Pobierz przepisy z backendu i wyświetl
        fetch('http://localhost:8080/api/recipes')
            .then(response => response.json())
            .then(data => {
                console.log('Dane przepisów:', data);  // Logowanie
                recipesList.innerHTML = ""; // Wyczyść istniejącą listę
                data.forEach(recipe => {
                    const li = document.createElement("li");
                    li.textContent = `${recipe.name}: ${recipe.ingredients}`;
                    recipesList.appendChild(li);
                });
            })
            .catch(error => console.error("Błąd podczas pobierania przepisów:", error));
    });

    // Pokaż formularz po kliknięciu "Dodaj Przepis"
    addRecipeBtn.addEventListener("click", function () {
        addRecipeSection.classList.remove("hidden");
        recipesSection.classList.add("hidden");
    });

    // Obsługa formularza dodawania przepisu
    recipeForm.addEventListener("submit", function (e) {
        e.preventDefault();

        const recipe = {
            name: recipeForm.name.value,
            ingredients: recipeForm.ingredients.value,
            steps: recipeForm.steps.value,
            category: { id: recipeForm.category.value }
        };

        console.log('Wysyłanie przepisu:', recipe);  // Logowanie wysyłanych danych

        // Wysyłanie przepisu do backendu
        fetch('http://localhost:8080/api/recipes', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(recipe)
        })
        .then(response => response.json())
        .then(data => {
            console.log('Odpowiedź serwera:', data);  // Logowanie odpowiedzi serwera
            alert("Przepis został dodany!");
            recipeForm.reset(); // Wyczyść formularz
        })
        .catch(error => console.error("Błąd podczas dodawania przepisu:", error));
    });
});
