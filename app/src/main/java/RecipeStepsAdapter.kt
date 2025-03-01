package com.example.recipiemaker

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RecipeStepsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_steps)

        val tvRecipeTitle = findViewById<TextView>(R.id.tvRecipeTitle)
        val tvRecipeDetails = findViewById<TextView>(R.id.tvRecipeDetails)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)

        val recipeDetails = intent.getStringExtra("RECIPE_DETAILS") ?: "No details available"

        val recipeName = recipeDetails.substringBefore(":", recipeDetails) // Handles missing ':'
        val recipeSteps = recipeDetails.substringAfter(":", "No steps provided").trim()

        tvRecipeTitle.text = recipeName
        tvRecipeDetails.text = recipeSteps

        // Back button functionality
        btnBack.setOnClickListener {
            finish()
        }
    }
}
