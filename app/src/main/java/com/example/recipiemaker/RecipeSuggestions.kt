package com.example.recipiemaker

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecipeSuggestions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_suggestions)

        val tvRecipeCount = findViewById<TextView>(R.id.tv_recipe_count)
        val rvRecipes = findViewById<RecyclerView>(R.id.rv_recipes)

        val ingredients = intent.getStringArrayExtra("ingredients") ?: emptyArray()
        val prompt = "Suggest five recipes I can make with these ingredients: ${ingredients.joinToString(", ")}"

        MistralAIService.getRecipeSteps(prompt) { response ->
            runOnUiThread {
                if (response != null) {
                    val recipes = response.split("\n").filter { it.isNotBlank() }
                    tvRecipeCount.text = recipes.size.toString()

                    rvRecipes.layoutManager = LinearLayoutManager(this)
                    rvRecipes.adapter = RecipeAdapter(recipes) { selectedRecipe ->
                        val intent = Intent(this, RecipeStepsActivity::class.java)
                        intent.putExtra("RECIPE_DETAILS", selectedRecipe) // Pass full details
                        startActivity(intent)
                    }
                } else {
                    tvRecipeCount.text = "0"
                }
            }
        }
    }
}
