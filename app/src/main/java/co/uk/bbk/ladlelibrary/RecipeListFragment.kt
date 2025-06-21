package co.uk.bbk.ladlelibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import co.uk.bbk.ladlelibrary.databinding.FragmentRecipeListBinding

class RecipeListFragment : Fragment() {

    private lateinit var binding: FragmentRecipeListBinding

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


        val recipes = listOf(
            RecipeItem(1, R.drawable.pancakes, "Pancakes", "description of pancake recipe", "...", "...", "Breakfast"),
            RecipeItem(2, R.drawable.lasagna, "Lasagna", "description of lasagna recipe", "...", "...", "Dinner")
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = RecipeListAdapter(recipes)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RecipeListFragment()
    }
}
