package co.uk.bbk.lab6


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.uk.bbk.ladlelibrary.Category
import co.uk.bbk.ladlelibrary.MainActivity
import co.uk.bbk.ladlelibrary.MainViewModel
import co.uk.bbk.ladlelibrary.R
import co.uk.bbk.ladlelibrary.RecipeItem
import co.uk.bbk.ladlelibrary.databinding.FragmentEditRecipeBinding
import kotlin.getValue

class EditRecipeFragment : Fragment() {
    private lateinit var binding: FragmentEditRecipeBinding
    private val viewModel: MainViewModel by activityViewModels()
    private var category: Category = Category.Other
    private var image: String = ""
    val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            requireContext().contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            image = it.toString()
            binding.photoUploadButtonEdit.setImageURI(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditRecipeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setTitle("Edit Recipe")

        val args = arguments
        val id = args?.getLong("id") ?: -1
        val categoryString = args?.getString("category") ?: "Other"
        viewModel.title.value = args?.getString("title")
        viewModel.image.value = args?.getString("image") ?: ""
        viewModel.shortDescription.value = args?.getString("description")
        viewModel.ingredients.value = args?.getString("ingredients")
        viewModel.instructions.value = args?.getString("instructions")
        viewModel.category.value = categoryString

        val uriString = args?.getString("image") ?: ""
        image = uriString
        // checking if the uriString is not empty before parsing to Uri to avoid crashing
        if (uriString.isNotBlank()) {
            val imageUri = Uri.parse(uriString)
            try {
                binding.photoUploadButtonEdit.setImageURI(imageUri)
            } catch (e: Exception) {
                Log.e("BBK-LOG", "Error: ${e.message}")
                binding.photoUploadButtonEdit.setImageResource(R.drawable.placeholder_photo)
            }
        }

        val categories = listOf("Please select a category") + Category.entries.map { it.name }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryInputEdit.adapter = adapter

        val currentCategory = Category.valueOf(categoryString)
        category = currentCategory

        val index = categories.indexOf(currentCategory.name)
        if (index != -1) {
            binding.categoryInputEdit.setSelection(index)
        }

        binding.categoryInputEdit.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected( parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    category = if (position == 0) Category.Other else Category.valueOf(categories[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    category = Category.Other
                }
        }

        binding.saveButtonEdit.setOnClickListener {
            val editedTitle = binding.titleInputEdit.text.toString()

            // validating that the title and category is not empty
            if (editedTitle.isEmpty()) {
                binding.titleInputLayoutEdit.error = "Please enter a title"
                return@setOnClickListener
            } else {
                binding.titleInputLayoutEdit.error = null
            }

            val editedShortDescription = binding.descriptionInputEdit.text.toString()
            val editedIngredients = binding.ingredientsInputEdit.text.toString()
            val editedInstructions = binding.instructionsInputEdit.text.toString()

            val editedRecipe = RecipeItem(
                    // image retrieval is a placeholder for now
                    id = id,
                    image = image,
                    title = editedTitle,
                    shortDescription = editedShortDescription,
                    ingredients = editedIngredients,
                    instructions = editedInstructions,
                    category = category.name
                )

            viewModel.editRecipe(editedRecipe)
            findNavController().popBackStack()
        }

        binding.photoUploadButtonEdit.setOnClickListener {
            imagePicker.launch("image/*")
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(
            id: Long,
            image: String,
            title: String,
            description: String,
            ingredients: String,
            instructions: String,
            category: String
        ) = EditRecipeFragment().apply {
            arguments = Bundle().apply {
                putLong("id", id)
                putString("image", image)
                putString("title", title)
                putString("description", description)
                putString("ingredients", ingredients)
                putString("instructions", instructions)
                putString("category", category)
            }
        }
    }
}