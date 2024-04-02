package com.example.foodbook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbook.R
import com.example.foodbook.activities.MainActivity
import com.example.foodbook.adapters.MyNewMealsAdapter
import com.example.foodbook.databinding.FragmentMyMealsBinding
import com.example.foodbook.viewModel.HomeViewModel
import com.example.foodbook.viewModel.MyMealsViewModel
import com.google.android.material.snackbar.Snackbar


class  MyMealsFragment : Fragment(){

    private lateinit var binding: FragmentMyMealsBinding
    private val viewModel: HomeViewModel by viewModels()

    private val viewModelMyMeal:MyMealsViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyMealsBinding.inflate(inflater)
        return  binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBtnAddMealClick()

        viewModelMyMeal.meals?.observe(viewLifecycleOwner){
            binding.rvMyMeals.adapter = MyNewMealsAdapter(it, object :MyNewMealsAdapter.MyNewMealListener{
                override fun onMealClicked(index: Int) {
                    Toast.makeText(requireContext(), getString(R.string.yummy),Toast.LENGTH_SHORT).show()
                }

                override fun onMealLongClicked(index: Int) {
                    //MealManager.remove(index)
                   // binding.rvMyMeals.adapter!!.notifyItemRemoved(index)
                }

            })
        }

        binding.rvMyMeals.layoutManager = LinearLayoutManager(requireContext())

        ItemTouchHelper(object :ItemTouchHelper.Callback(){

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            )= makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //MealManager.remove(viewHolder.adapterPosition)
                //binding.rvMyMeals.adapter!!.notifyItemRemoved(viewHolder.adapterPosition)
                val meal = (binding.rvMyMeals.adapter as MyNewMealsAdapter).mealAt(viewHolder.adapterPosition)
                viewModelMyMeal.deleteMeal(meal)
            }

        }).attachToRecyclerView(binding.rvMyMeals)


    }


    private fun onBtnAddMealClick() {
        binding.btnAddNewMeal.setOnClickListener{
            findNavController().navigate(R.id.action_myMealsFragment_to_addNewMealFragment)
        }
    }




}