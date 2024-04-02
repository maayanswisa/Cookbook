package com.example.foodbook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodbook.databinding.CategoryItemBinding
import com.example.foodbook.pojo.Category


class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {
    private var categoryList: List<Category> = ArrayList<Category>()

    var onItemClick: ((Category) -> Unit)? = null

    // Updates the category list and notifies the adapter.
    fun setCategoryList(categoryList: List<Category>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }

    // ViewHolder to hold view references.
    class CategoryViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Creates a ViewHolder for an item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }
    // Binds data to the ViewHolder's views.
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categoryList[position].strCategoryThumb)
            .into(holder.binding.imgCategory) //update image

        holder.binding.tvCategoryName.text = categoryList[position].strCategory // update category name

        //when click on category it's open all the meal that belongs to this category
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(categoryList[position])
        }

    }

    // Returns the size of the category list.
    override fun getItemCount(): Int {
        return categoryList.size
    }


}