package co.uk.bbk.ladlelibrary

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface RecipesDao {
    @Query("SELECT * FROM Recipes;")
    suspend fun getAllRecipes(): List<RecipeItem>

    @Insert
    suspend fun insertRecipe(recipe: RecipeItem)

    @Update
    suspend fun updateRecipe(recipe: RecipeItem)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeItem)

    @Query("SELECT * FROM Recipes WHERE id = :id")
    suspend fun getRecipe(id: Long): RecipeItem?

}