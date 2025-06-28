package co.uk.bbk.lab6


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.uk.bbk.ladlelibrary.Category
import co.uk.bbk.ladlelibrary.MainActivity
import co.uk.bbk.ladlelibrary.MainViewModel
import co.uk.bbk.ladlelibrary.RecipeItem
import co.uk.bbk.ladlelibrary.databinding.FragmentEditRecipeBinding
import kotlin.getValue

class EditRecipeFragment : Fragment() {
    private lateinit var binding: FragmentEditRecipeBinding
    private val viewModel: MainViewModel by activityViewModels()
    private var category: Category = Category.Other

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
        viewModel.shortDescription.value = args?.getString("description")
        viewModel.ingredients.value = args?.getString("ingredients")
        viewModel.instructions.value = args?.getString("instructions")
        viewModel.category.value = categoryString

        val categories = Category.entries.map { it.name }

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
                    category = Category.valueOf(categories[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    category = Category.Other
                }
            }

        binding.saveButtonEdit.setOnClickListener {
            val editedTitle = binding.titleTextEdit.text.toString()
            val editedShortDescription = binding.descriptionInputEdit.text.toString()
            val editedIngredients = binding.ingredientsInputEdit.text.toString()
            val editedInstructions = binding.instructionsInputEdit.text.toString()

            val editedRecipe = RecipeItem(
                    // image retrieval is a placeholder for now
                    id = id,
                    imageResId = args?.getInt("imageResId") ?: 0,
                    title = editedTitle,
                    shortDescription = editedShortDescription,
                    ingredients = editedIngredients,
                    instructions = editedInstructions,
                    category = category.name
                )

            viewModel.editRecipe(editedRecipe)
            findNavController().popBackStack()
            }
    }
    companion object {
        @JvmStatic
        fun newInstance(
            id: Long,
            imageResId: Int,
            title: String,
            description: String,
            ingredients: String,
            instructions: String,
            category: String
        ) = EditRecipeFragment().apply {
            arguments = Bundle().apply {
                putLong("id", id)
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