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
import co.uk.bbk.ladlelibrary.EditRecipeViewModel
import co.uk.bbk.ladlelibrary.MainActivity
import co.uk.bbk.ladlelibrary.databinding.FragmentEditRecipeBinding
import co.uk.bbk.ladlelibrary.RecipeListViewModel
import kotlin.getValue

class EditRecipeFragment : Fragment() {
    private lateinit var binding: FragmentEditRecipeBinding
    private val viewModel: EditRecipeViewModel by viewModels()
    private val recipeListViewModel: RecipeListViewModel by activityViewModels()

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
        val id = args?.getInt("id") ?: -1
        val imageResId = args?.getInt("imageResId") ?: 0
        val title = args?.getString("title") ?: "No Title"
        val shortDescription = args?.getString("description") ?: "No Description"
        val ingredients = args?.getString("ingredients") ?: "No Ingredients"
        val instructions = args?.getString("instructions") ?: "No Instructions"
        val category = args?.getString("category") ?: "No Category"

        viewModel.recipeId.value = id
        viewModel.title.value = title
        viewModel.shortDescription.value = shortDescription
        viewModel.ingredients.value = ingredients
        viewModel.instructions.value = instructions
        viewModel.imageResId.value = imageResId
        viewModel.category.value = category

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, viewModel.categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryInputEdit.adapter = adapter

        val index = viewModel.getIndexOfSelectedCategory()
        if (index != -1) {
            binding.categoryInputEdit.setSelection(index)
        }

        binding.categoryInputEdit.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected( parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    viewModel.selectCategoryAt(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    viewModel.category.value = ""
                }
            }

        viewModel.editedRecipe.observe(viewLifecycleOwner) { editedRecipe ->
            editedRecipe?.let {
                Log.i("BBK", "Received updated recipe with id: ${it.id}")
                recipeListViewModel.showEditedRecipe(it)
                findNavController().popBackStack()
            }
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(
            id: Int,
            imageResId: Int,
            title: String,
            description: String,
            ingredients: String,
            instructions: String,
            category: String
        ) = EditRecipeFragment().apply {
            arguments = Bundle().apply {
                putInt("id", id)
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