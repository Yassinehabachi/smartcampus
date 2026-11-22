# SmartCampus

![Java 17](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-6DB33F?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/Kubernetes-manifests%20%2B%20Helm-326CE5?logo=kubernetes&logoColor=white)
![CI](https://github.com/Yassinehabachi/smartcampus/actions/workflows/ci-cd.yml/badge.svg)

REST API for booking rooms on a university campus. It lists rooms (amphitheatres, labs, classrooms) and stores reservations by room, person, date and time slot. It is also a DevOps showcase: containerised, deployed on Kubernetes, and built through a CI/CD pipeline with a security scan.

**Live demo:** https://smartcampus-4mpm.onrender.com (free tier, sleeps after 15 minutes idle, so the first request can take about a minute. Demo data is fictional and resets on restart. The demo runs on an in-memory H2 database.)

## Architecture

```
                 +-----------------+        +--------------------+
  HTTP client -> | smartcampus API | -----> | PostgreSQL 15      |
                 | Spring Boot 8080|  JPA   | smartcampus_db     |
                 +-----------------+        +--------------------+
```

- **Docker Compose**: two services, `api` (port 8082 on the host) and `db` (PostgreSQL with a healthcheck, data in a named volume). Credentials come from `.env`.
- **Kubernetes** (`k8s/`): `smartcampus` Deployment (3 replicas, probes on `/actuator/health`) behind a ClusterIP Service, a `postgres` Deployment and Service, a ConfigMap and a Secret.
- **Helm** (`helm/smartcampus/`): the same stack as a chart with configurable values.

## Tech stack

Java 17, Spring Boot 3.2.5 (Web, Data JPA, Actuator), PostgreSQL 15 (H2 for tests and the hosted demo), Maven, Docker (multi-stage, non-root), Kubernetes, Helm, GitHub Actions, Trivy, GHCR.

## Quickstart

```bash
git clone https://github.com/Yassinehabachi/smartcampus.git
cd smartcampus
cp .env.example .env        # then set POSTGRES_PASSWORD
docker compose up --build
```

The API is at http://localhost:8082. The Docker build compiles the app itself, so no local Java or Maven is needed.

Without Docker: `mvn spring-boot:run -Dspring-boot.run.profiles=demo` starts the API on port 8080 with an in-memory database.

## API

| Method | Path | Description |
| --- | --- | --- |
| GET | `/` | Welcome message |
| GET | `/api/rooms` | List rooms |
| GET | `/api/rooms/{id}` | One room (404 if unknown) |
| POST | `/api/rooms` | Create a room |
| GET | `/api/reservations` | List reservations |
| POST | `/api/reservations` | Create a reservation (400 if `roomId` is unknown) |
| GET | `/actuator/health` | Health check |

```bash
curl http://localhost:8082/api/rooms

curl -X POST http://localhost:8082/api/rooms \
  -H 'Content-Type: application/json' \
  -d '{"name":"Salle TD 12","capacity":40}'

curl -X POST http://localhost:8082/api/reservations \
  -H 'Content-Type: application/json' \
  -d '{"roomId":1,"reservedBy":"Yassine","date":"2026-11-10","timeSlot":"08:30-10:30"}'
```

## CI/CD and DevSecOps

`.github/workflows/ci-cd.yml` runs on every push and pull request to `main`:

1. **Build and test**: `mvn clean package` with JDK 17, including the MockMvc tests. The JAR is uploaded as an artifact.
2. **Docker build**: builds the image from the multi-stage Dockerfile.
3. **Trivy scan**: reports CRITICAL and HIGH vulnerabilities in OS packages and libraries.
4. **Publish**: on `main`, pushes the image to GitHub Container Registry (`ghcr.io/yassinehabachi/smartcampus/smartcampus-api`) tagged `latest` and with the commit SHA.

A `Jenkinsfile` with build and test stages is included as an alternative pipeline.

## Kubernetes

```bash
docker build -t smartcampus:1.0 .
kubectl apply -f k8s/
# or with Helm
helm install smartcampus helm/smartcampus
```

The k8s Secret and Postgres manifest hold demo credentials. Replace them before any real use.

## Tests

```bash
mvn test
```

Tests use the `test` profile (in-memory H2), so no database is required.
