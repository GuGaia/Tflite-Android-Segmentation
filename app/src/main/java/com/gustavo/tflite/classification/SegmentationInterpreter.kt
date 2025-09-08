package com.gustavo.tflite.classification

import android.content.res.AssetManager
import android.graphics.*
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class SegmentationInterpreter(
    assetManager: AssetManager,
    private val modelPath: String = "unet_pet_fp32.tflite",
    private val inputSize: Int = 256,
) {

    private val interpreter: Interpreter

    init {
        interpreter = Interpreter(loadModelFile(assetManager, modelPath),
            Interpreter.Options().apply {
                numThreads = Runtime.getRuntime().availableProcessors().coerceAtMost(4)
            })
    }

    private fun loadModelFile(assetManager: AssetManager, path: String): MappedByteBuffer {
        val fd = assetManager.openFd(path)
        FileInputStream(fd.fileDescriptor).channel.use { ch ->
            return ch.map(FileChannel.MapMode.READ_ONLY, fd.startOffset, fd.length)
        }
    }

    private fun makeInputBuffer(bitmap: Bitmap): ByteBuffer {
        val buf = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3).order(ByteOrder.nativeOrder())
        val scaled = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
        for (y in 0 until inputSize) {
            for (x in 0 until inputSize) {
                val c = scaled.getPixel(x, y)
                buf.putFloat(Color.red(c) / 255f)
                buf.putFloat(Color.green(c) / 255f)
                buf.putFloat(Color.blue(c) / 255f)
            }
        }
        buf.rewind()
        return buf
    }

    fun predictMask(bitmap: Bitmap, threshold: Float = 0.5f): Bitmap {
        val input = makeInputBuffer(bitmap)
        val output = ByteBuffer.allocateDirect(4 * inputSize * inputSize).order(ByteOrder.nativeOrder())
        interpreter.run(input, output)
        output.rewind()

        val mask = Bitmap.createBitmap(inputSize, inputSize, Bitmap.Config.ARGB_8888)

        for (y in 0 until inputSize) {
            for (x in 0 until inputSize) {
                val z = output.getFloat()
                val prob = if (z >= 0f) {
                    val e = kotlin.math.exp(-z)
                    1f / (1f + e)
                } else {
                    val e = kotlin.math.exp(z)
                    e / (1f + e)
                }
                val v = if (prob >= threshold) 255 else 0
                mask.setPixel(x, y, Color.argb(160, v, 0, 0))
            }
        }
        return mask
    }
    fun overlayOn(bitmap: Bitmap, threshold: Float = 0.5f): Bitmap {
        val maskSmall = predictMask(bitmap, threshold)
        val mask = Bitmap.createScaledBitmap(maskSmall, bitmap.width, bitmap.height, true)
        val out = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(out)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        canvas.drawBitmap(mask, 0f, 0f, Paint())
        return out
    }

    fun close() {
        interpreter.close()
    }
}