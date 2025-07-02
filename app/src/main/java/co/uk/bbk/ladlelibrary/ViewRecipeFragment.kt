package co.uk.bbk.ladlelibrary

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import co.uk.bbk.ladlelibrary.databinding.FragmentViewRecipeBinding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.uk.bbk.ladlelibrary.MainViewModel

// ViewRecipeFragment that allows users to view the details of each recipe
class ViewRecipeFragment : Fragment() {
    // Binding to the layout for this fragment
    private lateinit var binding: FragmentViewRecipeBinding
    // Shared MainViewModel to access the data and business logic
    private val viewModel: MainViewModel by activityViewModels()

    private var recipeId: Long = 0L

    // onCreateView method from the Fragment class that inflates the layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // view is bound to the FragmentViewRecipeBinding class (fragment_view_recipe.xml)
        binding = FragmentViewRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    // onViewCreated method from the Fragment class used to set up the ui, observe the ViewModel, and handle click events
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setting the title of the activity to "View Recipe"
        (activity as? MainActivity)?.setTitle("View Recipe")
        binding.lifecycleOwner = this
        // assinging the recipeid variable to the id passed in the arguments bundle
        // this is used to fetch the recipe details from the ViewModel
        recipeId = arguments?.getLong("id", 0L) ?: 0L
        viewModel.getRecipe(recipeId)

        // observing the viewingRecipe LiveData value from the ViewModel to get the recipe details
        viewModel.viewingRecipe.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                Log.i("BBK-LOG", "Recipe details received for: ${recipe.title}")
                // recipe details bound to the UI
                binding.recipeTitle.text = recipe.title
                binding.recipeDescription.text = recipe.shortDescription
                binding.ingredients.text = recipe.ingredients
                binding.instructions.text = recipe.instructions
                binding.category.text = recipe.category
                // setting the image for the recipe (handling null or erroneous image URLs)
                if (recipe.image.isNullOrBlank()) {
                    binding.recipePhotoView.setImageResource(R.drawable.placeholder_photo)
                } else {
                    try {
                        val imageUri = Uri.parse(recipe.image)
                        binding.recipePhotoView.setImageURI(imageUri)
                    } catch (e: Exception) {
                        Log.e("BBK-LOG", "Error parsing image URI: ${e.message}")
                        binding.recipePhotoView.setImageResource(R.drawable.placeholder_photo)
                    }
                }
                // setting the click listeners for the edit button, getting the recipe details and passing them to the EditRecipeFragment (which is then navigated to)
                binding.editButton.setOnClickListener {
                    Log.i("BBK-LOG", "Edit recipe button clicked")
                    val bundle = Bundle().apply {
                        putLong("id", recipe.id)
                        putString("title", recipe.title)
                        putString("image", recipe.image)
                        putString("description", recipe.shortDescription)
                        putString("ingredients", recipe.ingredients)
                        putString("instructions", recipe.instructions)
                        putString("category", recipe.category)
                    }
                    requireActivity().supportFragmentManager.setFragmentResult(
                        "edit_recipe",
                        bundle
                    )
                    findNavController().navigate(R.id.editRecipeFragment2, bundle)
                }

                // setting the click listener for the delete button, showing an AlertDialog to confirm deletion of the recipe and navigating back to RecipeListFragment if confirmed
                binding.deleteButtonView.setOnClickListener {
                    Log.i("BBK-LOG", "Delete recipe button clicked (ViewRecipeFragment)")
                    AlertDialog.Builder(requireContext())
                        .setTitle("Are you sure you want to delete this " + recipe.title + " recipe?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            viewModel.deleteRecipe(recipe)
                            findNavController().popBackStack()
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
        }
    }
    // onResume method from the Fragment class that ensures that the recipe details in the ViewRecipeFragment are updated when the fragment is resumed after editing
    override fun onResume() {
        super.onResume()
        viewModel.getRecipe(recipeId)
    }

    // Companion object to create a new instance of the ViewRecipeFragment with the recipe details passed as arguments
    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            image: String,
            description: String,
            ingredients: String,
            instructions: String,
            category: String
        ) = ViewRecipeFragment().apply {
            arguments = Bundle().apply {
                putString("title", title)
                putString("image", image)
                putString("description", description)
                putString("ingredients", ingredients)
                putString("instructions", instructions)
                putString("category", category)
            }
        }
    }
}
