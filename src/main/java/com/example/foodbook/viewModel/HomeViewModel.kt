package com.example.foodbook.viewModel


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architectureprojects.data.model.repository.ItemRepository
import com.example.foodbook.db.MealDatabase
import com.example.foodbook.location.LocationUpdatesLiveData
import com.example.foodbook.pojo.Category
import com.example.foodbook.pojo.CategoryList
import com.example.foodbook.pojo.Meal
import com.example.foodbook.pojo.MealList
import com.example.foodbook.pojo.MealsByCategory
import com.example.foodbook.pojo.MealsByCategoryList
import com.example.foodbook.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
//this view model is shared by all fragment in mainActivity

//class HomeViewModel(private val mealDatabase: MealDatabase) : ViewModel() {
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ItemRepository(application) //create repository instance

    private var randomMealLiveData = MutableLiveData<Meal>()

    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()

    private var categoriesLiveData = MutableLiveData<List<Category>>()

    private var favoriteMealsLiveData = repository.getAllItem() //liveData list of favorite meal to present in FavoritePage(fragment)

    private var bottomSheetMealLiveData = MutableLiveData<Meal>()

    private var searchdMealLiveData = MutableLiveData<List<Meal>>()

    private val newMealLiveData = MutableLiveData<Meal>()

    val address : LiveData<String> = LocationUpdatesLiveData(application.applicationContext)


    init{
        getRandomMeal()
    }

    // Fetches a random meal using Retrofit and updates LiveData for UI observation.
    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList> {

            // Handle successful API response.
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                response.body()?.let {
                    // Update LiveData with the meal from the response.
                    randomMealLiveData.value = it.meals[0] //we want just one meal
                }
            }

            // Handle API call failure
            override fun onFailure(call: Call<MealList>, t: Throwable) {
                // Log the error for debugging
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }


    fun getPopularItems() {
        RetrofitInstance.api.getPopularItems("Seafood")
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    if (response.body() != null) {
                        popularItemsLiveData.value = response.body()!!.meals////we want list of meals
                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.d("HomeFragment", t.message.toString())
                }

            })
    }


    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }



    fun getMealById(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let{
                    bottomSheetMealLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeViewModel", t.message.toString())
            }

        })
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            repository.deleteItem(meal)
        }
    }

    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            repository.addItem(meal)
        }
    }


    fun searchdMeals(searchQuery: String) = RetrofitInstance.api.searchMeal(searchQuery).enqueue(object :Callback<MealList>{
        override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
            val mealsList = response.body()?.meals
            mealsList?.let{
                searchdMealLiveData.postValue(it)
            }
        }

        override fun onFailure(call: Call<MealList>, t: Throwable) {
            Log.e("HomeViewModel", t.message.toString())
        }

    })

    fun observeSearchedMealLiveData():LiveData<List<Meal>> = searchdMealLiveData

// Exposes randomMealLiveData as LiveData (return value is unmodifiable from outside), enabling UI to react to data changes.
    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }


    fun observePopularItemsLiveData(): LiveData<List<MealsByCategory>> {
        return popularItemsLiveData
    }


    fun observeCategoriesLiveData(): LiveData<List<Category>>? {
        return categoriesLiveData
    }

    fun observeFavoriteMealsLiveData(): LiveData<List<Meal>>? {
        return favoriteMealsLiveData
    }

    fun observeBottomSheetMealLiveData():LiveData<Meal> = bottomSheetMealLiveData


    fun observeNewMealLiveData(): LiveData<Meal> = newMealLiveData

    fun setNewMeal(meal: Meal) {
        viewModelScope.launch {
            repository.addItem(meal)
        }
    }



}