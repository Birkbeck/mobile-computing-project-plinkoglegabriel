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

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setTitle("Home")

        Log.i("BBK-LOG","Button to view recipes clicked")
        binding.recipeListButtonMain.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_homeFragment_to_recipeListFragment2)
        }

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
