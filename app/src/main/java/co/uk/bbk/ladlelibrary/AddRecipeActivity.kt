package co.uk.bbk.ladlelibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import co.uk.bbk.ladlelibrary.databinding.ActivityAddRecipeBinding

class AddRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

