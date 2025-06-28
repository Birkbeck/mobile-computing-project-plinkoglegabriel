package co.uk.bbk.ladlelibrary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.uk.bbk.ladlelibrary.RecipeItem
import co.uk.bbk.ladlelibrary.RecipesDao
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _recipes = MutableLiveData(listOf<RecipeItem>())
    val recipes: LiveData<List<RecipeItem>> = _recipes

    var recipesDao: RecipesDao? = null

    val title = MutableLiveData<String>()
    val shortDescription = MutableLiveData<String>()
    val ingredients = MutableLiveData<String>()
    val instructions = MutableLiveData<String>()
    val category = MutableLiveData<String>()

    fun readAllRecipes() {
        viewModelScope.launch {
            recipesDao?.let {
                val recipes = it.getAllRecipes()

                Log.i("BBK", recipes.toString())
                _recipes.value = recipes
            }
        }
    }

    fun addRecipe(title: String, shortDescription: String, ingredients: String, instructions: String, category: String) {
        viewModelScope.launch {
            recipesDao?.let {
                val recipe = RecipeItem(imageResId = R.drawable.placeholder_photo, title = title, shortDescription = shortDescription,
                                        ingredients = ingredients, instructions = instructions,
                                        category = category)
                it.insertRecipe(recipe)

                readAllRecipes()
            }
        }

    }

    fun editRecipe(recipe: RecipeItem) {
        viewModelScope.launch {
            recipesDao?.let {
                it.updateRecipe(recipe)

                readAllRecipes()
            }
        }

    }

    fun deleteRecipe(recipe: RecipeItem) {
        viewModelScope.launch {
            recipesDao?.let {
                it.deleteRecipe(recipe)

                readAllRecipes()
            }
        }

    }
}