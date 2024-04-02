package com.example.foodbook.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodbook.pojo.Meal

// Defines a Room Database with Meal as the entity and sets the database version to 1.
@Database(entities = [Meal::class], version = 1)

// Specifies a TypeConverter to handle data types not natively supported by SQLite.
@TypeConverters(MealTypeConvertor::class)

// Declares an abstract RoomDatabase class for accessing Meal data.
abstract class MealDatabase : RoomDatabase() {

    // Abstract function to get an instance of MealDao for database operations.
    abstract fun mealDao(): MealDao

    // Companion object to enable singleton pattern for MealDatabase instance.
    companion object {
        // Marks the INSTANCE variable as volatile to ensure atomic access to the variable.
        @Volatile
        var INSTANCE: MealDatabase? = null

        // Synchronized method to get a singleton instance of MealDatabase, ensuring thread safety.
        @Synchronized
        fun getInstance(context: Context): MealDatabase {
            // Checks if INSTANCE is null, and if so, initializes it with the database builder.
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    MealDatabase::class.java, "meal.db")
                    // Handles migrations destructively by recreating the database instead of migrating if no migration object is found.
                    .fallbackToDestructiveMigration()
                    .build()
            }
            // Returns the singleton instance of MealDatabase.
            return INSTANCE as MealDatabase
        }
    }
}
