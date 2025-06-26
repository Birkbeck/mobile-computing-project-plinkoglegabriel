package co.uk.bbk.lab6

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import co.uk.bbk.ladlelibrary.MainActivity
import co.uk.bbk.ladlelibrary.databinding.FragmentAddRecipeBinding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import co.uk.bbk.ladlelibrary.AddRecipeViewModel
import co.uk.bbk.ladlelibrary.RecipeListViewModel

class AddRecipeFragment : Fragment() {
    private lateinit var binding: FragmentAddRecipeBinding
    private val viewModel: AddRecipeViewModel by viewModels()
    private val recipeListViewModel: RecipeListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRecipeBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setTitle("Add New Recipe")

        viewModel.recipe.observe(viewLifecycleOwner) { newRecipe ->
            newRecipe?.let {
                recipeListViewModel.addRecipe(it)
                findNavController().popBackStack()
                viewModel.clearRecipe()
            }
        }

        // Handling options for the category spinner
        val categories = viewModel.categories
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryInputAdd.adapter = adapter

        val index = viewModel.getIndexOfSelectedCategory()
        if (index >= 0) {
            binding.categoryInputAdd.setSelection(index)
        }

        binding.categoryInputAdd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                viewModel.selectCategoryAt(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                viewModel.selectCategoryAt(-1)
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = AddRecipeFragment()
    }
}