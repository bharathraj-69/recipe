package com.example.recipiemaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeAdapter(
    private val recipes: List<String>,
    private val onRecipeClick: (String) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.recipeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_button, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipeText = recipes[position]
        val recipeName = recipeText.substringBefore(":") // Extracting only the name before ':'
        holder.button.text = recipeName

        holder.button.setOnClickListener {
            onRecipeClick(recipeText) // Pass full recipe details
        }
    }

    override fun getItemCount() = recipes.size
}
