document.addEventListener('DOMContentLoaded', function () {
	const viewRecipesBtn = document.getElementById('view-recipes-btn');
	const addRecipeBtn = document.getElementById('add-recipe-btn');
	const registerBtn = document.getElementById('register-btn');
	const loginBtn = document.getElementById('login-btn');
	const logoutBtn = document.getElementById('logout-btn');

	const accountBtn = document.getElementById('account-btn'); 
	const myRecipesBtn = document.getElementById('my-recipes-btn');
	const recipesSection = document.getElementById('recipes-section');
	
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

	let currentPage = 1;
	const recipesPerPage = 10;
	let currentCategory = null;
	let currentRecipeId = null;;

	function hideAllSections() {
		console.log("loginSection:", loginSection);
		console.log("registerSection:", registerSection);
		console.log("addRecipeSection:", addRecipeSection);
		console.log("recipesSection:", recipesSection);
		console.log("categorySection:", categorySection);
		console.log("accountSection:", accountSection);
		console.log("comments-list:", document.getElementById('comments-list'));
		console.log("add-comment-section:", document.getElementById('add-comment-section'));
		console.log("comments-header:", document.getElementById('comments-header'));
		console.log("user-info:", document.getElementById('user-info'));
	
		loginSection?.classList.add('hidden');
		registerSection?.classList.add('hidden');
		addRecipeSection?.classList.add('hidden');
		recipesSection?.classList.add('hidden');
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

		// currentRecipeId = null;
		recipesList.innerHTML = '';
		myRecipesList.innerHTML = '';
		// recipesSection.innerHTML = '';
	}

	function isUserLoggedIn() {
		const token = localStorage.getItem('jwtToken');
		console.log('Sprawdzanie tokena JWT:', token);
		return token !== null; 
	}

	// Obsługa przycisku "Rejestracja"
	registerBtn.addEventListener('click', function () {
		console.log("Przycisk 'Rejestracja' został kliknięty");
		hideAllSections();
		registerSection.classList.remove('hidden');
	});

	// Obsługa formularza rejestracji
	registerForm.addEventListener('submit', function (e) {
		e.preventDefault();

		const username = document.getElementById('register-username').value;
		const email = document.getElementById('register-email').value;
		const password = document.getElementById('register-password').value;

		if (username && email && password) {
			console.log('Wysyłanie zapytania do backendu...');

			fetch('http://localhost:8080/register', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({ username, email, password }),
			})
				.then((response) => {
					const contentType = response.headers.get('content-type');
					if (contentType && contentType.includes('application/json')) {
						return response.json();
					} else {
						return response.text();
					}
				})
				.then((data) => {
					console.log('Odpowiedź backendu: ', data);

					if (typeof data === 'object' && data.success === true) {
						alert('Rejestracja udana!'); 
						console.log('Przekierowanie na stronę główną...');
						window.location.href = 'index.html'; 
					} else if (typeof data === 'string') {
						alert('Użytkownik zarejestrowany !');
						window.location.href = 'index.html';
					} else {
						alert('Błąd rejestracji: ' + data.message); 
					}
				})
				.catch((error) => {
					console.error('Błąd podczas rejestracji:', error);
					alert('Wystąpił problem z rejestracją: ' + error.message); 
				});
		} else {
			alert('Wprowadź nazwę użytkownika, email i hasło.'); 
		}
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
		console.log('Formularz logowania wysłany!');

		const usernameOrEmail = document.getElementById('login-usernameOrEmail').value;
		const password = document.getElementById('login-password').value;

		if (usernameOrEmail && password) {
			console.log('Dane logowania: ' + { usernameOrEmail, password });
			fetch('http://localhost:8080/login', {
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
					console.log('Odpowiedź serwera: ', data);
					if (data.jwt) {
						alert('Logowanie udane!');
						localStorage.setItem('jwtToken', data.jwt);

						updateLoginState();
						window.location.href = 'index.html'; // Zmień adres URL na stronę główną aplikacji
					} else {
						alert('Błąd logowania: ' + data.message);
					}
				})
				.catch((error) => {
					console.error('Błąd podczas logowania:', error);
					alert('Błąd podczas logowania: ' + error.message);
				});
		} else {
			console.log('Brak nazwy użytkownika lub hasła!');
		}
	});

	
	// Funkcja do pobierania nazwy użytkownika z tokena JWT
	function getUsernameFromToken(token) {
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.sub || payload.username;
    } catch (error) {
        console.error('Błąd podczas dekodowania tokena JWT:', error);
        return null;
    }
}

	// Funkcja do ustawiania stanu zalogowania
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

	// Obsługa wylogowania
	logoutBtn.addEventListener('click', function () {
		localStorage.removeItem('jwtToken'); 
		alert('Zostałeś wylogowany.');
		updateLoginState();
		window.location.reload();
	});

	// Aktualizacja stanu zalogowania przy załadowaniu strony
	updateLoginState();

	// Obsługa przechodzenia do formularza dodawania przepisu
	addRecipeBtn.addEventListener('click', function () {
		if (isUserLoggedIn()) {
			hideAllSections();
			addRecipeSection.classList.remove('hidden');
		} else {
			alert('Dodawanie przepisu wymaga zalogowania.');
		}
	});

	// Obsługa formularza dodawania przepisu
	recipeForm.addEventListener('submit', function (e) {
		e.preventDefault();
	
		const token = localStorage.getItem('jwtToken');
		if (!token) {
			alert('Token JWT nie został znaleziony. Spróbuj ponownie się zalogować.');
			return;
		}
	
		if (!isUserLoggedIn()) {
			alert('Musisz być zalogowany, aby dodać przepis.');
			return;
		}

		const formData = new FormData(recipeForm);

		console.log('Wysyłanie przepisu:', formData);

		fetch('http://localhost:8080/api/recipes', {
			method: 'POST',
			headers: {
				Authorization: 'Bearer ' + localStorage.getItem('jwtToken'),
			},
			body: formData,
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

	// Obsługa dodawania kroków do przepisu
	addStepBtn.addEventListener('click', function () {
		const stepCount = stepsContainer.children.length + 1;
		const newStepInput = document.createElement('textarea');
		newStepInput.name = 'steps';
		newStepInput.placeholder = `Krok ${stepCount}`;
		newStepInput.required = true;
		stepsContainer.appendChild(newStepInput);
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


	// Obsługa przycisku "Moje konto"
	accountBtn.addEventListener('click', function () {
    	hideAllSections();
    	accountSection.classList.remove('hidden'); 
    	myRecipesList.classList.remove('hidden');
	});


	myRecipesBtn.addEventListener('click', function () {
		recipesSection.classList.add('hidden');
		recipesSection.innerHTML = '';
		myRecipesList.innerHTML = '';

		fetch('http://localhost:8080/api/recipes/my-recipes', {
			method: 'GET',
			headers: {
				'Authorization': 'Bearer ' + localStorage.getItem('jwtToken')
			}
		})
		.then(response => response.json())
		.then(data => {
			console.log('Moje przepisy:', data);
			myRecipesList.innerHTML = ''; 

			data.forEach(recipe => {
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
		.catch(error => {
			console.error('Błąd podczas pobierania przepisów użytkownika:', error);
			alert('Wystąpił problem z pobieraniem przepisów. Spróbuj ponownie.');
		});
	});
	

	// Funkcja do pobierania szczegółów przepisu z sekcji "Moje konto"
	function fetchRecipeDetailsFromMyRecipes(recipeId) {
		currentRecipeId = null;
		currentCategory = null;
		console.log('Fetching details for recipe ID:', recipeId);
	
		fetch(`http://localhost:8080/api/recipes/${recipeId}`)
			.then((response) => {
				if (!response.ok) {
					throw new Error(`Błąd HTTP! Status: ${response.status}`);
				}
				return response.json();
			})
			.then((data) => {
				console.log('Szczegóły przepisu:', data);
	
				myRecipesList.classList.add('hidden');

				recipesSection.classList.add('recipe-details');

				const recipeDetailsDiv = document.createElement('div');
            	recipeDetailsDiv.classList.add('recipe-details');	 
	
				const nameElement = document.createElement('h3');
				nameElement.textContent = data.name;
				recipesSection.appendChild(nameElement);
	
				const ingredientsElement = document.createElement('p');
				ingredientsElement.innerHTML = `<strong>Składniki:</strong> ${data.ingredients}`;
				recipesSection.appendChild(ingredientsElement);
	
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
				recipesSection.appendChild(stepsElement);
	
				// backToMyRecipesBtn.classList.remove('hidden');
				recipesSection.classList.remove('hidden');
			})
			.catch((error) => {
				console.error('Błąd podczas pobierania szczegółów przepisu:', error);
       			alert('Wystąpił błąd podczas pobierania szczegółów przepisu. Spróbuj ponownie.');
			});
	}
	

		// Obsługa powrotu do listy moich przepisów
		backToMyRecipesBtn.addEventListener('click', function () {
			myRecipesList.classList.remove('hidden');
			recipesSection.classList.add('hidden'); 
			recipesSection.innerHTML = ''; 
			// backToMyRecipesBtn.classList.add('hidden'); 
		});

	
	// Funkcja pobierania przepisów po kategorii
	function fetchRecipesByCategory(categoryId, page = 1) {
		currentRecipeId = null;
		currentCategory = categoryId;
	
		hideAllSections();
		
		myRecipesList.innerHTML = '';
		recipesList.innerHTML = '';
		
	
		fetch(`http://localhost:8080/api/recipes/category/${categoryId}`)
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
					
					// TO LOCAL USE
					// const fullImageUrl = `http://localhost:8080${recipe.imageUrl}`;
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
				console.error('Błąd podczas pobierania przepisów:', error);
				alert('Wystąpił problem z pobieraniem przepisów.');
			});
	}
	

	// Obsługa wyświetlania przepisów
	viewRecipesBtn.addEventListener('click', function () {
		hideAllSections();
		currentRecipeId = null; 
   		currentCategory = null; 
    	recipesList.innerHTML = '';
		myRecipesList.innerHTML = ''
		categorySection.classList.remove('hidden');
	});

	
	// Funkcja pobierania szczegółów przepisu
	function fetchRecipeDetails(recipeName) {
		currentRecipeId = null;
		console.log('Fetching details for recipe:', recipeName);

		hideAllSections();
		recipesList.innerHTML = '';
		myRecipesList.innerHTML = '';
   	    currentRecipeId = null;

		fetch(`http://localhost:8080/api/recipes/name/${recipeName}`)
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

				recipesList.appendChild(detailsContainer);

				 currentRecipeId = data.id;
				 document.getElementById('comments-header').classList.remove('hidden');
				 document.getElementById('comments-list').classList.remove('hidden');
				 if (isUserLoggedIn()) {
					 document.getElementById('add-comment-section').classList.remove('hidden');
				 }
	 
				 fetchCommentsForRecipe(currentRecipeId);
				 backToRecipesBtn.classList.remove('hidden');
			 })
			 .catch((error) => {
				 console.error('Błąd podczas pobierania szczegółów przepisu:', error);
				 alert(`Błąd podczas pobierania szczegółów przepisu: ${error.message}`);
			 });
	}

	// Obsługa powrotu do kategorii
	backToCategoriesBtn.addEventListener('click', function () {
		hideAllSections();
    	currentRecipeId = null;
		categorySection.classList.remove('hidden');
	});


	// Obsługa powrotu do przepisów z danej kategorii
	backToRecipesBtn.addEventListener('click', function () {
		currentRecipeId = null; // Zresetuj currentRecipeId
		fetchRecipesByCategory(currentCategory, currentPage);
		backToRecipesBtn.classList.add('hidden');
		backToCategoriesBtn.classList.remove('hidden');
	});

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

	// Obsługa formularza dodawania komentarzy
	document.getElementById('comment-form').addEventListener('submit', function (e) {
    	e.preventDefault();
    
    const content = document.getElementById('comment-content').value;

    // Pobranie wartości wybranej oceny (gwiazdek)
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

    console.log('Dodawanie komentarza dla przepisu o ID:', currentRecipeId);

    fetch(`http://localhost:8080/api/comments/${currentRecipeId}`, {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token, 
            'Content-Type': 'application/json',  
        },
        body: JSON.stringify({
            content: content,   
            rating: rating.value  
        }),
    })
    .then((response) => {
        if (!response.ok) {
            throw new Error(`Błąd HTTP! Status: ${response.status}`);
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


	// funkcja dodawania oceny *
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
	
	// funkcja wyswietlania komentarzy
	function fetchCommentsForRecipe(recipeId) {
	console.log('Pobieranie komentarzy dla przepisu o ID:', recipeId);
    fetch(`http://localhost:8080/api/comments/${recipeId}`)
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
