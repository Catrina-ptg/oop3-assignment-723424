"oop3-assignment-723424" 
# Movie Watchlist Backend

This is a Java Spring Boot application that allows users to search for movies, view details, and manage a personal watchlist. It integrates with the OMDb and TMDB APIs to fetch movie data and images, downloads them locally, and stores movie information in a database.

## Features

- Search movies by title using the OMDb API
- Fetch similar movies and images from TMDB
- Download and store images on the local file system
- Store and manage movie metadata including:
  - Title, year, director, genre
  - Watched flag (boolean)
  - Rating (1–5)
  - Image path
- Expose a RESTful API to:
  - Add a movie to the watchlist
  - Retrieve a list of movies (with pagination)
  - Update watched status and rating
  - Delete movies from the watchlist

## Technologies Used

- Java 21
- Spring Boot 3.2.x
- Spring Data JPA
- H2 (in-memory database)
- Maven
- OMDb API
- TMDB API
- JUnit and Mockito for testing

## Setup Instructions

### Prerequisites

- Java 21 or later
- Maven
- OMDb and TMDB API keys

### 1. Clone the repository

```bash
git clone https://github.com/Catrina-ptg/oop3-assignment-723424.git
cd movie-watchlist
```

### 2. Configure API keys

Edit the file located at:

```
src/main/resources/application.properties
```

And insert your keys:

```properties
omdb.api.key=your_omdb_key
tmdb.api.key=your_tmdb_key
```

### 3. Build and run the project

On Linux/macOS:

```bash
./build.sh
```

Or manually:

```bash
mvn clean package
java -jar target/movie-watchlist-1.0.0.jar
```

## API Usage

### Add a movie

```
POST /api/movies?title=The Amateur
```

### Retrieve all movies

```
GET /api/movies
```

### Update watched status

```
PATCH /api/movies/{id}/watched?watched=true
```

### Update rating

```
PATCH /api/movies/{id}/rating?rating=5
```

### Delete a movie

```
DELETE /api/movies/{id}
```

## Running Tests

```bash
mvn test
```

## Notes

This project was developed as part of a backend programming assignment to demonstrate:

- Use of external APIs
- REST API design
- File and image handling
- Basic multithreading
- Unit testing and mocking

