# Contributing to StrayToStay

This is one Maven project that produces one WAR. Frontend and backend live in the same
repository but in different folders, so two people can work at the same time without
touching each other's files.

## 1. Who owns what

| Area | Folders | Owners |
|---|---|---|
| Backend | `src/main/java/`, `src/test/`, `src/main/resources/`, `db/`, `pom.xml`, `Dockerfile`, `docker-compose.yml`, `WEB-INF/web.xml` | Ali, Thomas |
| Frontend | `src/main/webapp/WEB-INF/views/`, `src/main/webapp/static/`, `src/main/webapp/*.jsp` | Katerina, Ahmad |
| Shared | `docs/`, `README.md`, `.github/` | everyone, Flavia approves |

Rules that prevent merge conflicts:

1. **Stay in your area.** Frontend PRs do not edit Java, SQL or the POM. Backend PRs do not edit JSP, CSS or JS.
2. **The contract between the two sides is the request attribute.** A servlet puts objects
   into request scope (for example `request.setAttribute("pets", list)`) and forwards to a JSP.
   The JSP only reads those attributes. When you need a new attribute, agree on its name in
   the Trello card before either side starts.
3. **Shared files need a reviewer from each side.** `web.xml`, `pom.xml`, `README.md`.
4. **Small PRs.** One feature or one page per PR. A PR that touches 30 files will conflict.
5. **Rebase on `test` before opening a PR** so conflicts are resolved on your branch, not in the PR.

## 2. Branches

```
main   <-- stable, only receives PRs from test
test   <-- integration branch, receives PRs from feature branches
feature/<area>-<short-name>   <-- your work, created from test
```

Branch names:

- `feature/backend-login-servlet`
- `feature/frontend-pet-list-page`
- `fix/backend-pagination-clamp`
- `docs/a2-services-architecture`

Workflow:

```bash
git checkout test
git pull
git checkout -b feature/backend-user-dao

# ... work, commit often ...

git fetch origin
git rebase origin/test          # bring in what others merged
git push -u origin feature/backend-user-dao
# open a PR: base = test
```

When `test` is stable (end of a sprint / before a demo), Flavia or Ali opens one PR
`test -> main`. Nobody pushes directly to `main` or `test`.

## 3. Commits

Short, present tense, say what and why:

```
Add UserDao with findByEmail and insert
Clamp page number in PetSearchCriteria (invalid values fell through to SQL)
Add pet-list.jsp with search filters
```

## 4. Pull requests

- Fill in the PR template.
- CI (`mvn package`) must be green.
- At least one approval from an owner of the area you changed.
- Squash-merge, so `test` keeps one commit per PR.

## 5. Running locally

```bash
cp .env.example .env
docker compose up --build
# app: http://localhost:8080   health check: http://localhost:8080/health
# db : localhost:5432, user/password from .env
```

Reset the database (drops the volume, re-runs `db/*.sql`):

```bash
docker compose down -v && docker compose up --build
```

Without Docker you need JDK 17, Maven 3.9 and a local PostgreSQL 16. Run `mvn package`
and deploy `target/straytostay.war` to Tomcat 10.1.

## 6. Database changes

The schema is in `db/01_schema.sql`. If you change it:

1. Update `db/01_schema.sql` and, if needed, `db/02_seed.sql`.
2. Update the ER diagram in `docs/diagrams/`.
3. Say so in the PR description so reviewers reset their local database.
