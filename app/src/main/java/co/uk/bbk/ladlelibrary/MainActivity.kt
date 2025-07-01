package co.uk.bbk.ladlelibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.uk.bbk.ladlelibrary.databinding.ActivityMainBinding
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.util.Log
import android.view.Gravity
import androidx.activity.viewModels

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.findNavController
import co.uk.bbk.ladlelibrary.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as
                    NavHostFragment).navController
        val appBarConfiguration = AppBarConfiguration(navController.graph,
            binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.navView.setNavigationItemSelectedListener {
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
            val destination = it.itemId
            val current = navController.currentDestination?.id
            if (navController.graph.findNode(destination) != null && current != destination) {
                Log.i("BBK-LOG", "Navigating to new fragment: $destination")
                navController.navigate(destination)
            } else {
                Log.i("BBK-LOG", "Already on the selected destination")
            }
            true
        }

        val dao = RecipesDatabase.getInstance(applicationContext).recipeDao()
        viewModel.recipesDao = dao
        viewModel.readAllRecipes()

    }
    fun setTitle(title: String) {
        supportActionBar?.title = title
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        val up = navController.navigateUp()
        if(!up && !binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
            return false
        }
        return true
    }
}