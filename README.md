# FoodBook

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



