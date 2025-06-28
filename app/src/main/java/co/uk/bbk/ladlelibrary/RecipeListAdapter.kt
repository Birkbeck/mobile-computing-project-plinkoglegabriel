package co.uk.bbk.ladlelibrary

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.uk.bbk.ladlelibrary.databinding.HeaderItemViewBinding
import co.uk.bbk.ladlelibrary.databinding.RecipeItemViewBinding

// enum class to hold the view types
enum class ITEM_VIEW_TYPE {
    HEADER, RECIPE
}

// Data class to represent the two components of a recipe list item
sealed class RecipeListItem {
    data class Header(val category: String) : RecipeListItem()
    data class Recipe(val item: RecipeItem) : RecipeListItem()
}

// class to adapt the recipe list for the RecyclerView
class RecipeListAdapter(var data: List<RecipeListItem>, val onRecipeClick: (RecipeItem) -> Unit, val onEditClick: (RecipeItem) -> Unit, val onDeleteClick: (RecipeItem) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // classes to hold the two view types
    class HeaderViewHolder(val binding: HeaderItemViewBinding) : RecyclerView.ViewHolder(binding.root)
    class MyViewHolder(val binding: RecipeItemViewBinding) : RecyclerView.ViewHolder(binding.root)


    // override the getItemViewType method to get the correct view type based on the data
    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is RecipeListItem.Header -> ITEM_VIEW_TYPE.HEADER.ordinal
            is RecipeListItem.Recipe -> ITEM_VIEW_TYPE.RECIPE.ordinal
        }
    }

    // override the onCreateViewHolder method to inflate the correct layout based on the view type
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(parent.context)
        return when (viewType) {
            // if the view type is header then inflate the header
            ITEM_VIEW_TYPE.HEADER.ordinal -> {
                val binding = HeaderItemViewBinding.inflate(layout, parent, false)
                HeaderViewHolder(binding)
            }
            // if the view type is recipe then inflate the recipe
            ITEM_VIEW_TYPE.RECIPE.ordinal -> {
                val binding = RecipeItemViewBinding.inflate(layout, parent, false)
                MyViewHolder(binding)
            } else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }

    //  override the onBindViewHolder method to bind the data to the views
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = data[position]) {
            // if the item is a header then the category name is set
            is RecipeListItem.Header -> {
                val headerHolder = holder as HeaderViewHolder
                headerHolder.binding.categoryName.text = item.category
            }
            // if the item is a recipe then the recipe details are set
            is RecipeListItem.Recipe -> {
                val recipe = item.item
                val holder = holder as MyViewHolder
                holder.binding.recipeTitleItem.text = recipe.title
                holder.binding.recipeDescriptionItem.text = recipe.shortDescription
                holder.binding.recipePhotoItem.setBackgroundResource(recipe.imageResId)
                // set the click listeners for the buttons in the recipe item
                holder.binding.recipeTitleItem.setOnClickListener {
                    onRecipeClick(recipe)
                }
                holder.binding.editButton.setOnClickListener {
                    onEditClick(recipe)
                }
                holder.binding.deleteButtonItem.setOnClickListener {
                    onDeleteClick(recipe)
                }
            }
        }
    }

    // function to update the list of recipes
    fun updateRecipes(newRecipes: List<RecipeItem>) {
        data = groupRecipesByCategory(newRecipes)
        notifyDataSetChanged()
    }

    // function to group recipes in the recyclerView by category
    fun groupRecipesByCategory(recipes: List<RecipeItem>): List<RecipeListItem> {
        return recipes
            .groupBy { it.category }
            .flatMap { (category, items) ->
                listOf(RecipeListItem.Header(category)) +
                        items.map { RecipeListItem.Recipe(it) }
            }
    }
    // override the getItemCount method to return the size of the data
    override fun getItemCount(): Int = data.size
}
