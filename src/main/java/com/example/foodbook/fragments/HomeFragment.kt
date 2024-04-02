package com.example.foodbook.fragments


import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodbook.FileHelper
import com.example.foodbook.FileHelper.readAddressFromFile
import com.example.foodbook.R
import com.example.foodbook.activities.CategoryMealsActivity
import com.example.foodbook.activities.MainActivity
import com.example.foodbook.activities.MealActivity
import com.example.foodbook.adapters.CategoriesAdapter
import com.example.foodbook.adapters.MostPopularAdapter
import com.example.foodbook.databinding.FragmentHomeBinding
import com.example.foodbook.fragments.bottomsheet.MealBottomSheetFragment
import com.example.foodbook.pojo.Meal
import com.example.foodbook.pojo.MealsByCategory
import com.example.foodbook.viewModel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
onCreate: Initialize  fragment-level non-UI data structures or components.
onCreateView: Inflate and return your fragment's layout.
onViewCreated: Perform all setup that requires access to the initialized views.
*/
class HomeFragment : Fragment() {



    private lateinit var binding: FragmentHomeBinding
    //private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
//    private lateinit var myLocation: String
//    private lateinit var homeLocation: String
    private var myLocation: String? = null
    private var homeLocation: String? = null
    private var alreadyDisplayedMessage: Boolean = false


    private val viewModel: HomeViewModel by viewModels()

    //data to transfer in the intent from this activity to activity meal
    companion object {
        const val MEAL_ID = "com.example.foodbook.fragments.idMeal"
        const val MEAL_NAME = "com.example.foodbook.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.foodbook.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.foodbook.fragments.categoryName"
        private const val TAG = "com.example.foodbook.fragments.MainActivity"

    }


// Initializes a launcher for permission requests with a callback to trigger location updates if permission is granted.

    private val locationRequestLauncher : ActivityResultLauncher<String>
            = registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        if(it) {
            getLocationUpdates()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ViewModel sharing lets fragments use the same ViewModel from their activity, ensuring consistent data across the app. Getting the ViewModel in `onCreate` aligns with the lifecycle, allowing early setup for data management. This early initialization means everything's ready for when the UI is built, streamlining setup and data binding.
        //viewModel= (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //read user address from file(if exist)
        readUserAddress()

        // Checks if the app has ACCESS_FINE_LOCATION permission. If granted, starts location updates;
        // otherwise, requests the permission through locationRequestLauncher.
        if(ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            getLocationUpdates()
        }
        else {
            locationRequestLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }


        preparePopularItemsRecyclerView()

        //random meal
        viewModel.getRandomMeal() // Fetches a random meal and updates LiveData for UI observation.

        observerRandomMeal() // Sets up observation on the random meal LiveData. Updates UI with new meal image using Glide and updates the current meal data.

        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()


        prepareCategoriesRecyclerView()

        //viewModel.getCategories()
        observeCategoriesLiveData()

        onCategoryClick()

        onPopularItemLongClick()

        onSearchIconClick()

        binding.setHomeLocation.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.set_home_message), Toast.LENGTH_LONG).show()
        }
        binding.setHomeLocation.setOnLongClickListener {
            homeLocation=myLocation
            // Perform the file save operation in a coroutine to avoid blocking the UI thread
            lifecycleScope.launch(Dispatchers.IO) {
                // Use 'requireContext()' to safely get the Context in a Fragment
                homeLocation?.let { it1 -> FileHelper.saveAddressToFile(requireContext(), it1) }

                // update UI - show a Toast message after saving, switch back to the Main thread
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(),
                        getString(R.string.location_saved_as_home), Toast.LENGTH_SHORT).show()
                }
            }
            true // Indicates the long click was consumed
        }


    }




    private fun readUserAddress() {
        // Launch a coroutine in the lifecycle scope of the Fragment
        lifecycleScope.launch {
            val address = readAddressFromFile(requireContext())
            // lifecycleScope.launch ensures this block runs on the main thread.
            homeLocation = address
        }
    }
    private fun getLocationUpdates() {

        viewModel.address.observe(viewLifecycleOwner) { locationString ->
            // Log the location data
            myLocation=locationString
            Log.d(ContentValues.TAG, "almogDebug: getLocationUpdates: $myLocation")

            homeLocation?.let { nonNullHomeLocation ->
                myLocation?.let { nonNullMyLocation ->
                    Log.d(ContentValues.TAG, "almogDebug both not null")

                    if (nonNullHomeLocation == nonNullMyLocation) {
                        Log.d(ContentValues.TAG, "almogDebug: even ")

                        //to display message once
                        if (!alreadyDisplayedMessage) {
                            //Toast.makeText(requireContext(), "you are at home make food!", Toast.LENGTH_SHORT).show()
                            Log.d(ContentValues.TAG, "almogDebug you are at home make food!")
                            openAlertDialog()
                            alreadyDisplayedMessage=true
                        }



                    }
                }
            }

        }
    }


    private fun openAlertDialog(){
        val  builder : AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle(getString(R.string.home_sweet_home1))
            setMessage(getString(R.string.recognize_home_location))
            setCancelable(true)
            setIcon(R.drawable.house)

            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    //opens button sheet with details
    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick={meal->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick={
            val intent = Intent(activity,CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, it.strCategory)
            startActivity(intent)

        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter =categoriesAdapter
        }
    }
// Watches for changes in category data and updates the adapter
// with new data when changes occur.
    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData()?.observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategoryList(categories)// Update adapter's category list.
        })
    }

    //on click, opens MealActivity (full detail of meal)
    private fun onPopularItemClick() {

        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MealActivity.MEAL_ID, meal.idMeal)
            intent.putExtra(MealActivity.MEAL_NAME, meal.strMeal)
            intent.putExtra(MealActivity.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }
// Sets up the popular items RecyclerView with a horizontal layout and assigns its adapter.

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() { //
        viewModel.observePopularItemsLiveData().observe(
            viewLifecycleOwner
        ) { mealList ->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            //  val temp = meal.meals[0]
            val intent = Intent(activity, MealActivity::class.java)//from this activity to MealActivity
            intent.putExtra(MealActivity.MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MealActivity.MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MealActivity.MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }
    //image + randomMeal updates whenever the data observer detects a change in live data.
    private fun observerRandomMeal() {
        // Observes random meal LiveData.
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner,
            Observer { meal ->
                Glide.with(this@HomeFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.imgRandomMeal)  //Updates UI with new meal image using Glide

                this.randomMeal = meal//updates current meal data.
            })
    }
}



