package co.uk.bbk.ladlelibrary

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeListAdapter(var data: List<RecipeItem>, val onRecipeClick: (RecipeItem) -> Unit, val onEditClick: (RecipeItem) -> Unit, val onDeleteClick: (RecipeItem) -> Unit) : RecyclerView.Adapter<RecipeListAdapter.MyViewHolder>() {
    class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val image = row.findViewById<View>(R.id.recipe_photo_item)
        val title = row.findViewById<TextView>(R.id.recipe_title_item)
        val shortDescription = row.findViewById<TextView>(R.id.recipe_description_item)
        val edit = row.findViewById<View>(R.id.edit_button)
        val delete = row.findViewById<View>(R.id.delete_button_item)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.recipe_item_view,
            parent, false
        )
        return MyViewHolder(layout)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = data[position]
        holder.image.setBackgroundResource(data[position].imageResId)
        holder.title.text = data[position].title
        holder.shortDescription.text = data[position].shortDescription

        holder.title.setOnClickListener {
            onRecipeClick(recipe)
        }
        holder.edit.setOnClickListener {
            onEditClick(recipe)
        }
        holder.delete.setOnClickListener {
            onDeleteClick(recipe)
        }
    }

    fun updateRecipes(newData: List<RecipeItem>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size
}
