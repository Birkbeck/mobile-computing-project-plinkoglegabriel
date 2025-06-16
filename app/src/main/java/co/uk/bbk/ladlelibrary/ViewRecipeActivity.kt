package co.uk.bbk.ladlelibrary

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import co.uk.bbk.ladlelibrary.databinding.ActivityViewRecipeBinding

class ViewRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageResId = intent.getIntExtra("imageResId", 0)
        val title = intent.getStringExtra("title") ?: "No Title"
        val description = intent.getStringExtra("description") ?: "No Description"
        val ingredients = intent.getStringExtra("ingredients") ?: "No Ingredients"
        val instructions = intent.getStringExtra("instructions") ?: "No Instructions"
        val category = intent.getStringExtra("category") ?: "No Category"

        val image = findViewById<ImageView>(R.id.recipe_photo_view)
        val titleView = findViewById<TextView>(R.id.recipe_title)
        val descriptionView = findViewById<TextView>(R.id.recipe_description)
        val ingredientsView = findViewById<TextView>(R.id.ingredients)
        val instructionsView = findViewById<TextView>(R.id.instructions)
        val categoryView = findViewById<TextView>(R.id.category)

        image.setImageResource(imageResId)
        titleView.text = title
        descriptionView.text = description
        ingredientsView.text = ingredients
        instructionsView.text = instructions
        categoryView.text = category
    }

}