# Integration IHM PHP - API Plats & Utilisateurs

## 1. Base URL

Configurer une variable unique cote PHP:

```php
$apiBaseUrl = 'http://localhost:8080/plats-utilisateurs-service/api';
```

## 2. Contrat API

- OpenAPI: `docs/openapi-plats-utilisateurs.yaml`
- Health check: `GET /health`

## 3. Helper PHP recommande

```php
<?php
function apiRequest(string $method, string $url, ?array $payload = null): array {
    $ch = curl_init($url);
    $headers = ['Accept: application/json'];

    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, $method);

    if ($payload !== null) {
        $json = json_encode($payload, JSON_UNESCAPED_UNICODE);
        $headers[] = 'Content-Type: application/json';
        curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
    }

    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);

    $raw = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    $curlError = curl_error($ch);
    curl_close($ch);

    if ($raw === false) {
        throw new RuntimeException('Erreur cURL: ' . $curlError);
    }

    $body = $raw !== '' ? json_decode($raw, true) : null;
    return ['status' => $httpCode, 'body' => $body];
}
```

## 4. Endpoints consommes par l'IHM

### Plats

- `GET /plats` -> liste
- `GET /plats/{id}` -> detail
- `POST /plats` -> creation
- `PUT /plats/{id}` -> modification
- `DELETE /plats/{id}` -> suppression

Payload create/update:

```json
{
  "nom": "Salade nicoise",
  "description": "Thon, tomates, oeufs, olives",
  "prix": 10.50
}
```

### Utilisateurs

- `GET /utilisateurs` -> liste
- `GET /utilisateurs/{id}` -> detail
- `POST /utilisateurs` -> creation
- `PUT /utilisateurs/{id}` -> modification
- `DELETE /utilisateurs/{id}` -> suppression

Payload create/update:

```json
{
  "nom": "Dupont",
  "prenom": "Alice",
  "email": "alice.dupont@example.com",
  "adresse": "12 rue de Provence, Marseille"
}
```

## 5. Codes HTTP a gerer cote IHM

- `200` OK
- `201` Cree (header `Location` expose)
- `204` Supprime
- `400` Erreur de validation
- `404` Ressource inexistante
- `409` Conflit metier (email deja utilise)
- `500` Erreur interne

Format d'erreur JSON:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Le champ 'nom' est obligatoire.",
  "path": "/plats-utilisateurs-service/api/plats",
  "timestamp": "2026-04-08T12:34:56Z"
}
```

## 6. CORS (si appels AJAX navigateur)

Le service renvoie deja les en-tetes CORS.

Variables optionnelles:

- `PLATS_USERS_CORS_ALLOWED_ORIGINS` (default `*`)
- `PLATS_USERS_CORS_ALLOWED_HEADERS`
- `PLATS_USERS_CORS_ALLOWED_METHODS`

Exemple strict en dev:

```bash
export PLATS_USERS_CORS_ALLOWED_ORIGINS="http://localhost:8000"
```

## 7. Validation metier appliquee

- `id` strictement positif
- `nom` et `prenom` obligatoires
- `email` valide et normalise en minuscules
- `prix` strictement positif, max 2 decimales
- tailles max alignees avec le schema SQL

## 8. Checklist integration IHM

1. Verifier `GET /health` avant appels fonctionnels.
2. Centraliser tous les appels via un helper HTTP unique.
3. Afficher les messages `message` de l'API en cas d'erreur 4xx.
4. Gerer proprement les 5xx (message technique generique cote UI).
5. Verifier les redirections/refresh apres `201` et `204`.

## 9. Troubleshooting

- Si `health` = 200 mais `plats/utilisateurs` = 500: verifier la configuration DB du microservice.
- Pour GlassFish + AlwaysData: suivre `docs/runbook-glassfish-alwaysdata.md`.
