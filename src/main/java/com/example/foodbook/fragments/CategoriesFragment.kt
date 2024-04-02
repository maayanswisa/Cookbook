package com.example.foodbook.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodbook.activities.CategoryMealsActivity
import com.example.foodbook.activities.MainActivity
import com.example.foodbook.adapters.CategoriesAdapter
import com.example.foodbook.databinding.FragmentCategoriesBinding
import com.example.foodbook.viewModel.HomeViewModel


class CategoriesFragment :Fragment(){

    private lateinit var binding:FragmentCategoriesBinding

    private lateinit var categoriesAdapter: CategoriesAdapter

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        viewModel.getCategories()
        observeCategories()
        onCategoryClick()
    }

    private fun observeCategories() {
        viewModel.observeCategoriesLiveData()?.observe(viewLifecycleOwner, Observer { categories->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun prepareRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick={
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME, it.strCategory)
            startActivity(intent)

        }
    }
}
