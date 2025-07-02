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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import co.uk.bbk.ladlelibrary.Category
import co.uk.bbk.ladlelibrary.MainActivity
import co.uk.bbk.ladlelibrary.MainViewModel
import co.uk.bbk.ladlelibrary.R
import co.uk.bbk.ladlelibrary.RecipeItem
import co.uk.bbk.ladlelibrary.databinding.FragmentEditRecipeBinding
import kotlinx.coroutines.launch
import kotlin.getValue

// The EditRecipeFragment that allows users to edit an existing recipe
class EditRecipeFragment : Fragment() {
    // Binding to the layout for this fragment
    private lateinit var binding: FragmentEditRecipeBinding
    // Shared MainViewModel to access the data and business logic
    private val viewModel: MainViewModel by activityViewModels()
    // initialising the category variable to store the selected category chosen by the user
    private var category: Category = Category.Other
    // initialising and setting the image variable to an empty string and the imagePicker to later handle image selection
    private var image: String = ""
    val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            requireContext().contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            image = it.toString()
            Log.i("BBK-LOG", "Image selected: $image")
            // ensures the image's URI is saved as a string and set to the UI photo upload ImageButton
            binding.photoUploadButtonEdit.setImageURI(it)
        }
    }

    // onCreateView method from the Fragment class that inflates the layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // view is bound to the FragmentAddRecipeBinding class (fragment_edit_recipe.xml)
        binding = FragmentEditRecipeBinding.inflate(inflater, container, false)
        // binding the viewModel to the layout and setting the lifecycle owner to this fragment
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    // onViewCreated method from the Fragment class that sets up the UI and handles user interactions
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setting the title of the activity to "Edit Recipe" (for toolbar)
        (activity as MainActivity).setTitle("Edit Recipe")

        // arguments retrieved from the Bundle passed to this fragment by the recipe list or view recipe fragment (the previous fragment)
        // aguments used to populate the ui with the existing recipe details
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

        // Handling options for the category spinner
        val categories = Category.entries.map { it.name }
        // ArrayAdapter allows the spinner to be populated with the previously defined list of categories
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // attaching the adapter to the spinner in the layout
        binding.categoryInputEdit.adapter = adapter

        // setting the category selected by the user as the category variable
        val currentCategory = Category.valueOf(categoryString)
        category = currentCategory
        val index = categories.indexOf(currentCategory.name)
        if (index != -1) {
            binding.categoryInputEdit.setSelection(index)
        }

        // setting the onItemSelectedListener for the spinner to update the category variable when a new category is selected by the user (when editing)
        binding.categoryInputEdit.onItemSelectedListener =
            // using an anonymous object (avoid class creation as wont be resued) to implement the AdapterView.OnItemSelectedListener interface
            object : AdapterView.OnItemSelectedListener {
                // onItemSelected method that is called when an item is selected (the category is assigned to the the value of the position of the selected category)
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    category = Category.valueOf(categories[position])
                    Log.i("BBK-LOG", "Category selected: ${category.name}")
                }
                // onNothingSelected method for when no item is selected (category is assigned to "Other")
                override fun onNothingSelected(parent: AdapterView<*>) {
                    category = Category.Other
                    Log.i("BBK-LOG", "No category selected so category set to default Other")
                }
            }
        // setting the click listener for the save button, handling the saving of the edited recipe
        binding.saveButtonEdit.setOnClickListener {
            // launching a coroutine (implemented to handle uniqueTitleCheck() call, allowing it to return immediately if not unique)
            lifecycleScope.launch {
                val editedTitle = binding.titleInputEdit.text.toString()

                // validating that the title and category is not empty
                if (editedTitle.isEmpty()) {
                    binding.titleInputLayoutEdit.error = "Please enter a title"
                    return@launch
                } else {
                    binding.titleInputLayoutEdit.error = null
                }
                // checking if the title is unique using the uniqueTitleCheck method from the viewModel
                // if it is not unique, an error message is displayed and the coroutine is returned (preventing saving)
                if (viewModel.uniqueTitleCheck(editedTitle, id) && editedTitle != viewModel.viewingRecipe.value?.title) {
                    binding.titleInputLayoutEdit.error = "Title already exists. Please choose another."
                    return@launch
                } else {
                    binding.titleInputLayoutEdit.error = null
                }
            // other recipe attributes are retrieved from the input fields (in case they have been edited)
            val editedShortDescription = binding.descriptionInputEdit.text.toString()
            val editedIngredients = binding.ingredientsInputEdit.text.toString()
            val editedInstructions = binding.instructionsInputEdit.text.toString()

            // editRecipe val assigned to a new RecipeItem object with the edited values
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
            // calling the editRecipe function from the viewModel to save the edited recipe
            viewModel.editRecipe(editedRecipe)
            findNavController().popBackStack()
             Log.i("BBK-LOG", "Recipe edited and saved and user directed back to previous fragment")
        }
    }
        // setting the click listener for the uploading photo button (image picker is launched)
        binding.photoUploadButtonEdit.setOnClickListener {
            imagePicker.launch("image/*")
            Log.i("BBK-LOG", "Photo upload button clicked and image picker launched")
        }
    }

    // companion object to create a new instance of the EditRecipeFragment with the required arguments (from the recipe list or view recipe fragment)
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