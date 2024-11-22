document.addEventListener('DOMContentLoaded', function () {
	const viewRecipesBtn = document.getElementById('view-recipes-btn');
	const addRecipeBtn = document.getElementById('add-recipe-btn');
	const registerBtn = document.getElementById('register-btn');
	const loginBtn = document.getElementById('login-btn');
	const logoutBtn = document.getElementById('logout-btn');

	const accountBtn = document.getElementById('account-btn');
	const myRecipesBtn = document.getElementById('my-recipes-btn');
	const myRecipesSection = document.getElementById('myRecipes-section');

	const addRecipeSection = document.getElementById('add-recipe-section');
	const categorySection = document.getElementById('category-section');
	const registerSection = document.getElementById('register-section');
	const loginSection = document.getElementById('login-section');
	const accountSection = document.getElementById('account-section');

	const recipesList = document.getElementById('recipes-list');
	const recipeForm = document.getElementById('recipe-form');
	const backToCategoriesBtn = document.getElementById('back-to-categories-btn');
	const backToRecipesBtn = document.getElementById('back-to-recipes-btn');

	const addStepBtn = document.getElementById('add-step-btn');
	const stepsContainer = document.getElementById('steps-container');

	const loginForm = document.getElementById('login-form');
	const registerForm = document.getElementById('register-form');

	const myRecipesList = document.getElementById('my-recipes-list');
	const backToMyRecipesBtn = document.getElementById('back-to-my-recipes-btn');

	// const API_BASE_URL = 'https://foodbook-crcr.onrender.com';
	  const API_BASE_URL = 'http://localhost:8080';

	let currentPage = 1;
	const recipesPerPage = 9;
	let currentCategory = null;
	let currentRecipeId = null;

	

	document.getElementById('foodbook-header').addEventListener('click', () => {
		window.location.href = 'index.html';
	});
	

		const urlPath = window.location.pathname;
	
		// Navigation to a specific recipe
		if (urlPath.startsWith('/recipe/')) {
			const recipeId = urlPath.split('/recipe/')[1];
			if (recipeId) {
				fetchRecipeDetailsById(recipeId);
			}
		}

		// Handle navigation based on URL hash or path
		const currentHash = window.location.hash;
		if (currentHash === '#/register') {
			hideAllSections();
			registerSection.classList.remove('hidden');
		} else if (currentHash === '#/login') {
			hideAllSections();
			loginSection.classList.remove('hidden');
		} else if (currentHash.startsWith('#/category/')) {
			const categorySlug = currentHash.split('/category/')[1];
			if (categorySlug) {
				fetchRecipesByCategorySlug(categorySlug);
			}
		} else if (currentHash.startsWith('#/recipe/')) {
			const recipeSlug = currentHash.split('/recipe/')[1];
			if (recipeSlug) {
				fetchRecipeDetailsBySlug(recipeSlug);
			}
		}
		

	
	function hideAllSections() {
		console.log('loginSection:', loginSection);
		console.log('registerSection:', registerSection);
		console.log('addRecipeSection:', addRecipeSection);
		console.log('recipesSection:', myRecipesSection);
		console.log('categorySection:', categorySection);
		console.log('accountSection:', accountSection);
		console.log('comments-list:', document.getElementById('comments-list'));
		console.log('add-comment-section:', document.getElementById('add-comment-section'));
		console.log('comments-header:', document.getElementById('comments-header'));
		console.log('user-info:', document.getElementById('user-info'));

		loginSection?.classList.add('hidden');
		registerSection?.classList.add('hidden');
		addRecipeSection?.classList.add('hidden');
		myRecipesSection?.classList.add('hidden');
		categorySection?.classList.add('hidden');
		accountSection?.classList.add('hidden');
		backToCategoriesBtn.classList.add('hidden');
		backToRecipesBtn.classList.add('hidden');
		document.getElementById('comments-list')?.classList.add('hidden');
		document.getElementById('add-comment-section')?.classList.add('hidden');
		document.getElementById('comments-header')?.classList.add('hidden');
		document.getElementById('user-info')?.classList.add('hidden');

		const userInfoDisplay = document.getElementById('user-info');
		userInfoDisplay?.classList.add('hidden');

		recipesList.innerHTML = '';
		myRecipesList.innerHTML = '';
	}

	function updateURL(path) {
		window.history.pushState({}, '', path);
	}	
	

	function isUserLoggedIn() {
		const token = localStorage.getItem('jwtToken');
		console.log('Sprawdzanie tokena JWT:', token);
		return token !== null;
	}

	// REGISTER BUTTON
	registerBtn.addEventListener('click', function () {
		console.log("The 'Registration' button has been clicked");
		hideAllSections();
		registerSection.classList.remove('hidden');
		updateURL('#/register');
	});

	// REGISTER FORM
	registerForm.addEventListener('submit', function (e) {
		e.preventDefault();

		const username = document.getElementById('register-username').value;
		const email = document.getElementById('register-email').value;
		const password = document.getElementById('register-password').value;
		const confirmPassword = document.getElementById('register-confirm-password').value;

		if (password !== confirmPassword) {
			alert('Hasła nie są takie same. Spróbuj ponownie.');
			return;
		}
		
		if (username && email && password) {
			console.log('Sending a request to the backend...');

			fetch(`${API_BASE_URL}/register`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({ username, email, password, confirmPassword }),
			})
				.then((response) => {
					if (response.ok) {
						return response.text();
					} else {
						return response.text().then((error) => {
							throw new Error(error);
						});
					}
				})
				.then((data) => {
					console.log('Backend respond: ', data);
					alert(data);
					window.location.href = 'index.html';
				})
				.catch((error) => {
					console.error('Błąd podczas rejestracji:', error.message);
					alert('Błąd rejestracji: ' + error.message);
				});
		} else {
			alert('Wprowadź nazwę użytkownika, email i hasło.');
		}
	});

	// LOGIN BUTTON
	loginBtn.addEventListener('click', function () {
		console.log("Przycisk 'Logowanie' został kliknięty");
		hideAllSections();
		loginSection.classList.remove('hidden');
		updateURL('#/login');
	});

	// LOGIN FORM
	loginForm.addEventListener('submit', function (e) {
		e.preventDefault();
		console.log('Login form sent!');

		const usernameOrEmail = document.getElementById(
			'login-usernameOrEmail'
		).value;
		const password = document.getElementById('login-password').value;

		if (usernameOrEmail && password) {
			console.log('Dane logowania: ' + { usernameOrEmail, password });
			fetch(`${API_BASE_URL}/login`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({ usernameOrEmail, password }),
			})
				.then((response) => {
					if (!response.ok) {
						throw new Error(`Błąd HTTP! Status: ${response.status}`);
					}
					return response.json();
				})
				.then((data) => {
					console.log('Server response: ', data);
					if (data.jwt) {
						alert('Logowanie udane!');
						localStorage.setItem('jwtToken', data.jwt);

						updateLoginState();
						window.location.href = 'index.html';
					} else {
						alert('Błąd podczas logowania: ' + data.message);
					}
				})
				.catch((error) => {
					console.error('Error logging in:', error);
					alert('Error logging in: ' + error.message);
				});
		} else {
			console.log('No username or password!');
		}
	});

	// Function to retrieve username from JWT
	function getUsernameFromToken(token) {
		try {
			const payload = JSON.parse(atob(token.split('.')[1]));
			return payload.sub || payload.username;
		} catch (error) {
			console.error('Error decoding JWT token:', error);
			return null;
		}
	}

	// Function to set the login status
	function updateLoginState() {
		const userInfoDisplay = document.getElementById('user-info');
		const token = localStorage.getItem('jwtToken');
		console.log('Aktualny token JWT:', token);

		if (isUserLoggedIn()) {
			const username = getUsernameFromToken(token);
			loginBtn.classList.add('hidden');
			registerBtn.classList.add('hidden');
			logoutBtn.classList.remove('hidden');
			accountBtn.classList.remove('hidden');
			userInfoDisplay.textContent = `Zalogowany jako: ${username}`;
			userInfoDisplay.classList.remove('hidden');
		} else {
			loginBtn.classList.remove('hidden');
			registerBtn.classList.remove('hidden');
			logoutBtn.classList.add('hidden');
			accountBtn.classList.add('hidden');
			userInfoDisplay.classList.add('hidden');
		}
	}

	// LOGOUT
	logoutBtn.addEventListener('click', function () {
		localStorage.removeItem('jwtToken');
		alert('Zostałeś wylogowany.');
		updateLoginState();
		window.location.reload();
	});

	// Update the login status when the page is loaded
	updateLoginState();

	// ADD RECIPE BUTTON
	addRecipeBtn.addEventListener('click', function () {
		if (isUserLoggedIn()) {
			hideAllSections();
			addRecipeSection.classList.remove('hidden');
		} else {
			alert('Dodawanie przepisu wymaga zalogowania.');
		}
	});

	// ADD RECIPE FORM
	recipeForm.addEventListener('submit', function (e) {
		e.preventDefault();

		const token = localStorage.getItem('jwtToken');
		if (!token) {
			alert('Spróbuj ponownie się zalogować.');
			return;
		}

		if (!isUserLoggedIn()) {
			alert('Musisz być zalogowany, aby dodać przepis.');
			return;
		}

		const formData = new FormData(recipeForm);

		console.log('Wysyłanie przepisu:', formData);

		fetch(`${API_BASE_URL}/api/recipes/add`, {
			method: 'POST',
			headers: {
				Authorization: 'Bearer ' + token,
			},
			body: formData,
		})
			.then((response) => {
				if (response.ok) {
					return response.json();
				} else if (response.status === 409) {
					return response.text().then((text) => {
						throw new Error(text || 'Przepis o tej nazwie już istnieje.');
					});
				} else if (response.status === 400) {
					return response.text().then((text) => {
						throw new Error(text || 'Błąd walidacji danych.');
					});
				} else {
					return response.text().then((text) => {
						throw new Error(
							text || 'Wystąpił błąd podczas dodawania przepisu.'
						);
					});
				}
			})
			.then((data) => {
				console.log('The recipe has been added:', data);
				alert('Przepis został dodany!');
				recipeForm.reset();
			})
			.catch((error) => {
				console.error('Błąd podczas dodawania przepisu:', error);
				alert(error.message);
			});
	});

	// ADD RECIPE STEPS BUTTON
	addStepBtn.addEventListener('click', function () {
		const stepCount = stepsContainer.children.length + 1;
		const newStepInput = document.createElement('textarea');
		newStepInput.name = 'steps';
		newStepInput.placeholder = `Krok ${stepCount}`;
		newStepInput.required = true;
		stepsContainer.appendChild(newStepInput);
	});

	function updateURL(hash) {
		window.location.hash = hash;
	}

	// CATEGORY BUTTONS
	document.getElementById('breakfast-btn').addEventListener('click', function () {
		const categorySlug = 'breakfast';
		updateURL(`#/category/${categorySlug}`);
		fetchRecipesByCategorySlug(categorySlug); 
	});
	
	document.getElementById('lunch-btn').addEventListener('click', function () {
		const categorySlug = 'lunch'; 
		updateURL(`#/category/${categorySlug}`);
		fetchRecipesByCategorySlug(categorySlug);
	});
	
	document.getElementById('dinner-btn').addEventListener('click', function () {
		const categorySlug = 'dinner';
		updateURL(`#/category/${categorySlug}`);
		fetchRecipesByCategorySlug(categorySlug);
	});
	
	document.getElementById('snack-btn').addEventListener('click', function () {
		const categorySlug = 'snack'; 
		updateURL(`#/category/${categorySlug}`);
		fetchRecipesByCategorySlug(categorySlug);
	});

	// MY ACCOUNT BUTTON
	accountBtn.addEventListener('click', function () {
		hideAllSections();
		accountSection.classList.remove('hidden');
		myRecipesList.classList.remove('hidden');
	});

	myRecipesBtn.addEventListener('click', function () {
		myRecipesSection.classList.add('hidden');
		myRecipesSection.innerHTML = '';
		myRecipesList.innerHTML = '';

		fetch(`${API_BASE_URL}/api/recipes/my-recipes`, {
			method: 'GET',
			headers: {
				Authorization: 'Bearer ' + localStorage.getItem('jwtToken'),
			},
		})
			.then((response) => response.json())
			.then((data) => {
				console.log('My recipes:', data);
				myRecipesList.innerHTML = '';

				data.forEach((recipe) => {
					const recipeItem = document.createElement('li');
					const recipeLink = document.createElement('a');
					recipeLink.textContent = recipe.name;
					recipeLink.href = '#';
					recipeLink.addEventListener('click', (e) => {
						e.preventDefault();
						fetchRecipeDetailsFromMyRecipes(recipe.id);
					});

					recipeItem.appendChild(recipeLink);
					myRecipesList.appendChild(recipeItem);
				});

				myRecipesList.classList.remove('hidden');
				backToMyRecipesBtn.classList.add('hidden');
			})
			.catch((error) => {
				console.error('Error loading user recipes:', error);
				alert('Wystąpił problem z pobieraniem przepisów. Spróbuj ponownie.');
			});
	});

	// FETCH RECIPES FROM MY ACCOUNT FUNCTION
	function fetchRecipeDetailsFromMyRecipes(recipeId) {
		currentRecipeId = null;
		currentCategory = null;
		console.log('Fetching details for recipe ID:', recipeId);

		fetch(`${API_BASE_URL}/api/recipes/${recipeId}`)
			.then((response) => {
				if (!response.ok) {
					throw new Error(`Błąd HTTP! Status: ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				console.log('Recipe details:', data);

				myRecipesList.classList.add('hidden');

				myRecipesSection.classList.add('recipe-details');

				const recipeDetailsDiv = document.createElement('div');
				recipeDetailsDiv.classList.add('recipe-details');

				const nameElement = document.createElement('h3');
				nameElement.textContent = data.name;
				myRecipesSection.appendChild(nameElement);

				const ingredientsElement = document.createElement('p');
				ingredientsElement.innerHTML = `<strong>Składniki:</strong> ${data.ingredients}`;
				myRecipesSection.appendChild(ingredientsElement);

				const stepsElement = document.createElement('div');
				const stepsHeader = document.createElement('p');
				stepsHeader.innerHTML = `<strong>Kroki:</strong>`;
				stepsElement.appendChild(stepsHeader);

				const stepsList = document.createElement('ol');
				data.steps.forEach((step) => {
					const stepItem = document.createElement('li');
					stepItem.textContent = step.description;
					stepsList.appendChild(stepItem);
				});

				stepsElement.appendChild(stepsList);
				myRecipesSection.appendChild(stepsElement);

				// backToMyRecipesBtn.classList.remove('hidden');
				myRecipesSection.classList.remove('hidden');
			})
			.catch((error) => {
				console.error('Error retrieving recipe details:', error);
				alert(
					'Wystąpił błąd podczas pobierania szczegółów przepisu. Spróbuj ponownie.'
				);
			});
	}

	// BACK TO MY RECIPES BUTTON
	backToMyRecipesBtn.addEventListener('click', function () {
		myRecipesList.classList.remove('hidden');
		myRecipesSection.classList.add('hidden');
		myRecipesSection.innerHTML = '';
		// backToMyRecipesBtn.classList.add('hidden');
	});

	// FETCH RECIPE BY CATEGORY FUNCTION
	function fetchRecipesByCategory(categoryId, page = 1) {
		currentRecipeId = null;
		currentCategory = categoryId;

		hideAllSections();

		myRecipesList.innerHTML = '';
		recipesList.innerHTML = '';

		fetch(`${API_BASE_URL}/api/recipes/category/slug/${categorySlug}`)
			.then((response) => response.json())
			.then((data) => {
				console.log('Dane przepisów:', data);

				const totalRecipes = data.length;
				const totalPages = Math.ceil(totalRecipes / recipesPerPage);
				const paginatedRecipes = data.slice(
					(page - 1) * recipesPerPage,
					page * recipesPerPage
				);

				paginatedRecipes.forEach((recipe) => {
					const recipeDiv = document.createElement('div');
					recipeDiv.classList.add('recipe-tile');

					const fullImageUrl = recipe.imageUrl;

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

				backToCategoriesBtn.classList.remove('hidden');
				backToRecipesBtn.classList.add('hidden');
			})
			.catch((error) => {
				console.error('Error loading recipes:', error);
				alert('Wystąpił problem z pobieraniem przepisów.');
			});
	}

	// DISPLAY ALL RECIPES BUTTON
	viewRecipesBtn.addEventListener('click', function () {
		hideAllSections();
		currentRecipeId = null;
		currentCategory = null;
		recipesList.innerHTML = '';
		myRecipesList.innerHTML = '';
		categorySection.classList.remove('hidden');
	});


	function fetchRecipesByCategorySlug(categorySlug) {
		currentRecipeId = null;
		currentCategory = categorySlug;
	
		hideAllSections();
	
		recipesList.innerHTML = '';
		myRecipesList.innerHTML = '';
	
		fetch(`${API_BASE_URL}/api/recipes/category/slug/${categorySlug}`)
			.then((response) => {
				if (!response.ok) {
					throw new Error(`Błąd HTTP! Status: ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				console.log(`Przepisy dla kategorii ${categorySlug}:`, data);
	
				if (data.length === 0) {
					recipesList.innerHTML = '<p>Brak przepisów w tej kategorii.</p>';
					return;
				}
	
				data.forEach((recipe) => {
					const recipeDiv = document.createElement('div');
					recipeDiv.classList.add('recipe-tile');
	
					const recipeImage = document.createElement('img');
					recipeImage.src = recipe.imageUrl;
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
						fetchRecipeDetailsBySlug(recipe.slug);
					});
	
					recipesList.appendChild(recipeDiv);
				});
	
				backToCategoriesBtn.classList.remove('hidden');
			})
			.catch((error) => {
				console.error('Błąd podczas pobierania przepisów:', error);
				alert('Nie udało się załadować przepisów. Spróbuj ponownie później.');
			});
	}
	

	function fetchRecipeDetailsBySlug(slug) {
		console.log('Pobieranie szczegółów przepisu dla slug:', slug);
	
		hideAllSections();
		recipesList.innerHTML = '';
		myRecipesList.innerHTML = '';
		currentRecipeId = null;
	
		fetch(`${API_BASE_URL}/api/recipes/slug/${slug}`)
			.then((response) => {
				if (!response.ok) {
					throw new Error(`Błąd HTTP! Status: ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				console.log('Szczegóły przepisu:', data);
	
				const detailsContainer = document.createElement('div');
				detailsContainer.classList.add('recipe-details-background');
	
				const nameElement = document.createElement('h3');
				nameElement.textContent = data.name;
				detailsContainer.appendChild(nameElement);
	
				const ingredientsElement = document.createElement('p');
				ingredientsElement.innerHTML = `<strong>Składniki:</strong> ${data.ingredients}`;
				detailsContainer.appendChild(ingredientsElement);
	
				const stepsElement = document.createElement('div');
				const stepsHeader = document.createElement('p');
				stepsHeader.innerHTML = `<strong>Kroki:</strong>`;
				stepsElement.appendChild(stepsHeader);
	
				const stepsList = document.createElement('ol');
				data.steps.forEach((step) => {
					const stepItem = document.createElement('li');
					stepItem.textContent = step.description;
					stepsList.appendChild(stepItem);
				});
	
				stepsElement.appendChild(stepsList);
				detailsContainer.appendChild(stepsElement);
	
				recipesList.appendChild(detailsContainer);
	
				currentRecipeId = data.id;
	
				// Aktualizacja URL na podstawie slug
				updateURL(`#/recipe/${data.slug}`);
	
				document.getElementById('comments-header').classList.remove('hidden');
				document.getElementById('comments-list').classList.remove('hidden');
				if (isUserLoggedIn()) {
					document
						.getElementById('add-comment-section')
						.classList.remove('hidden');
				}
	
				fetchCommentsForRecipe(currentRecipeId);
				backToRecipesBtn.classList.remove('hidden');
			})
			.catch((error) => {
				console.error('Błąd podczas pobierania szczegółów przepisu:', error);
				alert(`Błąd podczas pobierania szczegółów przepisu: ${error.message}`);
			});
	}
	
	

	// DISPLAY RECIPE DETAILS FUNCTION
	function fetchRecipeDetails(recipeId) {
		currentRecipeId = null;
		console.log('Fetching details for recipe:', recipeId);

		hideAllSections();
		recipesList.innerHTML = '';
		myRecipesList.innerHTML = '';
		currentRecipeId = null;

		fetch(`${API_BASE_URL}/api/recipes/slug/${slug}`)
			.then((response) => {
				if (!response.ok) {
					throw new Error(`ERROR HTTP! Status: ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				console.log('Recipe details:', data);

				const detailsContainer = document.createElement('div');
				detailsContainer.classList.add('recipe-details-background');

				const nameElement = document.createElement('h3');
				nameElement.textContent = data.name;
				detailsContainer.appendChild(nameElement);

				const ingredientsElement = document.createElement('p');
				ingredientsElement.innerHTML = `<strong>Składniki:</strong> ${data.ingredients}`;
				detailsContainer.appendChild(ingredientsElement);

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
				detailsContainer.appendChild(stepsElement);

				recipesList.appendChild(detailsContainer);

				currentRecipeId = data.id;

				updateURL(`/recipe/${data.id}`);

				document.getElementById('comments-header').classList.remove('hidden');
				document.getElementById('comments-list').classList.remove('hidden');
				if (isUserLoggedIn()) {
					document
						.getElementById('add-comment-section')
						.classList.remove('hidden');
				}

				fetchCommentsForRecipe(currentRecipeId);
				backToRecipesBtn.classList.remove('hidden');
			})
			.catch((error) => {
				console.error('Error retrieving recipe details:', error);
				alert(`Błąd podczas pobierania szczegółów przepisu: ${error.message}`);
			});
	}

	// BACK TO CATEGORIES BUTTON
	backToCategoriesBtn.addEventListener('click', function () {
		hideAllSections();
		currentRecipeId = null;
		categorySection.classList.remove('hidden');
	});

	// BACK TO RECIPES LIST BUTTON
	backToRecipesBtn.addEventListener('click', function () {
		currentRecipeId = null;
		fetchRecipesByCategory(currentCategory, currentPage);
		backToRecipesBtn.classList.add('hidden');
		backToCategoriesBtn.classList.remove('hidden');
	});

	// PAGINATION
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

	// ADD COMMENT FORM
	document
		.getElementById('comment-form')
		.addEventListener('submit', function (e) {
			e.preventDefault();

			const content = document.getElementById('comment-content').value;

			const rating = document.querySelector('input[name="rating"]:checked');
			if (!rating) {
				alert('Proszę wybrać ocenę w gwiazdkach.');
				return;
			}

			const token = localStorage.getItem('jwtToken');

			if (!token) {
				alert('Brak tokena JWT, proszę zalogować się ponownie.');
				return;
			}

			if (!currentRecipeId) {
				alert('Błąd: Brak ID przepisu.');
				return;
			}

			console.log('Adding a comment for a recipe with ID:', currentRecipeId);

			fetch(`${API_BASE_URL}/api/comments/${currentRecipeId}`, {
				method: 'POST',
				headers: {
					Authorization: 'Bearer ' + token,
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({
					content: content,
					rating: rating.value,
				}),
			})
				.then((response) => {
					if (!response.ok) {
						throw new Error(`ERROR HTTP! Status: ${response.status}`);
					}
					return response.json();
				})
				.then((data) => {
					alert('Komentarz dodany!');
					document.getElementById('comment-form').reset();
					fetchCommentsForRecipe(currentRecipeId);
				})
				.catch((error) => {
					console.error('Błąd podczas dodawania komentarza:', error);
					alert('Wystąpił błąd podczas dodawania komentarza.');
				});
		});

	// ADD RATING FUNCTION IN *
	function renderStars(rating) {
		let stars = '';
		for (let i = 0; i < 5; i++) {
			if (i < rating) {
				stars += '★';
			} else {
				stars += '☆';
			}
		}
		return `<span class="stars">${stars}</span>`;
	}

	// DISPLAYING COMMENTS FUNCTION
	function fetchCommentsForRecipe(recipeId) {
		console.log('Pobieranie komentarzy dla przepisu o ID:', recipeId);
		fetch(`${API_BASE_URL}/api/comments/${recipeId}`)
			.then((response) => {
				if (!response.ok) {
					throw new Error(`Błąd HTTP! Status: ${response.status}`);
				}
				return response.json();
			})
			.then((comments) => {
				const commentsList = document.getElementById('comments-list');
				commentsList.innerHTML = '';

				if (comments.length > 0) {
					comments.forEach((comment) => {
						const commentItem = document.createElement('li');
						const stars = renderStars(comment.rating);
						commentItem.innerHTML = `<strong>${comment.user.username}</strong>: ${comment.content} (Ocena: ${comment.rating}/5)`;
						commentsList.appendChild(commentItem);
					});
				} else {
					commentsList.innerHTML = '<p>Brak komentarzy.</p>';
				}
			})
			.catch((error) => {
				console.error('Błąd podczas pobierania komentarzy:', error);
				alert(`Błąd podczas pobierania komentarzy: ${error.message}`);
			});
	}
});
