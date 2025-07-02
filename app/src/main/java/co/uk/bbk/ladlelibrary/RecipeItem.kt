package co.uk.bbk.ladlelibrary

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

// Enum class holding the list of recipe categories
enum class Category { Breakfast, Brunch, Lunch, Dinner, Desserts, Other
}

// Data class storing recipe details
@Entity(tableName = "Recipes")
data class RecipeItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val image: String,
    val shortDescription: String,
    val ingredients: String,
    val instructions: String,
    val category: String
) : Serializable
