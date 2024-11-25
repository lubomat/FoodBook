# FoodBook (English version)

## Author
This project was created by **[Mateusz Lubowiecki](https://github.com/lubomat)**.

---

## Project Description
FoodBook is a web application designed for managing culinary recipes. Users can create accounts, add their own recipes, browse recipes from other users, and manage their favorite categories. The project is implemented as a full-stack application using both backend and frontend technologies.

---

## Features
- **User Registration and Login**:
  - Secure login using JWT.
  - Registration and login forms.
- **Recipe Management**:
  - Adding recipes (name, ingredients, steps, image, category).
  - Browsing recipes by category or searching by name.
  - Viewing detailed recipes with a list of steps and ingredients.
- **Comments and Ratings**:
  - Adding comments and ratings to recipes.
  - Displaying the average rating of a recipe.
- **Recipe Categories**:
  - Managing recipe categories (e.g., breakfast, lunch, dinner).
  - Dynamically displaying categories on the website.
- **Responsive Design**:
  - A frontend optimized for both mobile and desktop devices.

---

## Technologies
**Backend**:
- Java 17
- Spring Boot
  - Spring Security (authentication and authorization)
  - Spring Data JPA (database management)
  - Spring Validation (form validation)
- Hibernate (ORM)
- Database: PostgreSQL
- Flyway (database migration management)
- Cloudinary (image storage)

**Frontend**:
- HTML5, CSS3
- JavaScript (Vanilla JS)
- Responsive user interface
- Fetch API (communication with the backend)

---

## Online Demo
The application is available at:  
**[FoodBook on Render](https://foodbook-1.onrender.com/)**

> **Note**: The application is hosted on the free tier of the Render platform, so the first load may take a few minutes as the backend server starts up.

### Test Credentials
To log in and test the application's functionality, you can use:
- Login: **usertest**
- Password: **test**

---

## Project Structure
- `src/`
  - `main/`
    - `java/com/cookBook/CookBook/`
      - `config/` - Spring configuration (e.g., Spring Security)
      - `controller/` - REST controllers
      - `model/` - JPA entities (Category, Recipe, User)
      - `repository/` - Spring Data JPA repositories
      - `service/` - Business logic
      - `security/` - JWT handling and authorization
    - `resources/`
      - `static/` - Frontend (HTML, CSS, JS)
      - `db/migration/` - Flyway migrations
      - `application.yml` - Application configuration
- `README.md` - Project documentation

---

## Installation and Running Locally

### Requirements:
- **Java 17**
- **Maven**
- **PostgreSQL** (with a created database, e.g., `foodbook`)

---

### Steps:
1. **Clone the repository**:
   ```bash
   git clone https://github.com/lubomat/FoodBook.git
   cd FoodBook
   
2. **Configure the .env file**:
   ```bash
   DB_URL=jdbc:postgresql://localhost:5432/foodbook
   DB_USERNAME=your_database_username
   DB_PASSWORD=your_database_password
   
3. **Set up Cloudinary (optional, if you want to use Cloudinary for image management):**:
   ```bash
   CLOUDINARY_CLOUD_NAME=your_cloudinary_name
   CLOUDINARY_API_KEY=your_cloudinary_api_key
   CLOUDINARY_API_SECRET=your_cloudinary_api_secret
   
4. **Build the project: Run the following command in the terminal from the root directory of the project:**:
   ```bash
   mvn clean install
   

5. **Access the application: The application will be available at:**:
   ```bash
   http://localhost:8080



---



# FoodBook (Polska wersja)

## Autor
Projekt został stworzony przez **[Mateusz Lubowiecki](https://github.com/lubomat)**.


## Opis projektu
FoodBook to aplikacja webowa przeznaczona do zarządzania przepisami kulinarnymi. Użytkownicy mogą tworzyć konta,
dodawać własne przepisy, przeglądać przepisy innych użytkowników oraz zarządzać swoimi ulubionymi kategoriami. 
Projekt jest zrealizowany jako pełnoprawna aplikacja typu full-stack z użyciem technologii backendowych i frontendowych.

---

## Funkcje aplikacji
- **Rejestracja i logowanie użytkownika**:
    - Bezpieczne logowanie z użyciem JWT.
    - Formularz rejestracji i logowania.
- **Zarządzanie przepisami**:
    - Dodawanie przepisów (nazwa, składniki, kroki, obrazek, kategoria).
    - Przeglądanie przepisów według kategorii lub wyszukiwanie po nazwie.
    - Wyświetlanie szczegółów przepisu z listą kroków i składników.
- **Komentarze i oceny**:
    - Możliwość dodawania komentarzy oraz ocen do przepisów.
    - Wyświetlanie średniej oceny danego przepisu.
- **Kategorie przepisów**:
    - Zarządzanie kategoriami przepisów (np. śniadanie, obiad, kolacja).
    - Dynamiczne wyświetlanie kategorii na stronie.
- **Responsive Design**:
    - Frontend zoptymalizowany pod kątem urządzeń mobilnych i desktopów.

---

## Technologie
**Backend**:
- Java 17
- Spring Boot
    - Spring Security (autoryzacja i autentykacja)
    - Spring Data JPA (zarządzanie bazą danych)
    - Spring Validation (walidacja formularzy)
- Hibernate (ORM)
- Baza danych: PostgreSQL
- Flyway (zarządzanie migracjami bazy danych)
- Cloudinary (przechowywanie obrazów)

**Frontend**:
- HTML5, CSS3
- JavaScript (Vanilla JS)
- Responsywny interfejs użytkownika
- Fetch API (komunikacja z backendem)

---

## Demo online
Aplikacja jest dostępna pod adresem:  
**[FoodBook na Render](https://foodbook-1.onrender.com/)**

> **Uwaga**: Aplikacja jest hostowana na darmowym planie platformy Render, dlatego pierwsze uruchomienie może potrwać kilka minut (backend musi się obudzić).

### Dane testowe
Aby zalogować się i przetestować funkcjonalności aplikacji, możesz użyć:
- Login: **usertest**
- Hasło: **test**

---

## Struktura projektu
- `src/`
  - `main/`
    - `java/com/cookBook/CookBook/`
      - `config/` - Konfiguracje Spring (np. Spring Security)
      - `controller/` - Warstwa kontrolerów REST
      - `model/` - Encje JPA (Category, Recipe, User)
      - `repository/` - Repozytoria Spring Data JPA
      - `service/` - Logika biznesowa
      - `security/` - Obsługa JWT i autoryzacji
    - `resources/`
      - `static/` - Frontend (HTML, CSS, JS)
      - `db/migration/` - Migracje Flyway
      - `application.yml` - Konfiguracja aplikacji
- `README.md` - Dokumentacja projektu


---

## Instalacja i uruchomienie projektu lokalnie

### Wymagania:
- **Java 17**
- **Maven**
- **PostgreSQL** (z utworzoną bazą danych np. `foodbook`)

---


### Kroki:
1. **Sklonuj repozytorium**:
   ```bash
   git clone https://github.com/lubomat/FoodBook.git
   cd FoodBook

2. **Skonfiguruj plik .env**:
   ```bash
   DB_URL=jdbc:postgresql://localhost:5432/foodbook
   DB_USERNAME=twoja_nazwa_uzytkownika
   DB_PASSWORD=twoje_haslo

3. **Skonfiguruj cloudinary(opcjonalnie, jeśli używasz Cloudinary do obsługi obrazów)**:
   ```bash
   CLOUDINARY_CLOUD_NAME=twoja_nazwa_cloudinary
   CLOUDINARY_API_KEY=twoj_klucz_api
   CLOUDINARY_API_SECRET=twoj_sekret_api

4. **Zbuduj projekt: W terminalu, w katalogu głównym projektu, uruchom poniższe polecenie:**:
   ```bash
   mvn clean install

5. **Uzyskaj dostęp do aplikacji: Aplikacja będzie dostępna pod adresem:**:
   ```bash
   http://localhost:8080

---
