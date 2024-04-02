package com.example.foodbook.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.foodbook.local_db.MyMealsRepository
import com.example.foodbook.pojo.MyMeals

class MyMealsViewModel(application: Application):AndroidViewModel(application) {

    private val repository = MyMealsRepository(application)
    val meals:LiveData<List<MyMeals>>?=repository.getMeals()

    fun addMeal(meal: MyMeals){
        repository.addMeal(meal)
    }

    fun deleteMeal(meal: MyMeals){
        repository.deleteMeal(meal)
    }
}