# RAG Application (Spring Boot + React)
## Sommaire

1. [Qu’est-ce qu’un RAG ?](#1-qu’est-ce-quun-rag-)
2. [Architecture de l’application](#2-architecture-de-lapplication)
3. [Technologies utilisées](#3-technologies-utilisées)
4. [Configuration après clonage](#4-configuration-après-clonage)
5. [Docker](#5-docker)
6. [Lancer l’application](#6-lancer-lapplication)
7. [Notes importantes / Dépannage](#7-notes-importantes--dépannage)
8. [Vedios pour l'explication](#8-vedios-pour-l'explication)
   
## 1. Qu’est-ce qu’un RAG ?

RAG signifie **Retrieval-Augmented Generation**. C’est une approche qui combine la recherche d’informations pertinentes avec la génération de texte par un modèle de langage.

- **Indexing (Indexation)** : Les documents (PDF, texte…) sont transformés en embeddings (vecteurs numériques) et stockés dans une base de données vectorielle comme ChromaDB.
- **Retrieval (Recherche)** : Le système recherche les segments pertinents dans l’index.
- **Augmented Generation** : Les segments récupérés servent à construire un prompt pour le modèle LLM, qui génère la réponse.

---

## 2. Architecture de l’application

utilisateur uplaod pdf -> springController -> DocumentLoaderService (Pdf->Text->Segment) -> EmbeddingStrore (ChromaDB) [Segements->  Embeddings] -> ChatAssistantService (askQuestion)->ContentRetriever (EmbeddingStore+Model) Recupere segments pertinents ->Construction Prompt -> AssitantChat -> ReponseLLM->Utilisateur


### Explication :

1. L’utilisateur upload un PDF.
2. Le PDF est transformé en texte puis segmenté.
3. Chaque segment est transformé en embedding et stocké dans ChromaDB pour la recherche sémantique.
4. Lorsqu’une question est posée, les segments pertinents sont récupérés.
5. Le modèle de langage génère la réponse à partir de ces segments.
6. La conversation est sauvegardée dans MongoDB.

---

## 3. Technologies utilisées

- **LangChain4j** : Orchestrateur pour RAG.
- **Ollama (local)** : Modèle LLM pour la génération de texte.
- **ChromaDB** : Stockage vectoriel des embeddings.
- **MongoDB** : Stockage des conversations.
- **OpenAI** : Génération de texte additionnelle si nécessaire.
- **Spring Boot** : Backend.
- **React** : Frontend.

---

## 4. Configuration après clonage

### Backend Spring Boot (`application.properties`)

Créez le fichier `src/main/resources/application.properties` :

```properties
spring.application.name=rag
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

# MongoDB
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=mongodb

# Ollama
ollama.url=http://localhost:11434
ollama.embedding=nomic-embed-text
model.timeout=30

# ChromaDB
chroma.url=http://localhost:8000

# OpenAI
openai.url=https://openrouter.ai/api/v1
openai.key=VOTRE_CLE_OPENAI
openai.chat=gpt-3.5-turbo
```
### Frontend React (.env)

Créez le fichier  ` .env  ` à la racine du projet React :
```
REACT_APP_API_BASE_URL=http://localhost:votre_port
REACT_APP_ASK_ENDPOINT=/notebook/ask
REACT_APP_UPLOAD_ENDPOINT=/notebook/upload
REACT_APP_CONVERSATIONS_ENDPOINT=/notebook/conversations
```
## 5. Docker

L’application utilise Docker pour :

- Ollama : ollama:latest
- ChromaDB : chromadb:0.4.24 (version 1)
- MongoDB : mongo:7

-**Commandes utiles** :
Lancer Docker Compose :
 `docker-compose up --build  `

Pull du modèle d’embedding Ollama :
 `docker exec ollama ollama pull nomic-embed-text `

## 6. Lancer l’application
### 1. Backend Spring Boot
 `mvn spring-boot:run `
ou depuis votre IDE  `(IntelliJ, Eclipse,Spring Tools for Eclipse) `.

### 2. Frontend React
Installer node_modules : `npm install  `
Lancer le frontend : ` npm start `
## 7. Notes importantes / Dépannage
### 1. Docker :
-Assurez-vous que les ports 11434 (Ollama), 8000 (ChromaDB) et 27017 (MongoDB) ne sont pas utilisés par d’autres services.
-Si vous avez des erreurs de container, utilisez : `docker ps  `pour vérifier 
 `docker stop <container> ` pour arrêter les containers conflictuels.

### 2. Ollama Embedding :
- Si le modèle nomic-embed-text n’est pas téléchargé, utilisez la commande :
 `docker exec ollama ollama pull nomic-embed-text`
- Vérifiez que Ollama tourne avant de lancer le backend.

### 3. MongoDB :

- Vérifiez que la base mongodb est accessible sur localhost:27017.
- Les conversations sont automatiquement créées à la première interaction.

### 4.Frontend React :

- Assurez-vous que le backend est lancé avant de démarrer le frontend.
- Si les requêtes échouent, vérifiez les URLs dans .env.

### 5. Timeouts LLM :

- Pour des PDF volumineux, ajustez  `model.timeout ` dans application.properties.
## 8. Vedio pour l'explication 
[Vedio d'explication](https://drive.google.com/file/d/1IJyk4gl97fqb2K-tLtN6LOrPMmMG2Se_/view)
[Resultat](https://drive.google.com/file/d/1G5Ph6QOPTGlQIa0JdBO03HnHUI323st9/view)



