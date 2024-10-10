document.addEventListener('DOMContentLoaded', function () {
    const viewRecipesBtn = document.getElementById('view-recipes-btn');
    const addRecipeBtn = document.getElementById('add-recipe-btn');
    const registerBtn = document.getElementById('register-btn');
    const loginBtn = document.getElementById('login-btn');
	
    const recipesSection = document.getElementById('recipes-section');
    const addRecipeSection = document.getElementById('add-recipe-section');
    const categorySection = document.getElementById('category-section');
    const registerSection = document.getElementById('register-section');
    const loginSection = document.getElementById('login-section');
    const recipesList = document.getElementById('recipes-list');
    const recipeForm = document.getElementById('recipe-form');
    const backToCategoriesBtn = document.getElementById('back-to-categories-btn');
    const backToRecipesBtn = document.getElementById('back-to-recipes-btn');
    
    const addStepBtn = document.getElementById('add-step-btn');
    const stepsContainer = document.getElementById('steps-container');

	const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');

    let currentPage = 1;
    const recipesPerPage = 10;
    let currentCategory = null;


    function hideAllSections() {
        // Ukrywa wszystkie sekcje
        loginSection.classList.add('hidden');
        registerSection.classList.add('hidden');
        addRecipeSection.classList.add('hidden');
        recipesSection.classList.add('hidden');
        categorySection.classList.add('hidden');
    }

    function isUserLoggedIn() {
        const token = localStorage.getItem('jwtToken');
        return token !== null;  // Zwraca true, jeśli token istnieje, co oznacza, że użytkownik jest zalogowany
    }

    // Obsługa przycisku "Rejestracja"
    registerBtn.addEventListener('click', function () {
		console.log("Przycisk 'Rejestracja' został kliknięty");
        hideAllSections(); 
        registerSection.classList.remove('hidden');
    });

    // Obsługa przycisku "Logowanie"
    loginBtn.addEventListener('click', function () {
		console.log("Przycisk 'Logowanie' został kliknięty");
        hideAllSections(); 
        loginSection.classList.remove('hidden');
    });

    // Obsługa formularza logowania
    loginForm.addEventListener('submit', function (e) {
        e.preventDefault();
		console.log("Formularz logowania wysłany!");		

        const username = document.getElementById('login-username').value;
        const password = document.getElementById('login-password').value;

        if (username && password) {
			console.log("Dane logowania: " + {username, password});
            fetch('http://localhost:8080/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Błąd HTTP! Status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                console.log("Odpowiedź serwera: ", data);
                if (data.jwt) {
                    alert('Logowanie udane! Twój token JWT: ' + data.jwt);
                    localStorage.setItem('jwtToken', data.jwt);
                } else {
                    alert('Błąd logowania: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Błąd podczas logowania:', error);
                alert('Błąd podczas logowania: ' + error.message);
            });
        } else {
            console.log("Brak nazwy użytkownika lub hasła!");
        }
    });

    // Obsługa formularza rejestracji
    registerForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const username = document.getElementById('register-username').value;
        const password = document.getElementById('register-password').value;

        if (username && password) {
			console.log("Wysyłanie zapytania do backendu...");
            fetch('http://localhost:8080/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            })
            .then(response => response.json())
            .then(data => {
				console.log("Odpowiedź backendu: ", data);
                if (data.success) {
                    alert('Rejestracja udana!');
                } else {
                    alert('Błąd rejestracji: ' + data.message);
                }
            })
            .catch(error => console.error('Błąd podczas rejestracji:', error));
        }
    });

    // Obsługa dodawania kroków do przepisu
    addStepBtn.addEventListener('click', function () {
        const stepCount = stepsContainer.children.length + 1;
        const newStepInput = document.createElement('textarea');
        newStepInput.name = 'steps';
        newStepInput.placeholder = `Krok ${stepCount}`;
        newStepInput.required = true;
        stepsContainer.appendChild(newStepInput);
    });

    // Obsługa wyświetlania przepisów
    viewRecipesBtn.addEventListener('click', function () {
        hideAllSections(); 
        categorySection.classList.remove('hidden');
    });

    // Obsługa przechodzenia do formularza dodawania przepisu
    addRecipeBtn.addEventListener('click', function () {
        if (isUserLoggedIn()) {
            // Jeśli użytkownik jest zalogowany, wyświetl formularz dodawania przepisu
            hideAllSections();
            addRecipeSection.classList.remove('hidden');
        } else {
            // Jeśli użytkownik nie jest zalogowany, wyświetl alert
            alert('Dodawanie przepisu wymaga zalogowania.');
        }
    });

    // Funkcja pobierania przepisów po kategorii
    function fetchRecipesByCategory(categoryId, page = 1) {
        currentCategory = categoryId;
        fetch(`http://localhost:8080/api/recipes/category/${categoryId}`)
            .then((response) => response.json())
            .then((data) => {
                console.log('Dane przepisów:', data);
                recipesList.innerHTML = '';

                const totalRecipes = data.length;
                const totalPages = Math.ceil(totalRecipes / recipesPerPage);
                const paginatedRecipes = data.slice(
                    (page - 1) * recipesPerPage,
                    page * recipesPerPage
                );

                paginatedRecipes.forEach((recipe) => {
                    const recipeDiv = document.createElement('div');
                    recipeDiv.classList.add('recipe-tile');

                    const fullImageUrl = `http://localhost:8080${recipe.imageUrl}`;
                    const recipeImage = document.createElement('img');
                    recipeImage.src = fullImageUrl;
                    recipeImage.alt = recipe.name;
                    recipeImage.style.width = '100%';
                    recipeImage.style.height = '100%';
                    recipeImage.style.objectFit = 'cover';
                    recipeDiv.appendChild(recipeImage);

                    const recipeTitle = document.createElement('h3');
                    recipeTitle.textContent = recipe.name;
                    recipeTitle.classList.add('recipe-title');
                    recipeDiv.appendChild(recipeTitle);

                    recipeDiv.addEventListener('click', function () {
                        fetchRecipeDetails(recipe.name);
                    });

                    recipesList.appendChild(recipeDiv);
                });

                if (totalPages > 1) {
                    createPagination(totalPages, categoryId);
                }

                hideAllSections(); 
                recipesSection.classList.remove('hidden');
                backToCategoriesBtn.classList.remove('hidden');
            })
            .catch((error) => console.error('Błąd podczas pobierania przepisów:', error));
    }

    // Funkcja tworzenia paginacji
    function createPagination(totalPages, categoryId) {
        const paginationDiv = document.createElement('div');
        paginationDiv.classList.add('pagination');

        for (let i = 1; i <= totalPages; i++) {
            const pageButton = document.createElement('button');
            pageButton.textContent = i;
            pageButton.addEventListener('click', function () {
                currentPage = i;
                fetchRecipesByCategory(categoryId, i);
            });
            paginationDiv.appendChild(pageButton);
        }

        recipesList.appendChild(paginationDiv);
    }

    // Funkcja pobierania szczegółów przepisu
    function fetchRecipeDetails(recipeName) {
        fetch(`http://localhost:8080/api/recipes/name/${recipeName}`)
            .then((response) => response.json())
            .then((data) => {
                console.log('Szczegóły przepisu:', data);
                recipesList.innerHTML = '';

                const nameElement = document.createElement('h3');
                nameElement.textContent = data.name;
                recipesList.appendChild(nameElement);

                const ingredientsElement = document.createElement('p');
                ingredientsElement.innerHTML = `<strong>Składniki:</strong> ${data.ingredients}`;
                recipesList.appendChild(ingredientsElement);

                const stepsElement = document.createElement('div');
                const stepsHeader = document.createElement('p');
                stepsHeader.innerHTML = `<strong>Kroki:</strong>`;
                stepsElement.appendChild(stepsHeader);

                const stepsList = document.createElement('ol');

                data.steps.forEach((step, index) => {
                    const stepItem = document.createElement('li');
                    stepItem.textContent = step.description;
                    stepsList.appendChild(stepItem);
                });

                stepsElement.appendChild(stepsList);
                recipesList.appendChild(stepsElement);

                backToRecipesBtn.classList.remove('hidden');
                backToCategoriesBtn.classList.add('hidden');
            })
            .catch((error) => console.error('Błąd podczas pobierania szczegółów przepisu:', error));
    }

    // Obsługa powrotu do kategorii
    backToCategoriesBtn.addEventListener('click', function () {
        hideAllSections();  
        categorySection.classList.remove('hidden');
    });

    // Obsługa powrotu do przepisów z danej kategorii
    backToRecipesBtn.addEventListener('click', function () {
        fetchRecipesByCategory(currentCategory, currentPage);
        backToRecipesBtn.classList.add('hidden');
        backToCategoriesBtn.classList.remove('hidden');
    });

    // Obsługa kliknięcia kategorii
    document.getElementById('breakfast-btn').addEventListener('click', function () {
        fetchRecipesByCategory(1);
    });

    document.getElementById('lunch-btn').addEventListener('click', function () {
        fetchRecipesByCategory(2);
    });

    document.getElementById('dinner-btn').addEventListener('click', function () {
        fetchRecipesByCategory(3);
    });

    document.getElementById('snack-btn').addEventListener('click', function () {
        fetchRecipesByCategory(4);
    });

    // Obsługa formularza dodawania przepisu
    recipeForm.addEventListener('submit', function (e) {
        e.preventDefault();
        if (!isUserLoggedIn()) {
            // Zatrzymanie wysyłania formularza, jeśli użytkownik nie jest zalogowany
            alert('Musisz być zalogowany, aby dodać przepis.');
            return;
        }

        const formData = new FormData(recipeForm);

        console.log('Wysyłanie przepisu:', formData);

        fetch('http://localhost:8080/api/recipes', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')  // Użycie tokena JWT z localStorage
            },
            body: formData
        })
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Błąd podczas dodawania przepisu');
            }
        })
        .then((data) => {
            console.log('Przepis został dodany:', data);
            alert('Przepis został dodany!');
            recipeForm.reset();
        })
        .catch((error) => {
            console.error('Błąd podczas dodawania przepisu:', error);
            alert('Dodawanie nowego przepisu wymaga zalogowania!');
        });
    });
});
