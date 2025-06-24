package co.uk.bbk.lab6

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.uk.bbk.ladlelibrary.MainActivity
import co.uk.bbk.ladlelibrary.databinding.FragmentAddRecipeBinding
import androidx.fragment.app.viewModels
import co.uk.bbk.ladlelibrary.AddRecipeViewModel

class AddRecipeFragment : Fragment() {
    private lateinit var binding: FragmentAddRecipeBinding
    private val viewModel: AddRecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRecipeBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setTitle("Add New Recipe")
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddRecipeFragment()
    }
}