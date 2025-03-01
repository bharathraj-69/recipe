package com.example.recipiemaker

import android.content.Context
import android.util.Log
import okhttp3.*
import com.google.gson.*
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

object MistralAIService {
    private var apiKey: String? = null
    private const val API_URL = "https://api.mistral.ai/v1/chat/completions"

    fun initialize(context: Context) {
        val resId = context.resources.getIdentifier("mistral_api_key", "string", context.packageName)
        apiKey = if (resId != 0) context.getString(resId) else null
        Log.d("MistralAIService", "API Key: $apiKey")


    }


    private val client = OkHttpClient()
    private val gson = Gson()

    fun getRecipeSteps(prompt: String, callback: (String?) -> Unit) {
        if (apiKey.isNullOrBlank()) {
            callback("Error: API key is missing. Call MistralAIService.initialize(context) first.")
            return
        }

        val requestBody = gson.toJson(
            mapOf(
                "model" to "mistral-medium",  // Change this based on the model you want to use
                "messages" to listOf(mapOf("role" to "user", "content" to prompt))
            )
        ).toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(API_URL)
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("MistralAIService", "API Response: $responseBody") // Log response

                responseBody?.let {
                    try {
                        val jsonResponse = gson.fromJson(it, JsonObject::class.java)
                        val choices = jsonResponse.getAsJsonArray("choices")
                        val text = choices?.firstOrNull()?.asJsonObject
                            ?.getAsJsonObject("message")
                            ?.get("content")?.asString

                        callback(text ?: "Error: No content received.")
                    } catch (e: Exception) {
                        callback("Error: ${e.message}")
                    }
                } ?: callback("Error: Empty response from API.")
            }
        })
    }
}
