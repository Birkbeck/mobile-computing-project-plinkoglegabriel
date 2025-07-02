package co.uk.bbk.ladlelibrary

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


// This is the database class for the Recipes app (extends RoomDatabase and allows access to the database)
@Database(entities = [RecipeItem::class], version = 1)
abstract class RecipesDatabase : RoomDatabase() {
    // This is the abstract function that returns the RecipesDao interface
    abstract fun recipeDao(): RecipesDao

    // creating an instance of the database
    companion object {
        @Volatile
        private var INSTANCE: RecipesDatabase? = null
        fun getInstance(context: Context): RecipesDatabase {
            Log.i("BBK-LOG", "Created RecipesDatabase instance")
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                   RecipesDatabase::class.java,
                    "recipes_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

}