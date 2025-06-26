package co.uk.bbk.ladlelibrary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class RecipeListViewModel : ViewModel() {

    private val _recipesList = MutableLiveData<List<RecipeItem>>()
    val recipesList: LiveData<List<RecipeItem>> = _recipesList

    fun setRecipes(recipes: List<RecipeItem>) {
        _recipesList.value = recipes
    }

    fun deleteRecipe(recipeId: Int) {
        val currentRecipeList = _recipesList.value?.toMutableList() ?: return
        currentRecipeList.removeAll { it.id == recipeId }
        _recipesList.value = currentRecipeList
    }

    fun addRecipe(recipe: RecipeItem) {
        val currentList = _recipesList.value?.toMutableList() ?: mutableListOf()
        currentList.add(recipe)
        _recipesList.value = currentList
    }

}