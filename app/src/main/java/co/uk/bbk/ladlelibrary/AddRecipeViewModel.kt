package co.uk.bbk.ladlelibrary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class AddRecipeViewModel : ViewModel() {
    private val _recipe = MutableLiveData<RecipeItem?>()
    val recipe: LiveData<RecipeItem?> = _recipe

    val title = MutableLiveData<String>()
    val shortDescription = MutableLiveData<String>()
    val ingredients = MutableLiveData<String>()
    val instructions = MutableLiveData<String>()
    val category = MutableLiveData<String>()

    private val _imageResId = MutableLiveData<Int>().apply { value = R.drawable.placeholder_photo }
    val imageResId: LiveData<Int> = _imageResId

    val categories = listOf("Breakfast", "Brunch", "Lunch", "Dinner", "Desserts", "Other")

    fun addRecipe() {
        val newRecipe = RecipeItem(
            id = (0..100).random(),
            imageResId = imageResId.value ?: R.drawable.placeholder_photo,
            title = title.value ?: "",
            shortDescription = shortDescription.value ?: "",
            ingredients = ingredients.value ?: "",
            instructions = instructions.value ?: "",
            category = category.value ?: ""
        )
        _recipe.value = newRecipe
    }

    fun getIndexOfSelectedCategory(): Int {
        return categories.indexOfFirst { it.equals(category.value ?: "", ignoreCase = true) }
    }

    fun selectCategoryAt(index: Int) {
        category.value = categories.getOrNull(index)
    }

    fun clearRecipe() {
        _recipe.value = null
    }
}