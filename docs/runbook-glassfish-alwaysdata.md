# Runbook GlassFish + AlwaysData

Ce runbook configure le microservice pour utiliser une base MySQL AlwaysData depuis GlassFish.

## 1. Prerequis

- domaine GlassFish demarre (ex: `domain1`)
- WAR construit: `target/plats-utilisateurs-service-1.0-SNAPSHOT.war`
- acces reseau vers l'hote MySQL AlwaysData (`port 3306`)

## 2. Variables a adapter

- `DB_HOST` (ex: `mysql-dashmed.alwaysdata.net`)
- `DB_PORT` (souvent `3306`)
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`

## 3. Configuration JVM GlassFish

Important: `create-jvm-options` interprete `:` comme separateur. Il faut echapper les `:` dans les valeurs JDBC avec `\:`.

Exemple:

```bash
asadmin create-jvm-options '-Dplats.users.db.jndi=java\:comp/env/jdbc/__inexistant__'
asadmin create-jvm-options '-Dplats.users.db.url=jdbc\:mysql\://mysql-dashmed.alwaysdata.net\:3306/dashmed_plats_utilisateurs_service?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8&sslMode=DISABLED'
asadmin create-jvm-options '-Dplats.users.db.user=dashmed'
asadmin create-jvm-options '-Dplats.users.db.password=<PASSWORD>'
```

Pourquoi `plats.users.db.jndi` vers une valeur inexistante:

- le service tente d'abord JNDI, puis fallback sur les proprietes.
- cette valeur force le fallback vers les proprietes DB ci-dessus.

## 4. Redemarrage domaine

```bash
asadmin restart-domain domain1
```

## 5. Deploiement applicatif

```bash
asadmin deploy --force=true --name plats-utilisateurs-service \
  --contextroot plats-utilisateurs-service \
  target/plats-utilisateurs-service-1.0-SNAPSHOT.war
```

## 6. Verification

```bash
curl -s -w '\nHTTP %{http_code}\n' http://localhost:8080/plats-utilisateurs-service/api/health
curl -s -w '\nHTTP %{http_code}\n' http://localhost:8080/plats-utilisateurs-service/api/plats
curl -s -w '\nHTTP %{http_code}\n' http://localhost:8080/plats-utilisateurs-service/api/utilisateurs
```

Attendu:

- `/health` -> `HTTP 200`
- `/plats` -> `HTTP 200`
- `/utilisateurs` -> `HTTP 200`

## 7. Depannage rapide

### Cas: `health` = 200 mais `/plats` = 500

1. verifier les options JVM appliquees:

```bash
asadmin list-jvm-options | grep 'plats.users.db'
```

2. verifier les logs:

```bash
tail -n 200 $GLASSFISH_HOME/glassfish/domains/domain1/logs/server.log
```

3. erreur SSL keystore MySQL (`keystore.jks`) -> s'assurer que l'URL contient `sslMode=DISABLED`.

### Cas: erreur auth DB

- verifier `DB_USER` / `DB_PASSWORD`
- verifier droits SQL sur la base
- verifier que les tables `plats` et `utilisateurs` existent

## 8. Hygiene securite

- ne pas commiter de mot de passe en clair.
- utiliser de preference des secrets stores selon votre environnement.
