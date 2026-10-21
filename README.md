# Quiz en images (TP Android – Kotlin / XML)

Jeu de quiz de cinq questions illustrées (thème : **formes géométriques**). Pour chaque image, l'utilisateur choisit parmi trois boutons ; l'application gère le score, la progression et l'écran final dans une seule activité (`MainActivity`).

## Contenu du dépôt

| Dossier / fichier | Rôle |
|---|---|
| `app/src/main/java/com/example/quizimages/` | `MainActivity.kt` (logique) et `Question.kt` (data class) |
| `app/src/main/res/layout/activity_main.xml` | Interface XML |
| `app/src/main/res/drawable/` | Les cinq images vectorielles (`triangle`, `carre`, `cercle`, `etoile`, `rectangle`) |
| [`captures/`](captures/) | Captures d'écran (à ajouter, voir ci-dessous) |
| [`compte-rendu/`](compte-rendu/compte-rendu.md) | Compte rendu du travail |

## Ouvrir et lancer l'application

1. Installer **Android Studio** (Koala ou plus récent) avec le JDK 17 fourni.
2. `File > Open…` puis sélectionner le dossier `QuizImages`.
3. Attendre la synchronisation Gradle (le Gradle Wrapper 8.7 est inclus ; `local.properties` est créé automatiquement).
4. Choisir un émulateur ou un téléphone (Android 7.0 / API 24 minimum) et cliquer sur **Run ▶**.

En ligne de commande : `./gradlew assembleDebug`.

## Règles du jeu

- Un clic sur un choix valide la réponse : +1 point si elle est juste, 0 sinon ; la progression avance dans les deux cas.
- Après la réponse, les trois boutons sont désactivés ; **Question suivante** (ou **Voir le résultat** à la dernière question) devient disponible.
- L'écran final affiche le score sur 5 et le bouton **Rejouer**.
- La rotation de l'écran conserve la partie (`onSaveInstanceState`).

## Captures d'écran à produire

Lancer l'application et enregistrer dans `captures/` : `question.png`, `bonne-reponse.png`, `mauvaise-reponse.png`, `ecran-final.png`.
