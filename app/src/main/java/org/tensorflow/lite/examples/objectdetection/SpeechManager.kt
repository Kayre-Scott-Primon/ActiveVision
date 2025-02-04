package com.objecdetection.utils

import android.content.Context
import org.tensorflow.lite.examples.objectdetection.TextToSpeechHelper

class SpeechManager(context: Context) {
    private val ttsHelper: TextToSpeechHelper = TextToSpeechHelper(context)

    fun saySomething(message: String) {
        ttsHelper.speak(message)
    }

    fun stopSpeech() {
        ttsHelper.shutdown()
    }
}
