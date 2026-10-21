package com.example.quizimages

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.quizimages.databinding.ActivityMainBinding

/**
 * Unique activité du quiz en images (thème : les formes géométriques).
 * Toute la logique (questions, score, progression, résultat) est ici.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /** Les cinq questions statiques du quiz. La bonne réponse n'est pas toujours au même endroit. */
    private val questions: Array<Question> = arrayOf(
        Question(
            "Combien de côtés possède cette forme ?",
            R.drawable.triangle,
            arrayOf("4", "3", "5"),
            1,
            "Un triangle rouge"
        ),
        Question(
            "Quelle est cette forme aux quatre côtés égaux ?",
            R.drawable.carre,
            arrayOf("Triangle", "Cercle", "Carré"),
            2,
            "Un carré bleu"
        ),
        Question(
            "Cette forme possède-t-elle des angles ?",
            R.drawable.cercle,
            arrayOf("Non, aucun angle", "Oui, trois", "Oui, quatre"),
            0,
            "Un cercle vert"
        ),
        Question(
            "Combien de branches possède cette étoile ?",
            R.drawable.etoile,
            arrayOf("Six", "Cinq", "Quatre"),
            1,
            "Une étoile jaune à cinq branches"
        ),
        Question(
            "Quelle forme est représentée ici ?",
            R.drawable.rectangle,
            arrayOf("Rectangle", "Cercle", "Étoile"),
            0,
            "Un rectangle violet"
        )
    )

    // --- État de la partie ---
    private var indexCourant = 0          // indice de la question affichée
    private var score = 0                 // nombre de bonnes réponses
    private var aRepondu = false          // la question courante a-t-elle reçu une réponse ?
    private var partieTerminee = false    // l'écran final est-il affiché ?

    /** Les trois boutons de choix, dans l'ordre : l'indice du bouton = l'indice du choix. */
    private lateinit var boutonsChoix: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        boutonsChoix = listOf(binding.btnChoix1, binding.btnChoix2, binding.btnChoix3)

        // Un seul gestionnaire par bouton : on lui transmet l'indice du choix (0, 1 ou 2)
        boutonsChoix.forEachIndexed { indice, bouton ->
            bouton.setOnClickListener { verifierReponse(indice) }
        }
        binding.btnSuivant.setOnClickListener { questionSuivante() }
        binding.btnRejouer.setOnClickListener { rejouer() }

        // Restauration facultative après rotation de l'écran
        if (savedInstanceState != null) {
            indexCourant = savedInstanceState.getInt(KEY_INDEX, 0).coerceIn(0, questions.size - 1)
            score = savedInstanceState.getInt(KEY_SCORE, 0)
            aRepondu = savedInstanceState.getBoolean(KEY_REPONDU, false)
            partieTerminee = savedInstanceState.getBoolean(KEY_TERMINEE, false)
        }

        if (partieTerminee) {
            afficherResultat()
        } else {
            afficherQuestion()
            if (aRepondu) restaurerEtatApresReponse()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, indexCourant)
        outState.putInt(KEY_SCORE, score)
        outState.putBoolean(KEY_REPONDU, aRepondu)
        outState.putBoolean(KEY_TERMINEE, partieTerminee)
    }

    /** Affiche la question courante : numéro, image, texte et trois choix. */
    private fun afficherQuestion() {
        val question = questions[indexCourant]

        binding.zoneJeu.visibility = View.VISIBLE
        binding.zoneResultat.visibility = View.GONE

        binding.txtNumero.text = getString(R.string.numero_format, indexCourant + 1, questions.size)
        binding.imgQuestion.setImageResource(question.imageRes)
        binding.imgQuestion.contentDescription = question.descriptionImage
        binding.txtQuestion.text = question.texte

        boutonsChoix.forEachIndexed { indice, bouton ->
            bouton.text = question.choix[indice]
            bouton.isEnabled = true
        }

        // Pas encore de réponse : message vide et navigation bloquée
        binding.txtFeedback.visibility = View.INVISIBLE
        binding.btnSuivant.isEnabled = false
        binding.btnSuivant.text = getString(
            if (indexCourant == questions.size - 1) R.string.voir_resultat else R.string.question_suivante
        )
        mettreAJourBandeau()
    }

    /**
     * Vérifie le choix sélectionné. La garde [aRepondu] garantit qu'une question
     * ne rapporte jamais plus d'un point, même en cas de clics répétés.
     */
    private fun verifierReponse(indiceChoisi: Int) {
        if (aRepondu || partieTerminee) return
        aRepondu = true

        val question = questions[indexCourant]
        val estJuste = indiceChoisi == question.indiceBonneReponse
        if (estJuste) score++

        afficherCorrection(estJuste, question)
        mettreAJourBandeau()
    }

    /** Affiche le message juste/faux, désactive les choix et active la navigation. */
    private fun afficherCorrection(estJuste: Boolean, question: Question) {
        boutonsChoix.forEach { it.isEnabled = false }

        if (estJuste) {
            binding.txtFeedback.text = getString(R.string.bonne_reponse)
            binding.txtFeedback.setTextColor(ContextCompat.getColor(this, R.color.juste))
        } else {
            binding.txtFeedback.text = getString(
                R.string.mauvaise_reponse_format,
                question.choix[question.indiceBonneReponse]
            )
            binding.txtFeedback.setTextColor(ContextCompat.getColor(this, R.color.faux))
        }
        binding.txtFeedback.visibility = View.VISIBLE
        binding.btnSuivant.isEnabled = true
    }

    /** Après rotation : le choix exact n'est pas sauvegardé, on indique seulement que la question est répondue. */
    private fun restaurerEtatApresReponse() {
        boutonsChoix.forEach { it.isEnabled = false }
        val question = questions[indexCourant]
        binding.txtFeedback.text = getString(
            R.string.reponse_enregistree_format,
            question.choix[question.indiceBonneReponse]
        )
        binding.txtFeedback.setTextColor(ContextCompat.getColor(this, R.color.texte))
        binding.txtFeedback.visibility = View.VISIBLE
        binding.btnSuivant.isEnabled = true
        mettreAJourBandeau()
    }

    /** Met à jour le score affiché et la progression (= nombre de réponses données). */
    private fun mettreAJourBandeau() {
        binding.txtScore.text = getString(R.string.score_format, score, questions.size)
        binding.progressQuiz.max = questions.size
        binding.progressQuiz.progress = nombreReponsesDonnees()
    }

    /** Réponses données : questions précédentes + la courante si elle a reçu une réponse. */
    private fun nombreReponsesDonnees(): Int =
        if (partieTerminee) questions.size else indexCourant + if (aRepondu) 1 else 0

    /** Passe à la question suivante, ou affiche le résultat après la dernière. */
    private fun questionSuivante() {
        if (!aRepondu) return // impossible d'avancer sans avoir répondu

        if (indexCourant >= questions.size - 1) {
            partieTerminee = true
            afficherResultat()
        } else {
            indexCourant++
            aRepondu = false
            afficherQuestion()
        }
    }

    /** Affiche l'écran final : les choix sont masqués, le score final est visible. */
    private fun afficherResultat() {
        binding.zoneJeu.visibility = View.GONE
        binding.zoneResultat.visibility = View.VISIBLE
        binding.txtResultat.text = getString(R.string.resultat_format, score, questions.size)
        mettreAJourBandeau()
    }

    /** Réinitialise le quiz : score, progression et question courante. */
    private fun rejouer() {
        indexCourant = 0
        score = 0
        aRepondu = false
        partieTerminee = false
        afficherQuestion()
    }

    private companion object {
        const val KEY_INDEX = "index"
        const val KEY_SCORE = "score"
        const val KEY_REPONDU = "repondu"
        const val KEY_TERMINEE = "terminee"
    }
}
