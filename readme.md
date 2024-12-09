# FoodBook Frontend Documentation

## Overview
FoodBook is a dynamic web application designed to help users discover, share, and rate recipes. The frontend is built using modern JavaScript to provide a seamless user experience.

---

## Features
- Browse recipes by categories.
- Add new recipes with images.
- Register and log in to access personalized features.
- Add and view comments and ratings on recipes.
- Manage your account and view your recipes.

---

## Key Functionalities
- **Navigation**:
  - Return to the homepage by clicking the `FoodBook` header.
  - Browse recipes by categories: Breakfast, Lunch, Dinner, and Snacks.
  - Register, log in, and log out using navigation buttons.
  - Access your account to view and manage personal recipes.

- **Recipe Management**:
  - Add new recipes with images and detailed steps.
  - View recipe details, including ingredients, steps, and comments.

- **Comments and Ratings**:
  - Add comments and star ratings to recipes.
  - View comments from other users.

- **Authentication**:
  - Secure user registration and login.
  - JWT-based session management for protected actions.

---

## API Endpoints

### Authentication
- **Register**:  
  `POST /register`  
  Registers a new user with username, email, and password.

- **Login**:  
  `POST /login`  
  Authenticates the user and returns a JWT.

- **Validate Token**:  
  JWT stored in `localStorage` is used for protected routes.

### Recipes
- **Fetch Recipes by Category**:  
  `GET /api/recipes/category/slug/:slug`  
  Retrieves all recipes in a specific category.

- **Fetch Recipe Details**:  
  `GET /api/recipes/slug/:slug`  
  Retrieves detailed information about a recipe by its slug.

- **Add Recipe**:  
  `POST /api/recipes/add`  
  Adds a new recipe. Requires JWT authentication.

### Comments
- **Add Comment**:  
  `POST /api/comments/:recipeId`  
  Submits a comment and rating for a recipe. Requires JWT authentication.

- **Fetch Comments**:  
  `GET /api/comments/:recipeId`  
  Retrieves all comments for a specific recipe.

---


## Project Structure

- **index.html** - Main HTML file for the application
- **css/**
  - **style.css** - Application styling
- **js/**
  - **script.js** - Main JavaScript logic for the application
- **images/** - Contains images used in the application 
- **README.md** - Project documentation
    
---

## Getting Started

### Prerequisites
Before running the application, ensure you have the following installed:
- **Node.js** (version 16.x or higher)
- **npm** (version 7.x or higher)

---

### Installation
1. Clone the repository and switch to the `main` branch:
   ```bash
   git clone -b main https://github.com/lubomat/FoodBook.git
   cd FoodBook
