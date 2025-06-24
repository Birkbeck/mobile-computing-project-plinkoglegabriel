package co.uk.bbk.ladlelibrary

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.uk.bbk.ladlelibrary.databinding.FragmentRecipeListBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

class RecipeListFragment : Fragment() {

    private lateinit var binding: FragmentRecipeListBinding
    private val viewModel: RecipeListViewModel by viewModels()

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

        // using temporary data for now
        if (viewModel.recipesList.value.isNullOrEmpty()) {
            val recipes = listOf(
                RecipeItem(1, R.drawable.pancakes, "Pancakes", "description of pancake recipe", "...", "...", "Breakfast"),
                RecipeItem(2, R.drawable.lasagna, "Lasagna", "description of lasagna recipe", "...", "...", "Dinner")
            )
            viewModel.setRecipes(recipes)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        Log.i("BBK-LOG","Button to add a new recipe clicked")
        // obsever recipesList from the ViewModel checking for changes
        viewModel.recipesList.observe(viewLifecycleOwner, Observer { recipes ->
            binding.recyclerView.adapter = RecipeListAdapter(recipes,
            onRecipeClick = { recipe ->
                Log.i("BBK-LOG","View recipe button clicked")
            val bundle = Bundle().apply {
                putString("title", recipe.title)
                putInt("imageResId", recipe.imageResId)
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
                    putString("title", recipe.title)
                    putInt("imageResId", recipe.imageResId)
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
                        viewModel.deleteRecipe(recipe.id)
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
            )
    })
    }

    companion object {
        @JvmStatic
        fun newInstance() = RecipeListFragment()
    }
}
