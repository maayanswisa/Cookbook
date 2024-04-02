package com.example.foodbook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodbook.databinding.PopularItemsBinding
import com.example.foodbook.pojo.MealsByCategory

// Define a class MostPopularAdapter that extends RecyclerView.Adapter with a custom ViewHolder PopularMealViewHolder.
class MostPopularAdapter(): RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {

    // Declare a lateinit variable onItemClick of function type that takes MealsByCategory and returns Unit (void).
    lateinit var onItemClick: ((MealsByCategory) -> Unit)
    // Declare a nullable variable onLongItemClick of function type that takes MealsByCategory and returns Unit, initialized to null.
    var onLongItemClick: ((MealsByCategory) -> Unit)? = null
    // Initialize a variable mealsList as an ArrayList of MealsByCategory to hold the data set.
    private var mealsList = ArrayList<MealsByCategory>()

    // Define a function to update the adapter's data set and notify any registered observers that the data set has changed.
    fun setMeals(mealsList: ArrayList<MealsByCategory>) {
        this.mealsList = mealsList // Update the mealsList with the new data.
        notifyDataSetChanged() // Notify any registered observers that the data has changed.
    }

    // Define the ViewHolder class PopularMealViewHolder that holds the views for each item in the list.
    class PopularMealViewHolder(val binding: PopularItemsBinding): RecyclerView.ViewHolder(binding.root)

    //onCreateViewHolder method inflate the layout for an item and create a new ViewHolder.
    // This method is not called for every item in the dataset. Instead, it is called when the RecyclerView needs a new ViewHolder
// to display an item with. This typically happens when initializing the RecyclerView or when new item views
// need to be created as the user scrolls through the list. The method is optimized by RecyclerView to minimize calls by recycling ViewHolders.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        // Inflate the layout for an item, create a PopularMealViewHolder, and return it.
        return PopularMealViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    //  getItemCount method provides the number of items present in the data set.
    override fun getItemCount(): Int {
        return mealsList.size // Return the size of the mealsList.
    }

    //onBindViewHolder method  bind data to each ViewHolder.
    // This method is called for every item in the RecyclerView as it is being displayed or scrolled.
    // It binds the meal's  image from the mealsList to the ImageView within the ViewHolder.
    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        // Use Glide to load an image from the internet into an ImageView within the ViewHolder.
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb) // Load the image from the URL
            .into(holder.binding.imgPopularMealItem) // Set the image into the ImageView.

        // Set an OnClickListener to the itemView that invokes onItemClick with the item at the current position when clicked.
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(mealsList[position])
        }

        // Set an OnLongClickListener to the itemView that invokes onLongItemClick with the item at the current position when long-clicked.
        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(mealsList[position])
            true // Return true to indicate that the callback consumed the long click.
        }
    }
}
