package com.example.quizimages

import androidx.annotation.DrawableRes

/**
 * Représente une question du quiz.
 *
 * @property texte             énoncé de la question
 * @property imageRes          identifiant de l'image dans res/drawable (ex. R.drawable.triangle)
 * @property choix             exactement trois propositions de réponse
 * @property indiceBonneReponse indice (0, 1 ou 2) de la bonne réponse dans [choix]
 * @property descriptionImage  texte d'accessibilité décrivant l'image
 */
data class Question(
    val texte: String,
    @DrawableRes val imageRes: Int,
    val choix: Array<String>,
    val indiceBonneReponse: Int,
    val descriptionImage: String
)
