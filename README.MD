# Aplikacja do Szyfrowania i Deszyfrowania Wiadomości

## Opis projektu
Projekt zakłada stworzenie aplikacji webowej umożliwiającej szyfrowanie i deszyfrowanie wiadomości. Składa się ona z dwóch głównych komponentów: front-endu i back-endu.

### Technologie
- **Front-end:** JavaScript z wykorzystaniem frameworka React.
- **Back-end:** Java z wykorzystaniem frameworka Spring, działający na Spring Boot.

## Funkcjonalności
- Szyfrowanie i deszyfrowanie wiadomości przez użytkownika.
- Formularz logowania z walidacją danych.
- Zapisywanie i odczytywanie zaszyfrowanych wiadomości z bazy danych PostgreSQL.
- Zabezpieczenie przed atakami typu SQL injection i XSS.
- Kontrola dostępu do zaszyfrowanych wiadomości dla uprawnionych użytkowników.

## konfiguracją parametrów środowiskowych
W celu uzupełnienia kluczy i haseł należy utworzyć plik `application.properties` w folderze `backend/src/main/resources` i dodać do niego następujące linie:
```properties
spring.application.name=
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.session.jdbc.initialize-schema=always
```
oraz .env
```properties
spring.datasource.username=
spring.datasource.password=
#Generate JWS, base64 required https://8gwifi.org/jwsgen.jsp
jwt.secret=
```

## Uruchomienie
```bash
docker-compose up --build

```
