package co.uk.bbk.ladlelibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.uk.bbk.ladlelibrary.databinding.ActivityMainBinding
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recipeListButtonMain.setOnClickListener {
            val intent = Intent(this, RecipesActivity::class.java)
            startActivity(intent)
        }

        binding.addButtonMain.setOnClickListener {
//            implement
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_home -> {
                Log.i("BBK-LOG", "Button to Home clicked")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true

            }
            R.id.action_see_list -> {
                Log.i("BBK-LOG", "List of recipes button clicked")
                // Handle add action
                val intent = Intent(this, RecipesActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_add -> {
                Log.i("BBK-LOG", "Add recipe button clicked")
                // implement add recipe action
                val intent = Intent(this, AddRecipeActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return true
    }
}