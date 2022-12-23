package com.example.grademp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * @author Reima N.
 * @since 25.2.2022
 *
 * The only activity in the app, which serves as a host for all the fragments.
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}