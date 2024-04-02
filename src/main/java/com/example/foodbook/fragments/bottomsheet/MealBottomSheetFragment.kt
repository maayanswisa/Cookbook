package com.example.foodbook.fragments.bottomsheet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.foodbook.R
import com.example.foodbook.activities.MainActivity
import com.example.foodbook.activities.MealActivity
import com.example.foodbook.databinding.FragmentMealBottomSheetBinding
import com.example.foodbook.fragments.HomeFragment
import com.example.foodbook.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding:FragmentMealBottomSheetBinding
    private val viewModel: HomeViewModel by viewModels()
    private var mealName: String? = null
    private var mealThumb: String? = null
    private var mealId: String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)

        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealBottomSheetBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // if mealId exist(not null) so we update livedata of buttonSheet with function getMealById(id)
        mealId?.let{
            viewModel.getMealById(it)
        }


        observeBottomSheetMeal()

        onBottomSheetDialogClick()


    }




    private fun onBottomSheetDialogClick() {
        binding.bottomSheet.setOnClickListener{

            if(mealName != null && mealThumb != null){
                val intent = Intent(activity,MealActivity::class.java)
                intent.apply {
                    putExtra(MealActivity.MEAL_ID,mealId)
                    putExtra(MealActivity.MEAL_NAME,mealName)
                    putExtra(MealActivity.MEAL_THUMB,mealThumb)
                }
                startActivity(intent)

            }


        }
    }

    private fun observeBottomSheetMeal() {
        viewModel.observeBottomSheetMealLiveData().observe(viewLifecycleOwner, Observer { meal->
            Glide.with(this).load(meal.strMealThumb).into(binding.imgBottomSheet)
            binding.tvBottomSheetArea.text = meal.strArea
            binding.tvBottomSheetCategory.text = meal.strCategory
            binding.tvBottomSheetMealName.text = meal.strMeal

            mealName=meal.strMeal
            mealThumb=meal.strMealThumb

        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }
}