package co.uk.bbk.ladlelibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.uk.bbk.ladlelibrary.databinding.ActivityMainBinding
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.util.Log
import android.view.Gravity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
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
            if(navController.currentDestination?.id != it.itemId) {
                navController.navigate(it.itemId)
            }
            true
        }
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