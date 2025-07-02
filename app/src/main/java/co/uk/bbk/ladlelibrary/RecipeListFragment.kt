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

// RecipeListFragment that displays a list of recipes and allows users to view, edit, or delete them and add a new recipe also
class RecipeListFragment : Fragment() {
    // Binding to the layout for this fragment
    private lateinit var binding: FragmentRecipeListBinding
    // Shared MainViewModel to access the data and business logic
    private val viewModel: MainViewModel by activityViewModels()
    // adapter variable assigned to the RecipeListAdapter class that handles the display of recipes in a RecyclerView
    private lateinit var adapter: RecipeListAdapter

    // onCreateView method from the Fragment class that inflates the layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // view is bound to the FragmentRecipeListBinding class (fragment_recipe_list.xml)
        binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        // binding the viewModel to the layout and setting the lifecycle owner to this fragment
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    // onViewCreated method from the Fragment class used in this case to set up the RecyclerView, adapter and click listeners for the buttons
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setting the title of the activity to "Your Recipes"
        (activity as? MainActivity)?.setTitle("Your Recipes")

        // calling the readAllRecipes method from the ViewModel to get all recipes from the database
        viewModel.readAllRecipes()

        // recyclerView in layout is set up with a LinearLayoutManager to display the list of recipes
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = RecipeListAdapter(
            // adapter is initialised with an empty list (for list of recipes)
            emptyList(),
            // adapter is set up with click listeners for viewing, editing, and deleting recipes
            // onRecipeClick and onEditClick bundle the recipe data and navigate to their respective fragment
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
            // onDeleteClick shows an AlertDialog to confirm deletion of the recipe and if confirmed, uses deleteRecipe() from the ViewModel to delete the recipe
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

        // setting the adapter to the RecyclerView
        binding.recyclerView.adapter = adapter

        // observer recipesList from the ViewModel checking for changes
        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            adapter.updateRecipes(recipes)
            Log.i("BBK-LOG", "Recipes updated in RecipeListFragment")
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
