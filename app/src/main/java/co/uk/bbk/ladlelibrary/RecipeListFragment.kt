package co.uk.bbk.ladlelibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.uk.bbk.ladlelibrary.databinding.FragmentRecipeListBinding
import androidx.fragment.app.viewModels
import co.uk.bbk.ladlelibrary.MainViewModel

class RecipeListFragment : Fragment() {

    private lateinit var binding: FragmentRecipeListBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setTitle("Your Recipes")

        // using temporary data for now
        val recipes = listOf(
            RecipeItem(1, R.drawable.pancakes, "Pancakes", "description of pancake recipe", "...", "...", "Breakfast"),
            RecipeItem(2, R.drawable.lasagna, "Lasagna", "description of lasagna recipe", "...", "...", "Dinner")
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerView.adapter = RecipeListAdapter(recipes,
            onRecipeClick = { recipe ->
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
            }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = RecipeListFragment()
    }
}
