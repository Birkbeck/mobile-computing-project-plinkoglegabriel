package co.uk.bbk.ladlelibrary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// The MainViewModel class is the ViewModel where the fragment and activity can access the data and business logic
class MainViewModel: ViewModel() {
    // MutableLiveData is used to hold the list of recipes, their details and the currently viewed recipe
    private val _recipes = MutableLiveData(listOf<RecipeItem>())
    val recipes: LiveData<List<RecipeItem>> = _recipes

    // also initialising the RecipesDao to interact with the database
    var recipesDao: RecipesDao? = null

    val title = MutableLiveData<String>()
    val image = MutableLiveData<String>()
    val shortDescription = MutableLiveData<String>()
    val ingredients = MutableLiveData<String>()
    val instructions = MutableLiveData<String>()
    val category = MutableLiveData<String>()

    private val _viewingRecipe = MutableLiveData<RecipeItem?>()
    val viewingRecipe: LiveData<RecipeItem?> = _viewingRecipe

    // function to read all recipes from the database
    fun readAllRecipes() {
        viewModelScope.launch {
            recipesDao?.let {
                val recipes = it.getAllRecipes()

                Log.i("BBK", recipes.toString())
                _recipes.value = recipes
            }
        }
    }

    // function to add a new recipe to the database
    fun addRecipe(title: String, image: String, shortDescription: String, ingredients: String, instructions: String, category: String) {
        viewModelScope.launch {
            recipesDao?.let {
                val recipe = RecipeItem(title = title, image = image, shortDescription = shortDescription,
                                        ingredients = ingredients, instructions = instructions,
                                        category = category)
                it.insertRecipe(recipe)

                readAllRecipes()
            }
        }

    }

    // function to edit an existing recipe in the database
    fun editRecipe(recipe: RecipeItem) {
        viewModelScope.launch {
            recipesDao?.let {
                it.updateRecipe(recipe)

                readAllRecipes()
            }
        }

    }

    // function to delete a recipe from the database
    fun deleteRecipe(recipe: RecipeItem) {
        viewModelScope.launch {
            recipesDao?.let {
                it.deleteRecipe(recipe)

                readAllRecipes()
            }
        }

    }

    // getting a recipe by its id (from the database)
    fun getRecipe(id: Long) {
        viewModelScope.launch {
            recipesDao?.let { dao ->
                _viewingRecipe.value = dao.getRecipe(id)
            }
        }
    }

    // function to check if a title is unique and already belonging to another recipe with a different id
    suspend fun uniqueTitleCheck(title: String, id: Long?): Boolean {
        return if (id == null) {
            recipesDao?.getTitle(title) != null
        } else {
            recipesDao?.getTitleWithId(title, id) != null
        }
    }
}