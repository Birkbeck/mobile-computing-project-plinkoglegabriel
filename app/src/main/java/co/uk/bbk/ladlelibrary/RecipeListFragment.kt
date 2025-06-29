package co.uk.bbk.ladlelibrary

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.uk.bbk.ladlelibrary.databinding.FragmentRecipeListBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import co.uk.bbk.ladlelibrary.MainViewModel

class RecipeListFragment : Fragment() {

    private lateinit var binding: FragmentRecipeListBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: RecipeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setTitle("Your Recipes")

        viewModel.readAllRecipes()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        Log.i("BBK-LOG","Button to add a new recipe clicked")

        adapter = RecipeListAdapter(
            emptyList(),
            onRecipeClick = { recipe ->
                Log.i("BBK-LOG","View recipe button clicked")
            val bundle = Bundle().apply {
                putLong("id", recipe.id)
                putString("title", recipe.title)
                putString("image", recipe.image)
                putString("description", recipe.shortDescription)
                putString("ingredients", recipe.ingredients)
                putString("instructions", recipe.instructions)
                putString("category", recipe.category)
            }
            requireActivity().supportFragmentManager.setFragmentResult("view_recipe", bundle)
            findNavController().navigate(R.id.viewRecipeFragment2, bundle)
        },
            onEditClick = { recipe ->
                Log.i("BBK-LOG","Edit recipe button clicked")
                val bundle = Bundle().apply {
                    putLong("id", recipe.id)
                    putString("title", recipe.title)
                    putString("image", recipe.image)
                    putString("description", recipe.shortDescription)
                    putString("ingredients", recipe.ingredients)
                    putString("instructions", recipe.instructions)
                    putString("category", recipe.category)
                }
                requireActivity().supportFragmentManager.setFragmentResult("edit_recipe", bundle)
                findNavController().navigate(R.id.editRecipeFragment2, bundle)
            },
            onDeleteClick = { recipe ->
                Log.i("BBK-LOG","Delete recipe button clicked")
                AlertDialog.Builder(requireContext())
                    .setTitle("Are you sure you want to delete this " + recipe.title + " recipe?")
                    .setPositiveButton("Yes") { dialog, _ ->
                        viewModel.deleteRecipe(recipe)
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
            )
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // obsever recipesList from the ViewModel checking for changes
        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            adapter.updateRecipes(recipes)
        }
        // on click listener that directs to add recipe fragment
        binding.addButtonRecipes.setOnClickListener {
            Log.i("BBK-LOG","Button to add a new recipe clicked")
            findNavController().navigate(R.id.action_recipeListFragment2_to_addRecipeFragment2)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = RecipeListFragment()
    }
}
