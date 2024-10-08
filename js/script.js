document.addEventListener('DOMContentLoaded', function () {
	const viewRecipesBtn = document.getElementById('view-recipes-btn');
	const addRecipeBtn = document.getElementById('add-recipe-btn');
	const recipesSection = document.getElementById('recipes-section');
	const addRecipeSection = document.getElementById('add-recipe-section');
	const categorySection = document.getElementById('category-section');
	const recipesList = document.getElementById('recipes-list');
	const recipeForm = document.getElementById('recipe-form');
	const backToCategoriesBtn = document.getElementById('back-to-categories-btn');
	const backToRecipesBtn = document.getElementById('back-to-recipes-btn');
	


	const addStepBtn = document.getElementById('add-step-btn');
	const stepsContainer = document.getElementById('steps-container');

	let currentPage = 1;
	const recipesPerPage = 10;
	let currentCategory = null;

	viewRecipesBtn.addEventListener('click', function () {
		categorySection.classList.remove('hidden');
		recipesSection.classList.add('hidden');
		addRecipeSection.classList.add('hidden');
		backToRecipesBtn.classList.add('hidden');
	});

	addRecipeBtn.addEventListener('click', function () {
		addRecipeSection.classList.remove('hidden');
		recipesSection.classList.add('hidden');
		categorySection.classList.add('hidden');
		backToRecipesBtn.classList.add('hidden');
	});

	addStepBtn.addEventListener('click', function () {
		const stepCount = stepsContainer.children.length + 1;
		const newStepInput = document.createElement('textarea');
		newStepInput.name = 'steps';
		newStepInput.placeholder = `Krok ${stepCount}`;
		newStepInput.required = true;
		stepsContainer.appendChild(newStepInput);
	});

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

				categorySection.classList.add('hidden');
				recipesSection.classList.remove('hidden');
				backToCategoriesBtn.classList.remove('hidden');
				backToRecipesBtn.classList.add('hidden'); 
			})
			.catch((error) =>
				console.error('Błąd podczas pobierania przepisów:', error)
			);
	}

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
			.catch((error) =>
				console.error('Błąd podczas pobierania szczegółów przepisu:', error)
			);
	
	
	}

	backToCategoriesBtn.addEventListener('click', function () {
		categorySection.classList.remove('hidden');
		recipesSection.classList.add('hidden');
		backToCategoriesBtn.classList.add('hidden'); 
	});

	backToRecipesBtn.addEventListener('click', function () {
		fetchRecipesByCategory(currentCategory, currentPage); 
		backToRecipesBtn.classList.add('hidden'); 
		backToCategoriesBtn.classList.remove('hidden'); 
	});

	document
		.getElementById('breakfast-btn')
		.addEventListener('click', function () {
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

	recipeForm.addEventListener('submit', function (e) {
		e.preventDefault();

		const formData = new FormData(recipeForm);

		console.log('Wysyłanie przepisu:', formData);

		fetch('http://localhost:8080/api/recipes', {
			method: 'POST',
			body: formData,
		})
			.then((response) => response.json())
			.then((data) => {
				console.log('Odpowiedź serwera:', data);
				alert('Przepis został dodany!');
				recipeForm.reset();
			})
			.catch((error) =>
				console.error('Błąd podczas dodawania przepisu:', error)
			);
	});
});
