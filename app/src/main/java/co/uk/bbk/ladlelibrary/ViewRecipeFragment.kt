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

class ViewRecipeFragment : Fragment() {

    private lateinit var binding: FragmentViewRecipeBinding
    private val viewModel: MainViewModel by activityViewModels()
    private var recipeId: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setTitle("View Recipe")
        binding.lifecycleOwner = this
        recipeId = arguments?.getLong("id", 0L) ?: 0L
        viewModel.getRecipe(recipeId)

        viewModel.viewingRecipe.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                Log.i("BBK-LOG", "Recipe details received for: ${recipe.title}")
                // recipe details bound to the UI
                binding.recipeTitle.text = recipe.title
                binding.recipeDescription.text = recipe.shortDescription
                binding.ingredients.text = recipe.ingredients
                binding.instructions.text = recipe.instructions
                binding.category.text = recipe.category
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
    override fun onResume() {
        super.onResume()
        viewModel.getRecipe(recipeId)
    }

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
