package com.sifat.translationapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.sifat.translationapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var translator: Translator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.BENGALI)
            .build()
        translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        // **মডেল ডাউনলোড করো**
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                binding.translateButton.setOnClickListener {
                    val textToTranslate = binding.inputText.text.toString()
                    if (textToTranslate.isNotEmpty()) {
                        translateText(textToTranslate, binding.outputText)
                    } else {
                        binding.outputText.text = "Please enter text to translate"
                    }
                }
            }
            .addOnFailureListener {
                binding.outputText.text = "Model Download Failed"
            }
    }

    private fun translateText(inputText: String, outputText: TextView) {
        translator.translate(inputText)
            .addOnSuccessListener { translatedText ->
                outputText.text = translatedText
            }
            .addOnFailureListener {
                outputText.text = "Translation Failed"
            }
    }
}
