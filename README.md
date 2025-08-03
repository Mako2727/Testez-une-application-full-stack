# Projet Full Stack Yoga-App

## 📦 Technologies utilisées

- **Frontend** : Angular 14  
- **Backend** : Spring Boot (Java)  
- **Base de données** : MySQL  
- **Tests Front-end** : Jest (unitaires, intégration), Cypress (end-to-end)  
- **Tests Back-end** : JUnit (unitaires, intégration), Jacoco (couverture)  

---

## 🚀 Installation

### 1. Forker le projet

- Rendez-vous sur le dépôt GitHub du projet :  
  https://github.com/Mako2727/Testez-une-application-full-stack  
- Cliquez sur le bouton **Fork** en haut à droite pour créer une copie dans votre compte GitHub.

### 2. Cloner le projet avec GitHub Desktop

- Ouvrez GitHub Desktop.  
- Cliquez sur **File > Clone repository**.  
- Sélectionnez le dépôt que vous venez de forker dans l’onglet GitHub.com.  
- Choisissez un dossier de destination local.  
- Cliquez sur **Clone**.

---

### 3. Configuration de la base de données

- Assurez-vous que MySQL est installé et lancé.  
- Vérifiez que le fichier `application.properties` (backend) contient les bonnes informations de connexion :  
  ```
  spring.datasource.url=jdbc:mysql://localhost:3306/test?allowPublicKeyRetrieval=true
  spring.datasource.username=user
  spring.datasource.password=123456
  ```
- Créez la base de données avec le script SQL disponible dans :  
  `ressources/sql/script.sql`

---

### 4. Installation des dépendances

- **Frontend** :  
  ```bash
  npm install
  ```
- **Backend** :  
  ```bash
  mvn clean install
  ```

---

### 5. Lancer l’application Full Stack

- **Frontend** :  
  ```bash
  ng serve
  ```  
  L’application sera accessible à l’adresse : http://localhost:4200

- **Backend** :  
  ```bash
  mvn spring-boot:run
  ```

---

## 🧪 Exécution des tests

### Frontend

- **Tests unitaires et d’intégration (Jest)**  
  Lancer les tests :  
  ```bash
  npm run test
  ```  
  Rapport de couverture disponible ici :  
  `/front/coverage/jest/index.html`

- **Tests end-to-end (Cypress)**  
  Lancer les tests e2e en mode CI :  
  ```bash
  npm run e2e:ci
  ```  
  Générer le rapport de couverture :  
  ```bash
  npx nyc report --reporter=lcov --reporter=text-summary
  ```  
Le rapport se trouve ici
/front/coverage/lcov-report/index.html

  Ouvrir l’interface Cypress pour visualiser les tests :  
  ```bash
  npm run cypress:open
  ```

---

### Backend

- Lancer les tests unitaires et d’intégration avec couverture Jacoco :  
  ```bash
  mvn clean test jacoco:report
  ```  
- Les fichiers DTO sont exclus de la couverture grâce à la configuration suivante dans `pom.xml` :
  ```xml
  <excludes>
    <exclude>**/*Dto.class</exclude>
  </excludes>
  ```  
- Rapport de couverture Jacoco disponible ici :  
  `target/site/jacoco/index.html`

  les captures d'écrans des rapportq de couverture se trouvent ici:
docs\screenshot


