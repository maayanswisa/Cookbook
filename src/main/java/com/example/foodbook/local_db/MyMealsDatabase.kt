package com.example.foodbook.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodbook.pojo.MyMeals

@Database(entities = arrayOf(MyMeals::class), version = 1, exportSchema = false)
@TypeConverters(MyMealsConverter::class)
abstract class MyMealsDatabase : RoomDatabase() {

    abstract fun myMealsDao():MyMealDao

    companion object{

        @Volatile
        private var instance:MyMealsDatabase? = null

        fun getDatabase(context: Context)= instance?: synchronized(this){
            Room.databaseBuilder(context.applicationContext, MyMealsDatabase::class.java,"my_meals_db")
                .allowMainThreadQueries().build()
        }
    }
}