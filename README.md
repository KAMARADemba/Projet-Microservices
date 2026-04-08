# 🍽️ Projet de Livraison de Repas - Architecture Microservices
**R4.01 Architecture Logicielle** | BUT Informatique | IUT Aix-en-Provence, Aix-Marseille Université

---

## 📖 Contexte du Projet

L'application permet à une entreprise de restauration de proposer différents plats (salade niçoise, aïoli, gratin dauphinois, etc.).

- Les clients peuvent **combiner ces plats pour constituer des menus**.
- Les abonnés peuvent **commander ces menus en ligne** pour une livraison à domicile à une date précise.
- Le paiement s'effectue à la livraison.

---

## 🏗️ Architecture Logicielle

L'application suit une architecture orientée services (RESTful) divisée en **4 composants indépendants**. Ce dépôt contient **2 composants** développés par le même étudiant dans le même projet Maven/WAR :

### Composants du projet

| Composant | Rôle | Port | Technologies |
|---|---|---|---|
| **Plats & Utilisateurs** | CRUD plats et abonnés | 8080 | Jakarta EE, MariaDB |
| **Commandes** | Gestion des commandes | 8080 | Jakarta EE, MariaDB |
| **Menus** *(mock)* | CRUD menus | 3004 | JSON-Server |
| **IHM** *(hors scope)* | Interface graphique | - | HTML/CSS/PHP |

### Schéma des dépendances

```
IHM → API Commandes → API Menus → API Plats & Utilisateurs
```

- L'API **Commandes** consomme l'API **Menus** (port 3004) lors de la création d'une commande pour récupérer le nom et le prix de chaque menu (snapshot).
- L'API **Menus** consomme l'API **Plats & Utilisateurs** (non implémenté dans ce dépôt, géré par un autre étudiant).

---

## 📁 Structure du Projet

```
src/main/java/
├── api/
│   ├── PlatsUtilisateursApplication.java  ← Point d'entrée JAX-RS + Producers CDI
│   ├── PlatResource.java                  ← Endpoints /api/plats
│   ├── UtilisateurResource.java           ← Endpoints /api/utilisateurs
│   └── CommandeResource.java             ← Endpoints /api/commandes
├── domain/
│   ├── Plat.java
│   ├── Utilisateur.java
│   ├── Commande.java
│   └── LigneCommande.java
├── usecases/
│   ├── PlatService.java
│   ├── UtilisateurService.java
│   └── CommandeService.java
├── repositories/
│   ├── PlatRepositoryInterface.java
│   ├── UtilisateurRepositoryInterface.java
│   └── CommandeRepositoryInterface.java
└── infrastructure/
    ├── PlatRepositoryMariadb.java
    ├── UtilisateurRepositoryMariadb.java
    ├── CommandeRepositoryMariadb.java
    └── MenuServiceClient.java             ← Appel HTTP vers API Menus (port 3004)
src/main/resources/
└── config.properties                      ← Connexion base de données (à créer)
src/main/webapp/WEB-INF/
└── beans.xml                              ← CDI activé (annotated)
```

---

## 🗄️ Base de Données

Le projet utilise **MariaDB** hébergé sur **AlwaysData**.

### Tables

```sql
-- Plats proposés par l'entreprise
CREATE TABLE Plat (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nom         VARCHAR(100) NOT NULL,
    description TEXT,
    prix        DOUBLE NOT NULL
);

-- Abonnés
CREATE TABLE Utilisateur (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nom     VARCHAR(100) NOT NULL,
    prenom  VARCHAR(100) NOT NULL,
    email   VARCHAR(150) NOT NULL UNIQUE,
    adresse VARCHAR(255) NOT NULL
);

-- Commandes passées par les abonnés
CREATE TABLE Commande (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    abonne_id         INT NOT NULL,
    date_commande     DATETIME NOT NULL,
    adresse_livraison VARCHAR(255) NOT NULL,
    date_livraison    DATE NOT NULL,
    prix_total        DOUBLE NOT NULL
);

-- Lignes d'une commande (snapshot nom + prix au moment de la commande)
CREATE TABLE LigneCommande (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    commande_id   INT NOT NULL,
    menu_id       INT NOT NULL,
    menu_nom      VARCHAR(255) NOT NULL,
    quantite      INT NOT NULL,
    prix_unitaire DOUBLE NOT NULL,
    prix_ligne    DOUBLE NOT NULL,
    FOREIGN KEY (commande_id) REFERENCES Commande(id) ON DELETE CASCADE
);
```

---

## ⚙️ Configuration

### 1. Cloner le projet

```bash
git clone https://github.com/KAMARADemba/Projet-Microservices
cd Projet-Microservices
```

### 2. Créer le fichier `config.properties`

Dans `src/main/resources/`, créer un fichier `config.properties` (un exemple est disponible dans `config.properties.example`) :

```properties
db.url=jdbc:mariadb://mysql-[COMPTE].alwaysdata.net/[COMPTE]_db
db.user=[COMPTE]
db.pwd=[MOT_DE_PASSE]
```

### 3. Lancer le mock JSON-Server (API Menus)

Le composant Commandes dépend de l'API Menus. Pour le développement local, on utilise JSON-Server :

```bash
npm install -g json-server
npx json-server --watch menus.json --port 3004
```

Le mock sera disponible sur `http://localhost:3004/menus`.

### 4. Builder le projet

```bash
mvn clean package
```

Le WAR généré : `target/plats-utilisateurs.war`

### 5. Déployer sur GlassFish 7

Déployer le WAR via IntelliJ (Run/Debug configuration GlassFish) ou via l'interface admin :
```
http://localhost:4848
```

---

## 🌐 Endpoints disponibles

> URL de base : `http://localhost:8080/plats-utilisateurs/api`

---

### 🥗 Plats — `/api/plats`

| Méthode | URL | Description |
|---|---|---|
| GET | `/api/plats` | Liste tous les plats |
| GET | `/api/plats/{id}` | Récupère un plat par id |
| POST | `/api/plats` | Crée un nouveau plat |
| PUT | `/api/plats/{id}` | Modifie un plat |
| DELETE | `/api/plats/{id}` | Supprime un plat |

**Exemple body POST/PUT :**
```json
{
  "nom": "Bouillabaisse",
  "description": "Soupe de poissons marseillaise",
  "prix": 15.50
}
```

---

### 👥 Utilisateurs — `/api/utilisateurs`

| Méthode | URL | Description |
|---|---|---|
| GET | `/api/utilisateurs` | Liste tous les abonnés |
| GET | `/api/utilisateurs/{id}` | Récupère un abonné par id |
| POST | `/api/utilisateurs` | Crée un abonné |
| PUT | `/api/utilisateurs/{id}` | Modifie un abonné |
| DELETE | `/api/utilisateurs/{id}` | Supprime un abonné |

**Exemple body POST/PUT :**
```json
{
  "nom": "Dupont",
  "prenom": "Marie",
  "email": "marie.dupont@email.fr",
  "adresse": "12 rue des Lilas, 13001 Marseille"
}
```

---

### 🛒 Commandes — `/api/commandes`

| Méthode | URL | Description |
|---|---|---|
| GET | `/api/commandes` | Liste toutes les commandes |
| GET | `/api/commandes?abonneId={id}` | Filtre les commandes d'un abonné |
| GET | `/api/commandes/{id}` | Récupère une commande par id |
| POST | `/api/commandes` | Crée une commande (**nécessite le mock Menus sur port 3004**) |
| PUT | `/api/commandes/{id}` | Modifie adresse/date de livraison uniquement |
| DELETE | `/api/commandes/{id}` | Annule une commande |

**Exemple body POST (créer une commande) :**
```json
{
  "abonneId": 2,
  "adresseLivraison": "3 avenue Foch, 13002 Marseille",
  "dateLivraison": "2025-05-01",
  "lignes": [
    { "menuId": 1, "quantite": 2 },
    { "menuId": 2, "quantite": 1 }
  ]
}
```

> ⚠️ Lors d'un POST, l'API appelle automatiquement `http://localhost:3004/menus/{id}` pour récupérer le nom et le prix de chaque menu. La `dateCommande` et le `prixTotal` sont calculés côté serveur.

**Exemple body PUT (modifier livraison) :**
```json
{
  "adresseLivraison": "3 rue Paradis, 13001 Marseille",
  "dateLivraison": "2025-04-10"
}
```

---

## 🧱 Stack Technique

| Élément | Technologie |
|---|---|
| Langage | Java 21 |
| Framework | Jakarta EE 10 |
| Serveur | GlassFish 7 |
| Base de données | MariaDB (AlwaysData) |
| Sérialisation JSON | JSON-B (Yasson) |
| Parsing JSON (client HTTP) | JSON-P (Parsson) |
| Build | Maven |
| Mock API | JSON-Server |

---

## 👨‍💻 Auteur

**KAMARA Demba** — BUT Informatique, IUT Aix-en-Provence  
Promotion 2026 — Module R4.01 Architecture Logicielle
