package co.uk.bbk.ladlelibrary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewRecipeViewModel : ViewModel() {
    private val _recipe = MutableLiveData<RecipeItem?>()
    val recipe: LiveData<RecipeItem?> = _recipe

    fun setRecipe(recipe: RecipeItem) {
        _recipe.value = recipe
    }
}