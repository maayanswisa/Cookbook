package com.example.foodbook.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.foodbook.pojo.Meal
import com.example.foodbook.pojo.MyMeals

@Dao
interface MyMealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //if meal it's already exist in db
    fun addMeal (meal:MyMeals)

    @Update
    fun updateMeal(meal: MyMeals)

    @Delete
    fun deleteMeal(vararg meal: MyMeals)

    @Query("SELECT * FROM MyMeals")
    fun getMeals():LiveData<List<MyMeals>>
}