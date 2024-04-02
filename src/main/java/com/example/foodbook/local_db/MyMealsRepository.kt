package com.example.foodbook.local_db

import android.app.Application
import com.example.foodbook.pojo.MyMeals

class MyMealsRepository(application: Application) {

    private var myMealsDao: MyMealDao?

    init {
        val db = MyMealsDatabase.getDatabase(application.applicationContext)
        myMealsDao = db?.myMealsDao()
    }

    fun getMeals()=myMealsDao?.getMeals()

    fun addMeal(meal:MyMeals){
        myMealsDao?.addMeal(meal)
    }

    fun deleteMeal(meal:MyMeals){
        myMealsDao?.deleteMeal(meal)
    }




}