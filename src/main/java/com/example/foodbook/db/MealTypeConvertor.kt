package com.example.foodbook.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
// Specifies a TypeConverter to handle data types not natively supported by SQLite.

@TypeConverters
class MealTypeConvertor {

    @TypeConverter
    fun fromAnyToString(attribute:Any?) : String{
        if (attribute == null)
            return ""
        return attribute.toString()
    }


    @TypeConverter
    fun fromStringToAny(attribute:String?) : Any{
        if (attribute == null)
            return ""
        return attribute
    }


}