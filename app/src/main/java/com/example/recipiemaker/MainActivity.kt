package com.example.recipiemaker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var editTextIngredients: EditText
    private lateinit var buttonVoiceInput: ImageButton
    private lateinit var buttonFindRecipes: Button

    companion object {
        private const val REQUEST_CODE_VOICE_INPUT = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MistralAIService.initialize(this)

        editTextIngredients = findViewById(R.id.et_ingredients)
        buttonVoiceInput = findViewById(R.id.btn_voice_input)
        buttonFindRecipes = findViewById(R.id.btn_find_recipes)

        buttonVoiceInput.setOnClickListener {
            startVoiceRecognition()
        }

        buttonFindRecipes.setOnClickListener {
            val ingredients = editTextIngredients.text.toString()
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            if (ingredients.isNotEmpty()) {
                Toast.makeText(this, "Searching for recipes...", Toast.LENGTH_SHORT).show()
                // Add intent to move to RecipeSuggestions activity
                val intent = Intent(this, RecipeSuggestions::class.java)
                intent.putExtra("ingredients", ingredients.toTypedArray())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter ingredients", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startVoiceRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say the ingredients")

        try {
            startActivityForResult(intent, REQUEST_CODE_VOICE_INPUT)
        } catch (e: Exception) {
            Toast.makeText(this, "Voice input is not supported on your device", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_VOICE_INPUT && resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!result.isNullOrEmpty()) {
                val spokenText = result[0]
                val existingText = editTextIngredients.text.toString()
                editTextIngredients.setText(
                    if (existingText.isEmpty()) spokenText else "$existingText, $spokenText"
                )
            }
        }
    }
}
