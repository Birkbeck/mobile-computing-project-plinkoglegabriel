package co.uk.bbk.lab6

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import co.uk.bbk.ladlelibrary.MainActivity
import co.uk.bbk.ladlelibrary.databinding.FragmentAddRecipeBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import co.uk.bbk.ladlelibrary.Category
import co.uk.bbk.ladlelibrary.MainViewModel
import kotlinx.coroutines.launch

class AddRecipeFragment : Fragment() {
    private lateinit var binding: FragmentAddRecipeBinding
    private val viewModel: MainViewModel by activityViewModels()
    private var image: String = ""
    val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            requireContext().contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            image = it.toString()
            binding.photoUploadButtonAdd.setImageURI(it)
        }
    }

    private var category: Category = Category.Other

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRecipeBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setTitle("Add New Recipe")
        // make sure the fields are empty when the fragment is created
        viewModel.title.value = ""
        viewModel.shortDescription.value = ""
        viewModel.ingredients.value = ""
        viewModel.instructions.value = ""
        viewModel.category.value = ""

        // Handling options for the category spinner
        val categories = listOf("Please select a category") + Category.entries.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryInputAdd.adapter = adapter

        binding.categoryInputAdd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                category = if (position == 0) Category.Other else Category.valueOf(categories[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                category = Category.Other
            }
        }

        binding.saveButtonAdd.setOnClickListener {
            lifecycleScope.launch {
                val title = binding.titleInputAdd.text.toString()

                // validating that the title and category is not empty
                if (title.isEmpty()) {
                    binding.titleInputLayoutAdd.error = "Please enter a title"
                    return@launch
                } else {
                    binding.titleInputLayoutAdd.error = null
                }

                if (viewModel.uniqueTitleCheck(title)) {
                    binding.titleInputLayoutAdd.error =
                        "Title already exists. Please choose another."
                    return@launch
                } else {
                    binding.titleInputLayoutAdd.error = null
                }

                if (binding.categoryInputAdd.selectedItemPosition == 0) {
                    binding.categoryErrorText.visibility = View.VISIBLE
                    binding.categoryErrorText.text = "Please select a category"
                    return@launch
                } else {
                    binding.categoryErrorText.visibility = View.GONE
                }

                val shortDescription = binding.descriptionInputAdd.text.toString()
                val ingredients = binding.ingredientsInputAdd.text.toString()
                val instructions = binding.instructionsInputAdd.text.toString()

                viewModel.addRecipe(
                    title = title,
                    image = image,
                    shortDescription = shortDescription,
                    ingredients = ingredients,
                    instructions = instructions,
                    category = category.name
                )
                findNavController().popBackStack()
            }
        }

        binding.photoUploadButtonAdd.setOnClickListener {
            Log.i("BBK-LOG", "Photo upload button clicked in AddRecipeFragment")
            imagePicker.launch("image/*")
        }

        // Cancel button to go back to the previous fragment (debated whether to include given the back button available in the toolbar)
        binding.cancelButtonAdd.setOnClickListener {
            Log.i("BBK-LOG", "Cancel button clicked in AddRecipeFragment")
            findNavController().popBackStack()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = AddRecipeFragment()
    }
}