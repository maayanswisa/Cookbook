package com.example.foodbook

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File

object FileHelper {

    //The saveAddressToFile function saves the file to our Android app's private internal storage directory.
    fun saveAddressToFile(context: Context, address: String) {
        val fileName = "user_address05.txt"
        // If the file exists, it is overwritten.
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
            output.write(address.toByteArray())
        }
    }

    suspend fun readAddressFromFile(context: Context): String? = withContext(Dispatchers.IO) {
        val fileName = "user_address05.txt"
        val file = File(context.filesDir, fileName)
        if (!file.exists() || file.length() == 0L) {
            // File does not exist or is empty, so return null
            return@withContext null
        }

        try {
            context.openFileInput(fileName).use { input ->
                input.bufferedReader().use { bufferedReader ->
                    val content = bufferedReader.readText()
                    if (content.isEmpty()) {
                        // If the file is technically not empty but the content is empty
                        return@withContext null
                    }
                    content
                }
            }
        } catch (e: Exception) {

            null
        }
    }



}
