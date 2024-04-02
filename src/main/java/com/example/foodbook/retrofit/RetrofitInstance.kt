package com.example.foodbook.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
// Defines a singleton RetrofitInstance to manage API communication through MealApi.

object RetrofitInstance {
    // Lazily initialized Retrofit client for TheMealDB API with Gson converter.

    val api:MealApi by lazy {
        Retrofit.Builder().baseUrl( "https://www.themealdb.com/api/json/v1/1/")// Base URL for the API.
            .addConverterFactory(GsonConverterFactory.create())// Automatic JSON parsing.
            .build()
            .create(MealApi::class.java)// Create the MealApi interface implementation
    }
}