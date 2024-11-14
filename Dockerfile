# Używamy oficjalnego obrazu JDK 17 jako bazowego
FROM openjdk:17-jdk-alpine

# Ustawiamy katalog roboczy wewnątrz kontenera
WORKDIR /app

# Kopiujemy plik pom.xml i skrypty Mavena do kontenera
COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn

# Ustawiamy prawa do wykonywania skryptu mvnw
RUN chmod +x ./mvnw

# Pobieramy zależności projektu (bez budowania)
RUN ./mvnw dependency:go-offline

# Kopiujemy cały projekt (kod źródłowy)
COPY src ./src

# Budujemy projekt (generujemy plik .jar)
RUN ./mvnw clean package -DskipTests

# Używamy skompilowanego pliku .jar
CMD ["java", "-jar", "./target/FoodBook-0.0.2-SNAPSHOT.jar"]

# Wystawiamy port 8080, na którym działa aplikacja Spring Boot
EXPOSE 8080
