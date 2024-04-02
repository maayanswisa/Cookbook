package com.example.foodbook.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.foodbook.MusicService
import com.example.foodbook.R
import com.example.foodbook.viewModel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize the ViewModel
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // Start the music service when the main activity is created
        val serviceIntent = Intent(this, MusicService::class.java)
        startService(serviceIntent)



        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = Navigation.findNavController(this, R.id.frag_host)

        NavigationUI.setupWithNavController(bottomNavigation,navController)


    }


    override fun onDestroy() {
        super.onDestroy()
        // Stop the music service
        val serviceIntent = Intent(this, MusicService::class.java)
        stopService(serviceIntent)
    }



}