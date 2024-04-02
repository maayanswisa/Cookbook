package com.example.foodbook.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbook.R
import com.example.foodbook.activities.CategoryMealsActivity
import com.example.foodbook.activities.MainActivity
import com.example.foodbook.activities.MealActivity
import com.example.foodbook.adapters.CategoryMealsAdapter
import com.example.foodbook.adapters.MealsAdapter
import com.example.foodbook.databinding.FragmentFavoritesBinding
import com.example.foodbook.fragments.bottomsheet.MealBottomSheetFragment
import com.example.foodbook.pojo.MealsByCategory
import com.example.foodbook.viewModel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavoritesFragment :Fragment(){

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoritesAdapter: MealsAdapter
    private var FavoritesItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private val viewModel: HomeViewModel by viewModels()


    companion object {
        const val MEAL_ID = "com.example.foodbook.fragments.idMeal"
        const val MEAL_NAME = "com.example.foodbook.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.foodbook.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.foodbook.fragments.categoryName"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPopularItems()//what is this ?

        prepareRecyclerView()
        observeFavorite()
        OnMealClicked()


        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            //swipe to delete meal from favorite page.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition //determine position of item need to be deleted
                val deletedMeal = favoritesAdapter.differ.currentList[position]

                viewModel.deleteMeal(deletedMeal)

                Snackbar.make(requireView(), getString(R.string.meal_deleted), Snackbar.LENGTH_LONG).setAction(
                    getString(R.string.undo),
                    View.OnClickListener {
                        // Use a coroutine to insert the meal back
                        viewModel.insertMeal(deletedMeal)
                    }
                ).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)






    }

    private fun OnMealClicked() {
            favoritesAdapter.onMealClicked = { meal ->
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(MealActivity.MEAL_ID, meal.idMeal)
                intent.putExtra(MealActivity.MEAL_NAME, meal.strMeal)
                intent.putExtra(MealActivity.MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            }
        }


    private fun prepareRecyclerView() {
        favoritesAdapter = MealsAdapter()
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favoritesAdapter
        }
    }



              /*
                .apply {
                OnMealClicked = { item ->
                    val intent = Intent(activity, MealActivity::class.java).apply {
                        putExtra(CategoryMealsActivity.MEAL_ID, item.idMeal)
                        putExtra(CategoryMealsActivity.MEAL_NAME, item.strMeal)
                        putExtra(CategoryMealsActivity.MEAL_THUMB, item.strMealThumb)
                    }
                    startActivity(intent)
                }
            }
        }
    }
    */

    private fun observeFavorite() { //requireActivity()
        viewModel.observeFavoriteMealsLiveData()?.observe(viewLifecycleOwner, Observer { meals->
            favoritesAdapter.differ.submitList(meals)
        })
    }






}
