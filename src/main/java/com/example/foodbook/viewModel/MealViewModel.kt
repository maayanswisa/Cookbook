package com.example.foodbook.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architectureprojects.data.model.repository.ItemRepository
import com.example.foodbook.pojo.Meal
import com.example.foodbook.pojo.MealList
import com.example.foodbook.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class  MealViewModel(application: Application): AndroidViewModel(application) {

    private val repository = ItemRepository(application) //create repository instance

    private var mealDetailsLiveData = MutableLiveData<Meal>()

    fun getMealDetail(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object :retrofit2.Callback<MealList>{


            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body()!=null){
                    mealDetailsLiveData.value = response.body()!!.meals[0]
                }else
                    return
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity", t.message.toString())
            }
        })
    }

    fun observerMealDetailsLiveData(): LiveData<Meal> {
        return mealDetailsLiveData
    }


    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            repository.addItem(meal)
        }
    }

    fun updateMeal(meal: Meal){
        viewModelScope.launch {
            repository.updateItem(meal)
        }
    }

    /*
   coroutine -
   By using viewModelScope.launch, both insertMeal and
   updateMeal execute asynchronously. This means the database operations are
   performed without freezing the UI, allowing for a smooth user experience.
   The coroutine launched by viewModelScope.launch ensures that these operations
   are performed in the appropriate execution context  and are automatically canceled if the ViewModel is destroyed,
   thus managing resources efficiently and preventing potential memory leaks.
   Coroutines launched in this scope execute on the main thread.

     */





}