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
import co.uk.bbk.ladlelibrary.Category
import co.uk.bbk.ladlelibrary.MainViewModel

class AddRecipeFragment : Fragment() {
    private lateinit var binding: FragmentAddRecipeBinding
    private val viewModel: MainViewModel by activityViewModels()

    private var category: Category = Category.Other

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

        // Handling options for the category spinner
        val categories = Category.entries.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categoryInputAdd.adapter = adapter

        binding.categoryInputAdd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                category = Category.values()[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                category = Category.Other
            }
        }

        binding.saveButtonAdd.setOnClickListener {
            val title = binding.titleInputAdd.text.toString()
            val shortDescription = binding.descriptionInputAdd.text.toString()
            val ingredients = binding.ingredientsInputAdd.text.toString()
            val instructions = binding.instructionsInputAdd.text.toString()

            viewModel.addRecipe(title = title, shortDescription = shortDescription, ingredients = ingredients, instructions = instructions,  category = category.name)
                findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.title.value = ""
        viewModel.shortDescription.value = ""
        viewModel.ingredients.value = ""
        viewModel.instructions.value = ""
        viewModel.category.value = ""
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddRecipeFragment()
    }
}