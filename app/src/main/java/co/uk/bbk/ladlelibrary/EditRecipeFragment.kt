package co.uk.bbk.lab6

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import co.uk.bbk.ladlelibrary.MainActivity
import co.uk.bbk.ladlelibrary.ViewRecipeFragment
import co.uk.bbk.ladlelibrary.databinding.FragmentEditRecipeBinding

class EditRecipeFragment : Fragment() {
    private lateinit var binding: FragmentEditRecipeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditRecipeBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setTitle("Edit Recipe")

        val args = arguments
        val imageResId = args?.getInt("imageResId") ?: 0
        val title = args?.getString("title") ?: "No Title"
        val description = args?.getString("description") ?: "No Description"
        val ingredients = args?.getString("ingredients") ?: "No Ingredients"
        val instructions = args?.getString("instructions") ?: "No Instructions"
        val category = args?.getString("category") ?: "No Category"

        binding.photoUploadButtonEdit.setImageResource(imageResId)
        binding.titleTextEdit.setText(title)
        binding.descriptionInputEdit.setText(description)
        binding.ingredientsInputEdit.setText(ingredients)
        binding.instructionsInputEdit.setText(instructions)
        val categories = listOf("Breakfast", "Brunch", "Lunch", "Dinner", "Desserts", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryInputEdit.adapter = adapter

        val index = categories.indexOfFirst { it.equals(category, ignoreCase = true) }
        if (index != -1) {
            binding.categoryInputEdit.setSelection(index)
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
        ) = EditRecipeFragment().apply {
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