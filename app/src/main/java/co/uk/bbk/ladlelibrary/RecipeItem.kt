package co.uk.bbk.ladlelibrary

// Data class storing recipe details
data class RecipeItem(
    val title: String = "Recipe",
    val shortDescription: String = "Short description of recipe",
    val ingredients: String = "List of Ingredients needed to make recipe",
    val instructions: String = "List of instructions to follow",
    val category: String = "Category",
    val imagePath: String? = null
)