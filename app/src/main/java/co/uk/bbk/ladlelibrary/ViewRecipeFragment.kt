package co.uk.bbk.ladlelibrary

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import co.uk.bbk.ladlelibrary.databinding.FragmentViewRecipeBinding
import androidx.fragment.app.viewModels
import co.uk.bbk.ladlelibrary.MainViewModel

class ViewRecipeFragment : Fragment() {

    private lateinit var binding: FragmentViewRecipeBinding
    private val viewModel: MainViewModel by activityViewModels()

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
        val args = arguments
        args?.let {
            val recipe = RecipeItem(
                imageResId = args.getInt("imageResId", 0),
                title = args.getString("title") ?: "No Title",
                shortDescription = args.getString("description") ?: "No Description",
                ingredients = args.getString("ingredients") ?: "No Ingredients",
                instructions = args.getString("instructions") ?: "No Instructions",
                category = args.getString("category") ?: "No Category"
            )
            Log.i("BBK-LOG", "Recipe details received for: ${recipe.title}")
            // recipe details bound to the UI
            binding.recipeTitle.text = recipe.title
            binding.recipeDescription.text = recipe.shortDescription
            binding.ingredients.text = recipe.ingredients
            binding.instructions.text = recipe.instructions
            binding.category.text = recipe.category
            binding.recipePhotoView.setImageResource(recipe.imageResId)
        }


    }

    companion object {
        @JvmStatic
        fun newInstance(
            imageResId: Int,
            title: String,
            description: String,
            ingredients: String,
            instructions: String,
            category: String
        ) = ViewRecipeFragment().apply {
            arguments = Bundle().apply {
                putInt("imageResId", imageResId)
                putString("title", title)
                putString("description", description)
                putString("ingredients", ingredients)
                putString("instructions", instructions)
                putString("category", category)
            }
        }
    }
}
