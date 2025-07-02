package co.uk.bbk.ladlelibrary

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// The RecipesDao interface is used to access the database and the RecipeItem entity
@Dao
interface RecipesDao {
    // geting all recipes from the database
    @Query("SELECT * FROM Recipes;")
    suspend fun getAllRecipes(): List<RecipeItem>

    // inserting, updating, deleting a recipe
    @Insert
    suspend fun insertRecipe(recipe: RecipeItem)

    @Update
    suspend fun updateRecipe(recipe: RecipeItem)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeItem)

    // getting a recipe by its id
    @Query("SELECT * FROM Recipes WHERE id = :id")
    suspend fun getRecipe(id: Long): RecipeItem?

    // getting a recipe by its title to check there are no duplicates
    @Query("SELECT * FROM Recipes WHERE title = :title LIMIT 1")
    suspend fun getTitle(title: String): RecipeItem?

    // getting a recipe by its title and id to check there are no duplicates when editing a recipe
    @Query("SELECT * FROM Recipes WHERE title = :title AND id != :id LIMIT 1")
    suspend fun getTitleWithId(title: String, id: Long): RecipeItem?
}