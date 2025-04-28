# Describe
Project to verify the viability of a project for the identify object by device camera

# Motivation
Conclude course work of MBA on Software engineer by USP|ESALQ

# Main codes
- Convertion camera image to bitmap
  ```javascript 
  imageAnalyzer =
  ImageAnalysis.Builder()
      .setTargetAspectRatio(AspectRatio.RATIO_4_3)
      .setTargetRotation(fragmentCameraBinding.viewFinder.display.rotation)
      .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
      .setOutputImageFormat(OUTPUT_IMAGE_FORMAT_RGBA_8888)
      .build()
      .also {
          it.setAnalyzer(cameraExecutor) { image ->
              if (!::bitmapBuffer.isInitialized) {
                  bitmapBuffer = Bitmap.createBitmap(
                      image.width,
                      image.height,
                      Bitmap.Config.ARGB_8888
                  )
              }
        }
  }
  ```
- Image detected
    ```javascript 
  imageAnalyzer =
     fun detect(image: Bitmap, imageRotation: Int) {
        if (objectDetector == null) {
            setupObjectDetector() // Garante que o detector esta pronto
        }
        var inferenceTime = SystemClock.uptimeMillis() // inicio do timer
        val imageProcessor =
            ImageProcessor.Builder()
                .add(Rot90Op(-imageRotation / 90)) // Corrige rotação
                .build()
        // Prepara a imagem
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(image)) 
        val results = objectDetector?.detect(tensorImage) // Faz a inferencia
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime // Tempo
        objectDetectorListener?.onResults(
            results,
            inferenceTime,
            tensorImage.height,
            tensorImage.width
        )
    }
  ```
- Result
  ```javascript 
      override fun draw(canvas: Canvas) {
        super.draw(canvas)
        for (result in results) {
            val boundingBox = result.boundingBox
            val bottom = boundingBox.bottom * scaleFactor
            val top = boundingBox.top * scaleFactor
            val left = boundingBox.left * scaleFactor
            val drawableRect = RectF(left, top, right, bottom)
            val right = boundingBox.right * scaleFactor
            canvas.drawRect(drawableRect, boxPaint)
            val drawableText = result.categories[0].label + " " +
                String.format("%.2f", result.categories[0].score)
            textBackgroundPaint.getTextBounds(drawableText, 0, drawableText.length, bounds)
            val textWidth = bounds.width()
            val textHeight = bounds.height()
            canvas.drawRect(
                left,
                top,
                left + textWidth + Companion.BOUNDING_RECT_TEXT_PADDING,
                top + textHeight + Companion.BOUNDING_RECT_TEXT_PADDING,
                textBackgroundPaint
            )
            canvas.drawText(drawableText, left, top + bounds.height(), textPaint)
        }
    }
  ```
- Speach word/result
  ```javascript 
      fun speak(text: String) {
        if (isInitialized) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }
  ```

## based/helper/source
- https://github.com/tensorflow/tensorflow
- https://www.tensorflow.org
