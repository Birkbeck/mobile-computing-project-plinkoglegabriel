package co.uk.bbk.ladlelibrary

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditRecipeViewModel : ViewModel() {
    val recipeId = MutableLiveData<Int>()
    val imageResId = MutableLiveData<Int>()
    val title = MutableLiveData<String>()
    val shortDescription = MutableLiveData<String>()
    val ingredients = MutableLiveData<String>()
    val instructions = MutableLiveData<String>()
    val category = MutableLiveData<String>()

    private val _editedRecipe = MutableLiveData<RecipeItem?>()
    val editedRecipe: LiveData<RecipeItem?> = _editedRecipe

    val categories = listOf("Breakfast", "Brunch", "Lunch", "Dinner", "Desserts", "Other")

    fun selectCategoryAt(index: Int) {
        category.value = categories.getOrNull(index)
    }

    fun getIndexOfSelectedCategory(): Int {
        return categories.indexOfFirst { it.equals(category.value, ignoreCase = true) }
    }

    fun editRecipe() {
        Log.i("BBK", "editedRecipe called with id: ${recipeId.value}")
        val editedRecipe = RecipeItem(
            id = recipeId.value ?: -1,
            imageResId = imageResId.value ?: R.drawable.placeholder_photo,
            title = title.value ?: "",
            shortDescription = shortDescription.value ?: "",
            ingredients = ingredients.value ?: "",
            instructions = instructions.value ?: "",
            category = category.value ?: ""
        )
        _editedRecipe.value = editedRecipe
    }
}