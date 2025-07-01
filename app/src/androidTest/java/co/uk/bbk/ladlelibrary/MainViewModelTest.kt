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
        assertFalse(viewModel.uniqueTitleCheck("Lasagna"))
    }

    // Test to check the uniqueTitleCheck function in view model works to detect a non-unique title
    @Test
    @Throws(Exception::class)
    fun isNotUniqueTitle() = runBlocking {
        recipeDao.insertRecipe(lasagna)

        assertTrue(viewModel.uniqueTitleCheck("Lasagna"))
    }
}
