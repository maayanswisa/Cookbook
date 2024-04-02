package com.example.foodbook.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodbook.R
import com.example.foodbook.databinding.FragmentAddNewMealBinding
import com.example.foodbook.pojo.MyMeals
import com.example.foodbook.viewModel.MyMealsViewModel

class AddNewMealFragment : Fragment() {

    private lateinit var binding: FragmentAddNewMealBinding

    private var imageUri: Uri? = null

    private val viewModelMyMeal: MyMealsViewModel by activityViewModels()


    val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            binding.resultImage.setImageURI(it)
            if (it != null) {
                requireActivity().contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            imageUri = it
        }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewMealBinding.inflate(inflater)
        //setupAddMealImageButton()


        binding.btnAdd.setOnClickListener {
            val name=binding.edName.text.toString()
            val inst=binding.edInstructions.text.toString()
            if (name!="" && inst!="" && imageUri!=null){
                val meal = MyMeals(
                    binding.edName.text.toString(),
                    binding.edInstructions.text.toString(),
                    imageUri
                )
                viewModelMyMeal.addMeal(meal)
                findNavController().navigate(R.id.action_addNewMealFragment_to_myMealsFragment)

            }
            else{
                val  builder : android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
                builder.apply {
                    setTitle(getString(R.string.empty_fields))
                    setMessage(getString(R.string.empty_field_content))
                    setCancelable(true)
                    val dialog = builder.create()
                    dialog.show()
                }
            }

            //MealManager.add(meal)



        }

        binding.btnAddMealImg.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}





