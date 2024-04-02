package com.example.foodbook.adapters

import android.content.DialogInterface
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodbook.databinding.NewMyMealLayoutBinding
import com.example.foodbook.pojo.Meal
import com.example.foodbook.pojo.MyMeals

class MyNewMealsAdapter(val meals:List<MyMeals>,val callBack:MyNewMealListener) :RecyclerView.Adapter<MyNewMealsAdapter.MyMealViewHolder>() {


    interface MyNewMealListener{
        fun onMealClicked(index:Int)
        fun onMealLongClicked(index:Int)
    }


    inner class MyMealViewHolder(private val binding:NewMyMealLayoutBinding)
        :RecyclerView.ViewHolder(binding.root),View.OnClickListener,View.OnLongClickListener{

        init{
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)

        }




        fun bind(meal: MyMeals){
            binding.txtName.text = meal.title
            binding.txtDesc.text = meal.description
            //binding.mealImage.setImageURI(Uri.parse(meal.photo.toString()))
            Glide.with(binding.root).load(meal.photo).into(binding.mealImage)
        }

        override fun onClick(v: View?) {
            callBack.onMealClicked(adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            callBack.onMealLongClicked(adapterPosition)
            return true

        }


    }

    fun mealAt(position: Int) = meals[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= MyMealViewHolder(
        NewMyMealLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun getItemCount()= meals.size


    override fun onBindViewHolder(holder: MyMealViewHolder, position: Int) {
        holder.bind((meals[position]))
    }
}