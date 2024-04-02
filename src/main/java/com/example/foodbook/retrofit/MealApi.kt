package com.example.foodbook.retrofit

import com.example.foodbook.pojo.CategoryList
import com.example.foodbook.pojo.MealsByCategoryList
import com.example.foodbook.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
//Interface for accessing meal data from the server.
//"Call" is a type defined by Retrofit that represents a single network request and response.
//Retrofit handles threading for asynchronous calls.
interface MealApi {
        /*
        getRandomMeal method - fetch a random meal via GET request to "random.php".
        returns a Call<MealList>
        */
    @GET("random.php")// "random.php" is from URL
    fun getRandomMeal(): Call<MealList>

    //The getMealDetails function takes a meal ID (id) as a parameter and includes it as a query parameter in the URL with the key "i". It returns a Call object wrapping a MealList response.
    @GET("lookup.php") //"lookup.php" is from URL
    fun getMealDetails(@Query("i") id: String): Call<MealList>

    @GET("filter.php")
    fun getPopularItems(@Query("c") categoryName: String): Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories() : Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String): Call<MealsByCategoryList>

    @GET("search.php")
    fun searchMeal(@Query("s") searchQuery: String): Call<MealList>


}