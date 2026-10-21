# Compte rendu – Quiz en images

**Étudiant(e) / binôme :** _à compléter_
**Thème choisi :** formes géométriques (triangle, carré, cercle, étoile, rectangle)

## 1. Interface et widgets

L'interface est définie dans `activity_main.xml` (aucun Compose, une seule activité, aucun fragment).

- **Bandeau fixe** : `TextView` `txt_titre` (à gauche) et `txt_score` (en haut à droite), toujours visibles.
- **`ProgressBar` horizontale** `progress_quiz` (max = 5) : nombre de réponses données.
- **`ScrollView`** contenant le reste, pour les petits écrans, sans faire défiler le bandeau.
- **Zone de jeu** `zone_jeu` : `txt_numero` (« Question 2 sur 5 »), `ImageView` `img_question` (`fitCenter`, sans déformation), `txt_question`, trois `Button` `btn_choix1` à `btn_choix3`, `txt_feedback` et `btn_suivant`.
- **Zone de résultat** `zone_resultat` (masquée par défaut) : `txt_resultat` et `btn_rejouer`.

## 2. Tableau des questions

Une `data class Question(texte, imageRes, choix: Array<String>, indiceBonneReponse, descriptionImage)` représente une question. `MainActivity` déclare un `Array<Question>` de cinq éléments ; l'image est associée par `R.drawable.xxx`. L'indice de la bonne réponse varie (1, 2, 0, 1, 0).

| # | Image | Question | Choix | Bonne réponse |
|---|---|---|---|---|
| 1 | `triangle` | Combien de côtés possède cette forme ? | 4 / 3 / 5 | 1 (« 3 ») |
| 2 | `carre` | Quelle est cette forme aux quatre côtés égaux ? | Triangle / Cercle / Carré | 2 |
| 3 | `cercle` | Cette forme possède-t-elle des angles ? | Non, aucun angle / Oui, trois / Oui, quatre | 0 |
| 4 | `etoile` | Combien de branches possède cette étoile ? | Six / Cinq / Quatre | 1 |
| 5 | `rectangle` | Quelle forme est représentée ici ? | Rectangle / Cercle / Étoile | 0 |

## 3. Événements, score et progression

- **Quel choix a été cliqué ?** Les boutons sont dans une liste ; chaque `setOnClickListener` appelle `verifierReponse(indice)` avec l'indice du bouton (0, 1 ou 2), comparé à `indiceBonneReponse`.
- **Un seul point par question :** la variable `aRepondu` est testée au début de `verifierReponse` (retour immédiat si déjà vrai) et les trois boutons sont désactivés.
- **Score et progression :** le score compte les bonnes réponses ; la progression vaut `indexCourant + 1` si la question courante est répondue, sinon `indexCourant` (5 à la fin). Une mauvaise réponse fait donc avancer la barre sans modifier le score.
- **Dernière question :** si `indexCourant == questions.size - 1`, le bouton affiche « Voir le résultat » et `questionSuivante()` appelle `afficherResultat()` au lieu d'incrémenter l'indice, ce qui évite tout dépassement du tableau.
- **Rejouer :** `rejouer()` remet l'indice, le score et les états à zéro.
- **Accessibilité :** le retour utilise un texte et un symbole (✔ / ✘) en plus de la couleur ; chaque image a une `contentDescription`.

## 4. Tests réalisés

_Remplir après exécution sur émulateur / téléphone._

| Test | Résultat attendu | Résultat obtenu |
|---|---|---|
| Nouvelle partie | Question 1, 3 choix, score 0/5, progression 0/5 | |
| Bonne réponse | Score +1, progression +1 | |
| Mauvaise réponse | Score inchangé, progression +1 | |
| Clics multiples | Aucun point en plus, 3 choix désactivés | |
| Question suivante | Image, texte, choix mis à jour ; score conservé | |
| 5 bonnes réponses | 5/5, progression 5/5 | |
| 5 mauvaises réponses | 0/5, progression 5/5 | |
| Réponses mélangées | Score = nombre de bonnes réponses | |
| Rejouer | Retour à la question 1, score et progression à 0 | |
| Petit écran | Choix accessibles, textes lisibles | |

## 5. Difficultés rencontrées

_À compléter._
