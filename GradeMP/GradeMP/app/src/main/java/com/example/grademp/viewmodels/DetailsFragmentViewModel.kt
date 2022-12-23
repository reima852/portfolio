package com.example.grademp.viewmodels

import android.app.Application
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.grademp.R
import com.example.grademp.repositories.MPRepo
import com.example.grademp.room.getMPDatabase

/**
 * @author Reima N.
 * @since 28.2.2022
 *
 * ViewModel for the details fragment.
 */

class DetailsFragmentViewModel (application: Application, private val mPID: Int): ViewModel(){

    //Reference to the repository
    private val repository = MPRepo(getMPDatabase(application))


    //LiveData for the current MP we want to display
    val currentMP = Transformations.map(repository.members) { it ->
        it.filter { it.personNum == mPID }[0]}

    //Image setter
    fun setImage(imageView: ImageView, linkEnd: String) {
        //Generating the full url
        val imgUrl = "https://avoindata.eduskunta.fi/$linkEnd"

        //Generate the URI
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.broken_image_v2))
            .into(imageView)

    }
}