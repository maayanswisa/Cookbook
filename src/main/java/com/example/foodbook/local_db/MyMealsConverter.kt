package com.example.foodbook.local_db


import android.net.Uri
import androidx.room.TypeConverter

class MyMealsConverter {
    @TypeConverter
    fun fromString(value: String?): Uri? {
        return value?.let { Uri.parse(it) }
    }

    @TypeConverter
    fun uriToString(uri: Uri?): String? {
        return uri?.toString()
    }
}