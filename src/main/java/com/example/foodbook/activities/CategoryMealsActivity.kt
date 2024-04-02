package com.example.foodbook.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodbook.R
import com.example.foodbook.adapters.CategoryMealsAdapter
import com.example.foodbook.databinding.ActivityCategoryMealsBinding
import com.example.foodbook.databinding.ActivityMealBinding
import com.example.foodbook.fragments.HomeFragment
import com.example.foodbook.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {


    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter

    companion object {
        const val MEAL_ID = "com.example.foodbook.idMeal"
        const val MEAL_NAME = "com.example.foodbook.MealName"
        const val MEAL_THUMB = "com.example.foodbook.MealThumb"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]

        val categoryName = intent.getStringExtra(HomeFragment.CATEGORY_NAME)
        if (categoryName != null) {
            categoryMealsViewModel.getMealsByCategory(categoryName) //update livedata with current gategory list
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
        }


        onMealClicked()

        categoryMealsViewModel.observeMealsLiveData().observe(this) { mealList ->
            binding.tvCategoryCount.text = getString(R.string.count_of_meals)+mealList.size.toString() //update count of meals in current category
            categoryMealsAdapter.setMealsList(mealList) //Updates the adapter's meal list with the latest data from LiveData (mealList) for display in the RecyclerView.
        }
    }

    private fun onMealClicked() {
        categoryMealsAdapter.OnMealClicked = { item ->
            var intent = Intent(this@CategoryMealsActivity, MealActivity::class.java)
            intent.putExtra(MealActivity.MEAL_ID, item.idMeal)
            intent.putExtra(MealActivity.MEAL_NAME, item.strMeal)
            intent.putExtra(MealActivity.MEAL_THUMB, item.strMealThumb)

            startActivity(intent)
        }
    }
    //Initializes the RecyclerView with a GridLayoutManager to display items in  2 columns , and sets the adapter to manage the data and views.
    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }
}