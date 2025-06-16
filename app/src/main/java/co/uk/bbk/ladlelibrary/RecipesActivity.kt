package co.uk.bbk.ladlelibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.uk.bbk.ladlelibrary.databinding.ActivityRecipesBinding
import androidx.recyclerview.widget.LinearLayoutManager

class RecipesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        to implement - to be gotten from memory (placeholder for now)
        val recipes = listOf(
            RecipeItem(1, R.drawable.pancakes, "Pancakes", "description of pancake recipe", "...", "...", "Breakfast"),
            RecipeItem(2, R.drawable.lasagna, "Lasagna", "description of lasagna recipe", "...", "...", "Dinner")
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = RecipeListAdapter(recipes)

    }
}