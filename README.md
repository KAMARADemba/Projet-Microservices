# 🍽️ Projet de Livraison de Repas - Architecture Microservices

**R4.01 Architecture Logicielle** | BUT Informatique | IUT Aix-en-Provence, Aix-Marseille Université

Ce projet s'inscrit dans le cadre du module **R4.01 - Architecture Logicielle**. Il a pour objectif la mise en pratique du patron d'architecture **Microservices** à travers le développement d'une application web de livraison de repas à domicile.

---

## 📖 Contexte du Projet

L'application permet à une entreprise de restauration de proposer différents plats (salade niçoise, aïoli, gratin dauphinois, etc.). 
- Les clients peuvent **combiner ces plats pour constituer des menus** pour une personne.
- Les abonnés peuvent **commander ces menus en ligne** pour une livraison à domicile à une date précise. 
- Le paiement s'effectue à la livraison.

*(Note : L'ajout de plats par l'entreprise et l'inscription des abonnés sont gérés via les API mais ne sont pas obligatoirement intégrés à l'interface graphique par manque de temps).*

---

## 🏗️ Architecture Logicielle

L'application suit une architecture orientée services (RESTful) et est divisée en **4 composants logiciels indépendants**. Chaque composant possède sa propre base de données.

1. **🖥️ Composant "IHM" (Frontend)**
   - **Rôle :** Gère l'interface graphique avec l'utilisateur.
   - **Technologies :** HTML / CSS / PHP.

2. **👥 Composant "Plats & Utilisateurs" (API)**
   - **Rôle :** Gère l'accès et la manipulation des données liées aux plats et aux utilisateurs.
   - **Technologies :** Jakarta EE (API REST, opérations CRUD).

3. **📋 Composant "Menus" (API)**
   - **Rôle :** Gère les opérations sur les menus (création, modification, etc.).
   - **Particularité :** Consomme l'API du composant "Plats & Utilisateurs".
   - **Technologies :** Jakarta EE (API REST, opérations CRUD).

4. **🛒 Composant "Commandes" (API)**
   - **Rôle :** Gère la validation et le suivi des commandes (calcul des prix, dates de livraison, etc.).
   - **Particularité :** Consomme l'API du composant "Menus".
   - **Technologies :** Jakarta EE (API REST, opérations CRUD).

---

## 🛠️ Développement Local et Mocking (JSON-Server)

Afin de pouvoir développer chaque composant indépendamment sans attendre que les autres API soient terminées, nous utilisons **JSON-Server** pour mocker les endpoints.

### 1. Installation de JSON-Server
Assurez-vous d'avoir [Node.js](https://nodejs.org/) installé, puis lancez :
```bash
npm install -g json-server
