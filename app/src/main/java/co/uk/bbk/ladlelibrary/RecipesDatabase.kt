package co.uk.bbk.ladlelibrary

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

    @Database(entities = [RecipeItem::class], version = 1)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipesDao

    companion object {
        @Volatile
        private var INSTANCE: RecipesDatabase? = null
        fun getInstance(context: Context): RecipesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                   RecipesDatabase::class.java,
                    "expenses_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

}