# StrayToStay

A web platform connecting pet shelters with adopters and foster caregivers.

## Team — Group 7
- Flavia Viana — Project Manager
- Aikaterini Ioannidis — Frontend
- Ahmad Abu-Khater — Frontend
- Ali Cagri Demir — Backend
- Thomas Longtin — Backend

## Tech Stack
- Frontend: HTML, CSS, Vanilla JavaScript, JSP, JSTL
- Backend: Java, Jakarta Servlets, Apache Tomcat
- Database: PostgreSQL
- Build & containerization: Maven, Docker
- Architecture: Monolithic, MVC

## Branch Strategy
- `main` — stable branch, only receives pull requests from `test`
- `test` — integration branch; feature branches merge here via pull request
- `feature/<area>-<name>` — created off `test` (e.g. `feature/backend-login`, `feature/frontend-pet-list`)

See [CONTRIBUTING.md](CONTRIBUTING.md) for the full workflow and the frontend/backend ownership rules.

## Project Structure
```
stray-to-stay/
├── .github/                 CODEOWNERS, PR template, CI workflow (mvn package on every PR)
├── db/                      01_schema.sql, 02_seed.sql — run automatically by the db container
├── docs/diagrams/           architecture, class, ER and sequence diagrams (png + svg)
├── src/main/java/com/group7/straytostay/
│   ├── controller/          servlets                      (backend)
│   ├── filter/              auth + CSRF filters           (backend)
│   ├── service/             business rules                (backend)
│   ├── dao/                 data access, PreparedStatement (backend)
│   ├── model/               immutable models, enums       (backend)
│   └── util/                connection pool, hashing, validation (backend)
├── src/main/resources/      logback.xml
├── src/main/webapp/
│   ├── WEB-INF/web.xml      session + error-page config   (backend)
│   ├── WEB-INF/views/       JSP pages by area             (frontend)
│   ├── static/css|js|img    stylesheets, scripts, images  (frontend)
│   └── index.jsp            landing page                  (frontend)
├── src/test/java/           JUnit 5 + Mockito tests
├── pom.xml                  Maven build (Java 17, WAR)
├── Dockerfile               two-stage build: Maven -> Tomcat 10.1
└── docker-compose.yml       app + PostgreSQL 16
```

## Setup
Requires Docker Desktop. Nothing else needs to be installed.

```bash
git clone https://github.com/Flaviasv/stray-to-stay.git
cd stray-to-stay
cp .env.example .env
docker compose up --build
```

- App: http://localhost:8080 (health check: http://localhost:8080/health)
- Database: `localhost:5432`, database/user/password from `.env`
- Sample logins (seed data): `staff@ottawahumane.example` and `adopter@example.com`, password `Password1!`

Reset the database: `docker compose down -v && docker compose up --build`
