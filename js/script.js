document.addEventListener("DOMContentLoaded", function () {
    const viewRecipesBtn = document.getElementById("view-recipes-btn");
    const addRecipeBtn = document.getElementById("add-recipe-btn");
    const recipesSection = document.getElementById("recipes-section");
    const addRecipeSection = document.getElementById("add-recipe-section");
    const categorySection = document.getElementById("category-section");
    const recipesList = document.getElementById("recipes-list");
    const categoryList = document.getElementById("category-list");
    const recipeForm = document.getElementById("recipe-form");
    let previousView = null;


    // Pokaż listę kategorii po kliknięciu "Przepisy"
    viewRecipesBtn.addEventListener("click", function () {
        categorySection.classList.remove("hidden");
        recipesSection.classList.add("hidden");
        addRecipeSection.classList.add("hidden");
    });

    // Funkcja do wyświetlania listy przepisów dla danej kategorii
    function fetchRecipesByCategory(categoryId) {
        fetch(`http://localhost:8080/api/recipes/category/${categoryId}`)
            .then(response => response.json())
            .then(data => {
                console.log('Dane przepisów:', data);
                recipesList.innerHTML = ""; // Wyczyść istniejącą listę
                data.forEach(recipe => {
                    const button = document.createElement("button");
                    button.textContent = recipe.name;
                    button.classList.add("recipe-button");
                    button.addEventListener("click", function () {
                        fetchRecipeDetails(recipe.name);
                    });
                    recipesList.appendChild(button);
                });
                categorySection.classList.add("hidden");
                recipesSection.classList.remove("hidden");
            })
            .catch(error => console.error("Błąd podczas pobierania przepisów:", error));
    }

    function fetchRecipeDetails(recipeName) {
        fetch(`http://localhost:8080/api/recipes/name/${recipeName}`)
            .then(response => response.json())
            .then(data => {
                console.log('Szczegóły przepisu:', data);
                recipesList.innerHTML = ""; // Wyczyść istniejącą listę
                const nameElement = document.createElement("h3");
                nameElement.textContent = data.name;
                recipesList.appendChild(nameElement);
    
                const ingredientsElement = document.createElement("p");
                ingredientsElement.innerHTML = `<strong>Składniki:</strong> ${data.ingredients}`;
                recipesList.appendChild(ingredientsElement);
    
                const stepsElement = document.createElement("p");
                stepsElement.innerHTML = `<strong>Kroki:</strong> ${data.steps}`;
                recipesList.appendChild(stepsElement);
            })
            .catch(error => console.error("Błąd podczas pobierania szczegółów przepisu:", error));
    }
    
    

    // Dodaj event listener dla każdej kategorii
    document.getElementById("breakfast-btn").addEventListener("click", function () {
        fetchRecipesByCategory(1); // Id kategorii "Breakfast"
    });

    document.getElementById("lunch-btn").addEventListener("click", function () {
        fetchRecipesByCategory(2); // Id kategorii "Lunch"
    });

    document.getElementById("dinner-btn").addEventListener("click", function () {
        fetchRecipesByCategory(3); // Id kategorii "Dinner"
    });

    document.getElementById("snack-btn").addEventListener("click", function () {
        fetchRecipesByCategory(4); // Id kategorii "Snack"
    });

    // Pokaż formularz po kliknięciu "Dodaj Przepis"
    addRecipeBtn.addEventListener("click", function () {
        addRecipeSection.classList.remove("hidden");
        recipesSection.classList.add("hidden");
        categorySection.classList.add("hidden");
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

        console.log('Wysyłanie przepisu:', recipe);

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
            console.log('Odpowiedź serwera:', data);
            alert("Przepis został dodany!");
            recipeForm.reset(); // Wyczyść formularz
        })
        .catch(error => console.error("Błąd podczas dodawania przepisu:", error));
    });
});
