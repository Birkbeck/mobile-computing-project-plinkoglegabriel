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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import co.uk.bbk.ladlelibrary.MainActivity
import co.uk.bbk.ladlelibrary.databinding.FragmentAddRecipeBinding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import co.uk.bbk.ladlelibrary.Category
import co.uk.bbk.ladlelibrary.MainViewModel
import kotlinx.coroutines.launch


// The AddRecipeFragment that allows users to add a new recipe
class AddRecipeFragment : Fragment() {
    // Binding to the layout for this fragment
    private lateinit var binding: FragmentAddRecipeBinding
    // Shared MainViewModel to access the data and business logic
    private val viewModel: MainViewModel by activityViewModels()

    // initialising and setting the image variable to an empty string and the imagePicker to later handle image selection
    private var image: String = ""
    val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // using the URI class to request and persist read permission (app will retain access to the image even after the app is closed)
        uri?.let {
            requireContext().contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            // ensures the image's URI is saved as a string and set to the UI photo upload ImageButton
            image = it.toString()
            Log.i("BBK-LOG", "Image selected: $image")
            binding.photoUploadButtonAdd.setImageURI(it)
        }
    }

    // initialising the category variable to store the selected category chosen by the user
    private var category: Category = Category.Other

    // onCreateView method from the Fragment class that inflates the layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // view is bound to the FragmentAddRecipeBinding class (fragment_add_recipe.xml)
        binding = FragmentAddRecipeBinding.inflate(inflater)
        // binding the viewModel to the layout and setting the lifecycle owner to this fragment
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
    // onViewCreated method from the Fragment class that is called after the view has been created and contains how the add recipe's UI should be set up
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setting the title of the activity to "Add New Recipe" (appear in toolbar as such)
        (activity as MainActivity).setTitle("Add New Recipe")
        // make sure the fields are empty when the fragment is created
        viewModel.title.value = ""
        viewModel.shortDescription.value = ""
        viewModel.ingredients.value = ""
        viewModel.instructions.value = ""
        viewModel.category.value = ""

        // Handling options for the category spinner
        val categories = listOf("Please select a category") + Category.entries.map { it.name }
        // ArrayAdapter allows the spinner to be populated with the previously defined list of categories
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // attaching the adapter to the spinner in the layout
        binding.categoryInputAdd.adapter = adapter

        // setting the category selected by the user as the category variable
        binding.categoryInputAdd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                category = if (position == 0) Category.Other else Category.valueOf(categories[position])
            }
            // set to "Other" in the default case when no category is selected (although this is blocked by later validation)
            override fun onNothingSelected(parent: AdapterView<*>) {
                category = Category.Other
            }
        }

        // Setting up the click listeners for the save button
        binding.saveButtonAdd.setOnClickListener {
            // launching a coroutine to handle the saving of the recipe
            lifecycleScope.launch {
                Log.i("BBK-LOG", "Save button clicked in AddRecipeFragment")
                val title = binding.titleInputAdd.text.toString()

                // validating that the title and category is not empty
                if (title.isEmpty()) {
                    binding.titleInputLayoutAdd.error = "Please enter a title"
                    return@launch
                } else {
                    binding.titleInputLayoutAdd.error = null
                }
                // checking if the title is unique using the uniqueTitleCheck method from the viewModel
                // if it is not unique, an error message is displayed and the coroutine is returned (preventing saving)
                if (viewModel.uniqueTitleCheck(title, null)) {
                    binding.titleInputLayoutAdd.error = "Title already exists. Please choose another."
                    return@launch
                } else {
                    binding.titleInputLayoutAdd.error = null
                }

                // checking if category is selected and if not, an error message is displayed and the coroutine is returned (preventing saving)
                if (binding.categoryInputAdd.selectedItemPosition == 0) {
                    binding.categoryErrorText.visibility = View.VISIBLE
                    binding.categoryErrorText.text = "Please select a category"
                    return@launch
                } else {
                    binding.categoryErrorText.visibility = View.GONE
                }

                // other recipe attributes are retrieved from the input fields
                val shortDescription = binding.descriptionInputAdd.text.toString()
                val ingredients = binding.ingredientsInputAdd.text.toString()
                val instructions = binding.instructionsInputAdd.text.toString()

                // addRecipe function from viewmodel is called to save the recipe with the provided details
                viewModel.addRecipe(
                    title = title,
                    image = image,
                    shortDescription = shortDescription,
                    ingredients = ingredients,
                    instructions = instructions,
                    category = category.name
                )
                Log.i("BBK-LOG", "Newly added recipe saved")
                // go back to the previous fragment after saving
                findNavController().popBackStack()
            }
        }

        // Setting up the click listener for the photo upload button (image picker launches to select an image)
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