package com.coolapps.goonerpi.notes.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.coolapps.goonerpi.notes.R
import com.coolapps.goonerpi.notes.utilities.hideKeyboard
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(my_toolbar)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)


        navController.addOnNavigatedListener { _, destination ->
            if (destination.id == R.id.NotesListFragment)
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            else {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_sharp_arrow_back_24px)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.NotesListFragment -> finish()
            else -> {
                hideKeyboard()
                navController.popBackStack()
            }
        }
    }

}
