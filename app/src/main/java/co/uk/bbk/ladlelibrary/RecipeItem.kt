package co.uk.bbk.ladlelibrary

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

// consider for later
enum class Category { Breakfast, Brunch, Lunch, Dinner, Desserts, Other
}

// Data class storing recipe details
@Entity(tableName = "Recipes")
data class RecipeItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 1,
    val imageResId: Int,
    val title: String,
    val shortDescription: String,
    val ingredients: String,
    val instructions: String,
    val category: String
) : Serializable
