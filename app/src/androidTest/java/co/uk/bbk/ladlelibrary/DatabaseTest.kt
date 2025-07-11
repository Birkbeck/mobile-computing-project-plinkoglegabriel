package co.uk.bbk.ladlelibrary

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import android.content.Context
import kotlinx.coroutines.runBlocking
import java.io.IOException
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

// Test class to test the RecipesDatabase and RecipesDao functionality
@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    // Initialising the dao and database variables
    private lateinit var recipeDao: RecipesDao
    private lateinit var db: RecipesDatabase

    // Sample recipes for testing
    private val pancakes = RecipeItem(
        title = "Pancakes",
        image = R.drawable.pancakes.toString(),
        shortDescription = "Fluffy and tasty breakfast treat",
        ingredients = "Flour, Milk, Eggs, Sugar, Baking Powder",
        instructions = "Mix ingredients. Then fry and flip.",
        category = "Breakfast"
    )
    private val lasagna = RecipeItem(
        title = "Lasagna",
        image = R.drawable.lasagna.toString(),
        shortDescription = "Classic Italian pasta dish",
        ingredients = "Lasagna noodles, Tomato sauce, Cheese",
        instructions = "Layer ingredients and bake in the oven until ready.",
        category = "Dinner"
    )

    private val greekSalad = RecipeItem(
        title = "Greek Salad",
        image = R.drawable.greek_salad.toString(),
        shortDescription = "Fresh and healthy salad",
        ingredients = "Cucumbers, Tomatoes, Feta cheese, Olives, Olive oil",
        instructions = "Chop ingredients and mix with the olive oil dressing.",
        category = "Lunch"
    )

    // Setting up the database and dao before each test
    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        recipeDao = db.recipeDao()
    }

    // Closing the database after each test
    @After
    @Throws(IOException::class)
    fun closeDb() = db.close()

    // Test to check if the database can insert and retrieve recipes correctly
    // asserting true if the size of the list of recipes is 3 and the titles of all 3 recipes now exist in the database
    @Test
    @Throws(Exception::class)
    fun insertAndRetrieve() = runBlocking {
        recipeDao.insertRecipe(pancakes)
        recipeDao.insertRecipe(lasagna)
        recipeDao.insertRecipe(greekSalad)

        val recipes = recipeDao.getAllRecipes()
        assertTrue(recipes.size == 3)
        assertTrue(recipes.any { it.title == "Pancakes" })
        assertTrue(recipes.any { it.title == "Lasagna" })
        assertTrue(recipes.any { it.title == "Greek Salad" })
    }

    // Test to check if the database can update a recipe's title correctly in the database
    // asserting true if the updated title exists in the list of recipes and false if the old title does not exist
    @Test
    fun updateRecipe() = runBlocking {
        recipeDao.insertRecipe(lasagna)
        val recipeToUpdate = recipeDao.getAllRecipes().first { it.title == "Lasagna" }
        val updated = recipeToUpdate.copy(title = "Better Lasagna")
        recipeDao.updateRecipe(updated)

        val recipes = recipeDao.getAllRecipes()
        assertTrue(recipes.any { it.title == "Better Lasagna" })
        assertFalse(recipes.any { it.title == "Lasagna" })
    }

    // Test to check if a recipe can be deleted correctly from the database
    // asserting false if the size of the list of recipes decreases by 1 (indicating a successful deletion)
    @Test
    @Throws(Exception::class)
    fun deleteRecipe() = runBlocking {
        recipeDao.insertRecipe(greekSalad)
        val recipeToDelete = recipeDao.getAllRecipes().first { it.title == "Greek Salad" }
        recipeDao.deleteRecipe(recipeToDelete)

        val recipes = recipeDao.getAllRecipes()
        assertFalse(recipes.any { it.title == "Greek Salad" })
    }
}


