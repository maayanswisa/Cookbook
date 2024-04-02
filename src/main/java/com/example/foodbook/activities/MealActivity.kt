package com.example.foodbook.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodbook.MusicService
import com.example.foodbook.R
import com.example.foodbook.databinding.ActivityMealBinding
import com.example.foodbook.db.MealDatabase
import com.example.foodbook.pojo.Meal
import com.example.foodbook.viewModel.HomeViewModel
import com.example.foodbook.viewModel.MealViewModel

class MealActivity : AppCompatActivity() {

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String
    private lateinit var binding: ActivityMealBinding

    companion object {
        const val MEAL_ID = "com.example.meal_id"
        const val MEAL_NAME = "com.example.meal_name"
        const val MEAL_THUMB = "com.example.meal_thumb"
    }
    private lateinit var mealMvvm: MealViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mealMvvm = ViewModelProvider(this).get(MealViewModel::class.java)

        getMealInfoFromIntent()

        setInformationInViews()

        loadingCase()
        mealMvvm.getMealDetail(mealId)
        observerMealDetailsLiveData()

        onYoutubeImageClick()

        onFavoriteClick()


    }


    private fun onFavoriteClick() {
        binding.btnAddToFavorite.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, getString(R.string.meal_saved), Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener {
            val serviceIntent = Intent(this, MusicService::class.java)
            stopService(serviceIntent)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }


    private var mealToSave: Meal? = null
    private fun observerMealDetailsLiveData() {
        mealMvvm.observerMealDetailsLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(value: Meal) {
                onResponseCase()// if livedata changes we want to make all views visible and make progress bar invisible
                val meal = value
                mealToSave =
                    meal // this is the meal we need to save if client click on "like" button
                binding.tvCategory.text = "Category : ${meal!!.strCategory}"
                binding.tvArea.text = "Area : ${meal.strArea}"
                binding.tvInstructionsSteps.text = meal.strInstructions
                youtubeLink = meal.strYoutube.toString()
            }
        })
    }

    private fun setInformationInViews() {
        //set image
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        //set title
        binding.collapsingToolbar.title = mealName

        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))

    }

    //get Meal Info From Intent(from home frag)
    private fun getMealInfoFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(MealActivity.MEAL_ID) ?: "DefaultID"
        mealName = intent.getStringExtra(MealActivity.MEAL_NAME) ?: "DefaultName"
        mealThumb = intent.getStringExtra(MealActivity.MEAL_THUMB) ?: "DefaultThumb"
        Log.d(TAG, "Received - Meal ID: $mealId, Meal Name: $mealName, Meal Thumbnail: $mealThumb")

    }

    //we want to see the just the progress bar in this case
    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddToFavorite.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE

    }

    // if we get response we want to see everything except progress bar
    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddToFavorite.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        // Intent to start or resume the music service
        val musicServiceIntent = Intent(this, MusicService::class.java)
        startService(musicServiceIntent)


    }

}



