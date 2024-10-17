# Używamy oficjalnego obrazu JDK 17 jako bazowego
FROM openjdk:17-jdk-alpine

# Ustawiamy katalog roboczy wewnątrz kontenera
WORKDIR /app

# Kopiujemy pliki Mavena do kontenera
COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn

# Pobieramy zależności projektu
RUN ./mvnw dependency:resolve

# Kopiujemy cały projekt do kontenera
COPY src ./src

# Budujemy projekt (generujemy plik .jar)
RUN ./mvnw clean package -DskipTests

# Kopiujemy plik .jar do katalogu /app w kontenerze
COPY target/FoodBook-0.0.1-SNAPSHOT.jar /app/FoodBook.jar

# Wystawiamy port 8080, na którym działa aplikacja Spring Boot
EXPOSE 8080

# Uruchamiamy aplikację
CMD ["java", "-jar", "/app/FoodBook.jar"]
