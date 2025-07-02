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


// Test class to test the MainViewModel functionality
@RunWith(AndroidJUnit4::class)
class MainViewModelTest {
    // Initialising the DAO,database and viewModel variables
    private lateinit var recipeDao: RecipesDao
    private lateinit var db: RecipesDatabase
    private lateinit var viewModel: MainViewModel

    // Sample lasagna recipe for testing
    private val lasagna = RecipeItem(
        title = "Lasagna",
        image = R.drawable.lasagna.toString(),
        shortDescription = "Classic Italian pasta dish",
        ingredients = "Lasagna noodles, Tomato sauce, Cheese",
        instructions = "Layer ingredients and bake in the oven until ready.",
        category = "Dinner"
    )

    // Setting up the database and DAO before each test
    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        recipeDao = db.recipeDao()
        viewModel = MainViewModel()
        viewModel.recipesDao = recipeDao
    }

    // Closing the database after each test
    @After
    @Throws(IOException::class)
    fun closeDb() = db.close()

    // Test to check the uniqueTitleCheck function in view model works to detect a unique title
    @Test
    @Throws(Exception::class)
    fun isUniqueTitle() = runBlocking {
        assertFalse(viewModel.uniqueTitleCheck("Lasagna", null))
    }

    // Test to check the uniqueTitleCheck function in view model works to detect a non-unique title
    @Test
    @Throws(Exception::class)
    fun isNotUniqueTitle() = runBlocking {
        recipeDao.insertRecipe(lasagna)
        assertTrue(viewModel.uniqueTitleCheck("Lasagna", null))
    }

    // Test to check if the recipe is added correctly (the view model's LiveData updates properly after the addition)
    @Test
    fun addRecipe() = runBlocking {
        val initialRecipeCount = viewModel.recipes.value?.size ?: 0

        viewModel.addRecipe(
            title = lasagna.title,
            image = lasagna.image,
            shortDescription = lasagna.shortDescription,
            ingredients = lasagna.ingredients,
            instructions = lasagna.instructions,
            category = lasagna.category
        )

        // Waiting for the LiveData to update (450 milliseconds)
        Thread.sleep(450)

        val recipesWithAddition = viewModel.recipes.value ?: emptyList()
        assertTrue(recipesWithAddition.any { it.title == "Lasagna" })
        assertTrue(recipesWithAddition.size == initialRecipeCount + 1)
    }

}
