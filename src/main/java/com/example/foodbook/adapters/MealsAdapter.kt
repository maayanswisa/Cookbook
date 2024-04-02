package com.example.foodbook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodbook.databinding.MealItemBinding
import com.example.foodbook.pojo.Meal
import com.example.foodbook.pojo.MealsByCategory

class MealsAdapter : RecyclerView.Adapter<MealsAdapter.FavoriteMealsAdapterViewHolder>() {

    inner class FavoriteMealsAdapterViewHolder(val binding: MealItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
    //This property, differ, is an instance of AsyncListDiffer, a utility that helps manage the list data displayed in a RecyclerView by calculating differences between old and new data sets asynchronously.
    val differ = AsyncListDiffer(this, diffUtil)

    // Callback to handle item click
    private var onItemClickListener: ((Meal) -> Unit)? = null

    lateinit var onMealClicked:((Meal)-> Unit)

    fun setOnItemClickListener(listener: (Meal) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteMealsAdapterViewHolder {
        return FavoriteMealsAdapterViewHolder(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteMealsAdapterViewHolder, position: Int) {
        val meal = differ.currentList[position]

        // Bind data to the view
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = meal.strMeal

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(meal)
        }

        holder.itemView.setOnClickListener {
            onMealClicked?.invoke(meal)
        }





    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
