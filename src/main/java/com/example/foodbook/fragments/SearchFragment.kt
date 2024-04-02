package com.example.foodbook.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodbook.activities.MainActivity
import com.example.foodbook.activities.MealActivity
import com.example.foodbook.adapters.CategoriesAdapter
import com.example.foodbook.adapters.MealsAdapter
import com.example.foodbook.databinding.FragmentSearchBinding
import com.example.foodbook.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchRecyclerViewAdapter: MealsAdapter
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()




        observeSearchMealLiveData()

        var searchJob: Job? = null
        binding.edSearchBox.addTextChangedListener{
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchdMeals(it.toString())
            }

        }
        OnMealClicked()
    }

    private fun OnMealClicked() {
        searchRecyclerViewAdapter.onMealClicked = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MealActivity.MEAL_ID, meal.idMeal)
            intent.putExtra(MealActivity.MEAL_NAME, meal.strMeal)
            intent.putExtra(MealActivity.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeSearchMealLiveData() {
        viewModel.observeSearchedMealLiveData().observe(viewLifecycleOwner, Observer { mealsList->
            searchRecyclerViewAdapter.differ.submitList(mealsList)
        })
    }

    private fun searchMeals() {
        val searchQuery = binding.edSearchBox.text.toString()
        if(searchQuery.isNotEmpty()){
            viewModel.searchdMeals(searchQuery)
        }
    }

    private fun prepareRecyclerView() {
        searchRecyclerViewAdapter = MealsAdapter()
        binding.rvSearchedMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = searchRecyclerViewAdapter
        }
    }
}