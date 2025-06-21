package co.uk.bbk.ladlelibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.uk.bbk.ladlelibrary.databinding.FragmentViewRecipeBinding

class ViewRecipeFragment : Fragment() {

    private lateinit var binding: FragmentViewRecipeBinding

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

        val args = arguments
        val imageResId = args?.getInt("imageResId") ?: 0
        val title = args?.getString("title") ?: "No Title"
        val description = args?.getString("description") ?: "No Description"
        val ingredients = args?.getString("ingredients") ?: "No Ingredients"
        val instructions = args?.getString("instructions") ?: "No Instructions"
        val category = args?.getString("category") ?: "No Category"

        binding.recipePhotoView.setImageResource(imageResId)
        binding.recipeTitle.text = title
        binding.recipeDescription.text = description
        binding.ingredients.text = ingredients
        binding.instructions.text = instructions
        binding.category.text = category
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
