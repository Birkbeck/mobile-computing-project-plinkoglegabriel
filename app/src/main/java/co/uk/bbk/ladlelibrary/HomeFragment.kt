package co.uk.bbk.ladlelibrary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.uk.bbk.ladlelibrary.databinding.FragmentHomeBinding

// Home fragment - the main entry point of the app that displays the app's logo and title and buttons to add a recipe and view currently saved recipes
class HomeFragment : Fragment() {
    // Binding to the layout for this fragment
    private lateinit var binding: FragmentHomeBinding

    // onCreateView method from the Fragment class that inflates the layout (here it is bound to the FragmentHomeBinding class (fragment_home.xml))
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    // onViewCreated method from the Fragment class that sets the title of the activity and sets up click listeners for the buttons
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setting title
        (activity as? MainActivity)?.setTitle("Home")

        // setting click listeners for the button to view recipes (using navigation component to navigate to the RecipeListFragment)
        Log.i("BBK-LOG","Button to view recipes clicked")
        binding.recipeListButtonMain.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_homeFragment_to_recipeListFragment2)
        }

        // setting click listener for the button to add a new recipe (navigate to the AddRecipeFragment using the navigation component)
        Log.i("BBK-LOG","Button to add a new recipe clicked")
        binding.addButtonMain.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_homeFragment_to_addRecipeFragment2)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
