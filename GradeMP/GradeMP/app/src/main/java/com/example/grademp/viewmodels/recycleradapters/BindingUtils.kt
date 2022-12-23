package com.example.grademp.viewmodels.recycleradapters

import android.graphics.Color
import android.widget.TextView
import com.example.grademp.room.DatabaseMemberOfFinnishParliament
import com.example.grademp.room.MPNote

/**
 * @author Reima N.
 * @since 28.2.2022
 *
 * All RecyclerViews' binding utilities.
 */

//For binding MP names and parties to the text view component of the MP-RecyclerView.
//Here we also set the color to display the "grade" of the MP.
fun bindNamesAndParties(
    textView: TextView, databaseMemberOfFinnishParliament: DatabaseMemberOfFinnishParliament) {

    //First get the status
    val currentOpinion = databaseMemberOfFinnishParliament.status

    //The color and grade decisions
    val grade: String

    when (currentOpinion) {
        //Light red color, bad grade
        -1 -> {
            textView.setBackgroundColor(Color.parseColor("#E78989"))
            grade = "Bad"
        }

        //Light green color
        1 -> {
            textView.setBackgroundColor(Color.parseColor("#9BD676"))
            grade = "Good"
        }

        //Light blue color
        else -> {
            textView.setBackgroundColor(Color.parseColor("#8DC3E9"))
            grade = "Neutral"
        }
    }
    //Finally set the texts to the textview
    databaseMemberOfFinnishParliament.apply {
        val textToSet = "$first $last, $party       -->$grade status"
        textView.text = textToSet
    }
}

//For binding short info to the note in the Note-RecyclerView's list
fun bindFullDateAndOpinion (textView: TextView, mpNote: MPNote) {
    mpNote.apply {
        val fullDate = "${day}/${month}/${year}"
        val opinionText = if(opinion) "positive" else "negative"
        val noteTextShort = if (note.length > 20) note.slice(0..20) + "..." else note
        val textToBind = "$fullDate    $opinionText     (${noteTextShort.trimEnd()})"
        textView.text = textToBind
        //Set the background of the list items to green if the opinion is positive, otherwise red
        textView.setBackgroundColor(if (opinion) {
            Color.parseColor("#9BD676")
        } else Color.parseColor("#E78989"))
    }
}
