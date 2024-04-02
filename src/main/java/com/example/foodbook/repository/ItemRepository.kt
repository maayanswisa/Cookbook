package com.example.architectureprojects.data.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update

import com.example.foodbook.db.MealDao
import com.example.foodbook.db.MealDatabase
import com.example.foodbook.pojo.Meal

class ItemRepository(application: Application) {

    private var itemDao: MealDao?

    init {
        val db  = MealDatabase.getInstance(application.applicationContext)
        itemDao = db?.mealDao()
    }

    fun getAllItem() = itemDao?.getAllMeals() //with livedata - no db  //live data is automaicly asychronic

    suspend fun addItem(item: Meal) {
        itemDao?.insertMeal(item)
    }

    suspend fun deleteItem(item: Meal) {
        itemDao?.deleteMeal(item)
    }

    suspend fun updateItem(item: Meal) {
        itemDao?.updateMeal(item)
    }




}

//suspend fun insertMeal(meal: Meal)
//
//@Update
//suspend fun updateMeal(meal: Meal)
//
//@Delete
//suspend fun deleteMeal(meal: Meal)
//
//@Query("SELECT * FROM mealInformation")
//fun getAllMeals(): LiveData<List<Meal>>
