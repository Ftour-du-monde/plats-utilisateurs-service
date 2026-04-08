# Plats Utilisateurs Service

Microservice Jakarta EE du projet **R4.01 Architecture Logicielle**.

Ce composant expose l'API REST de reference pour les **plats** et les **utilisateurs**. Il est consomme par l'IHM PHP et par les autres microservices.

## 1. Perimetre fonctionnel

### Inclus

- CRUD complet des plats.
- CRUD complet des utilisateurs.
- validations metier (formats, tailles, id, prix, email unique).
- persistence MySQL dediee.
- format d'erreur REST uniforme.
- documentation OpenAPI + guide IHM PHP + diagramme de classes.

### Hors perimetre

- gestion des menus.
- gestion des commandes.
- interface graphique (IHM PHP).

## 2. Architecture (Clean Architecture)

```text
adapters/framework -> application -> domain
```

Arborescence principale:

```text
src/main/java/fr/univamu/iut/platsutilisateursservice/
  adapters/
    in/rest/
    out/persistence/mysql/
  application/
    port/in/
    port/out/
    usecase/
  domain/
    entity/
    exception/
  framework/database/
src/main/resources/
  META-INF/beans.xml
  db/schema.sql
  db/seed.sql
docs/
  openapi-plats-utilisateurs.yaml
  integration-ihm-php.md
  diagramme-classes-clean-architecture.puml
  runbook-glassfish-alwaysdata.md
scripts/
  smoke-api.sh
```

## 3. Prerequis

- Java 11+
- Maven 3.9+ (ou `./mvnw`)
- serveur Jakarta EE (GlassFish/Payara)
- MySQL 8+ (local ou distant)

## 4. Configuration base de donnees

Le `DataSource` est resolu dans cet ordre:

1. JNDI (`PLATS_USERS_DB_JNDI` / `-Dplats.users.db.jndi`)
2. variables d'environnement (`PLATS_USERS_DB_*`)
3. proprietes JVM (`-Dplats.users.db.*`)
4. valeurs locales par defaut

Proprietes supportees:

- `PLATS_USERS_DB_JNDI`
- `PLATS_USERS_DB_URL`
- `PLATS_USERS_DB_USER`
- `PLATS_USERS_DB_PASSWORD`

Equivalent JVM:

- `-Dplats.users.db.jndi=...`
- `-Dplats.users.db.url=...`
- `-Dplats.users.db.user=...`
- `-Dplats.users.db.password=...`

Variables CORS:

- `PLATS_USERS_CORS_ALLOWED_ORIGINS`
- `PLATS_USERS_CORS_ALLOWED_HEADERS`
- `PLATS_USERS_CORS_ALLOWED_METHODS`

## 5. Demarrage rapide

### 5.1 Build

```bash
./mvnw clean package
```

### 5.2 Deploiement GlassFish

```bash
asadmin deploy --name plats-utilisateurs-service \
  --contextroot plats-utilisateurs-service \
  target/plats-utilisateurs-service-1.0-SNAPSHOT.war
```

### 5.3 Endpoint de base

- `http://localhost:8080/plats-utilisateurs-service/api`

## 6. Endpoints REST

- `GET /api/health`
- `GET /api/plats`
- `GET /api/plats/{id}`
- `POST /api/plats`
- `PUT /api/plats/{id}`
- `DELETE /api/plats/{id}`
- `GET /api/utilisateurs`
- `GET /api/utilisateurs/{id}`
- `POST /api/utilisateurs`
- `PUT /api/utilisateurs/{id}`
- `DELETE /api/utilisateurs/{id}`

## 7. Qualite logicielle

### 7.1 Tests unitaires

```bash
./mvnw test
```

### 7.2 Javadoc

```bash
./mvnw javadoc:javadoc
```

### 7.3 Smoke test API deploiee

```bash
./scripts/smoke-api.sh
```

## 8. Regles metier clefs

### Plats

- `id` > 0 pour les operations par identifiant.
- `nom` obligatoire, max 150.
- `description` optionnelle, max 500.
- `prix` obligatoire, strictement positif, max 2 decimales.

### Utilisateurs

- `id` > 0 pour les operations par identifiant.
- `nom` obligatoire, max 120.
- `prenom` obligatoire, max 120.
- `email` obligatoire, valide, normalise en minuscules, unique.
- `adresse` optionnelle, max 255.

## 9. Format d'erreur API

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Le champ 'nom' est obligatoire.",
  "path": "/plats-utilisateurs-service/api/plats",
  "timestamp": "2026-04-08T12:34:56Z"
}
```

## 10. Documentation livree

- Contrat API: `docs/openapi-plats-utilisateurs.yaml`
- Guide IHM PHP: `docs/integration-ihm-php.md`
- Diagramme de classes (PlantUML): `docs/diagramme-classes-clean-architecture.puml`
- Runbook GlassFish + AlwaysData: `docs/runbook-glassfish-alwaysdata.md`

## 11. Notes exploitation

- En environnement AlwaysData, utiliser une URL JDBC explicite (incluant les parametres SSL requis).
- Exemple de configuration operationnelle dans le runbook: `docs/runbook-glassfish-alwaysdata.md`.
- Ne jamais commiter de secrets reels en clair dans le depot.
