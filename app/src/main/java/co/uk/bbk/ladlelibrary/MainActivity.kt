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
    // Binding to the layout for this activity
    private lateinit var binding: ActivityMainBinding
    // ViewModel to manage the app's data and business logic
    private val viewModel: MainViewModel by viewModels()

    // onCreate method from the AppCompatActivity class that sets up the activity's (the app's as its the only activity) layout and navigation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Setting the toolbar (in layout) as the app's action bar
        setSupportActionBar(binding.toolbar)
        // Setting up the navigation component with the NavHostFragment (in layout)
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as
                    NavHostFragment).navController
        // Setting up the navigation drawer
        val appBarConfiguration = AppBarConfiguration(navController.graph,
            binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        // binding the navigation drawer to the NavController
        binding.navView.setupWithNavController(navController)
        // handling navigation drawer item selection logic
        // added logic to prevent navigating to the same destination if already on it (led to crashes)
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

        // creating the database instance and setting the DAO in the MainViewModel
        val dao = RecipesDatabase.getInstance(applicationContext).recipeDao()
        viewModel.recipesDao = dao
        // calling the ViewModel's readAllRecipes() method to load the recipes from the database
        viewModel.readAllRecipes()

    }

    // function allowing the different fragments to set the title of the action bar
    fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    // onSupportNavigateUp() function from the AppCompatActivity class that handles the up navigation (back button in the toolbar)
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