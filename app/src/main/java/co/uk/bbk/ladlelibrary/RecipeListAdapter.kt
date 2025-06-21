package co.uk.bbk.ladlelibrary

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeListAdapter(val data: List<RecipeItem>, val onRecipeClick: (RecipeItem) -> Unit) : RecyclerView.Adapter<RecipeListAdapter.MyViewHolder>() {

    class MyViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val image = row.findViewById<View>(R.id.recipe_photo_item)
        val title = row.findViewById<TextView>(R.id.recipe_title_item)
        val description = row.findViewById<TextView>(R.id.recipe_description_item)

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
        holder.description.text = data[position].shortDescription

        holder.title.setOnClickListener {
            onRecipeClick(recipe)
        }
    }

    override fun getItemCount(): Int = data.size
}
