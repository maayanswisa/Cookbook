package com.example.foodbook.pojo

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyMeals(
    val title: String,
    val description: String,
    val photo: Uri?
) {
    @PrimaryKey(autoGenerate = true)
    var id:Int =0
}

//object MealManager {
//    val meals: MutableList<MyMeals> = mutableListOf()
//
//    fun add(myMeals: MyMeals) {
//        meals.add(myMeals)
//    }
//
//    fun remove(index: Int) {
//        meals.removeAt(index)
//    }
//}