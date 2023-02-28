package com.android.drawappandroid

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Environment
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileOutputStream
import java.util.*


class MainActivity : AppCompatActivity() {

    private var imageView: ImageView? = null
    private var submitButton: Button? = null
    private var floatStartX = -1f
    private var floatStartY: Float = -1f
    private var floatEndX = -1f
    private var floatEndY: Float = -1f
    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private val paint: Paint = Paint()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setup()
    }

    private fun setup() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
            ),
            PackageManager.PERMISSION_GRANTED
        )
        imageView = findViewById(R.id.imvSketch)
        submitButton = findViewById(R.id.btnSave)
        submitButton?.setOnClickListener { buttonSaveImage(submitButton) }

    }

    private fun drawPaintSketchImage() {
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(
                imageView!!.width,
                imageView!!.height,
                Bitmap.Config.ARGB_8888
            )
            if (bitmap != null) {
                canvas = Canvas(bitmap!!)
            }
            paint.color = Color.RED
            paint.isAntiAlias = true
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 8f
        }
        canvas?.drawLine(
            floatStartX,
            floatStartY - 220,
            floatEndX,
            floatEndY - 220,
            paint
        )
        imageView?.setImageBitmap(bitmap)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            floatStartX = event.x
            floatStartY = event.y
        }
        if (event.action == MotionEvent.ACTION_MOVE) {
            floatEndX = event.x
            floatEndY = event.y
            drawPaintSketchImage()
            floatStartX = event.x
            floatStartY = event.y
        }
        if (event.action == MotionEvent.ACTION_UP) {
            floatEndX = event.x
            floatEndY = event.y
            drawPaintSketchImage()
        }
        return super.onTouchEvent(event)
    }

    fun buttonSaveImage(view: View?) {
        val fileSaveImage = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            Calendar.getInstance().time.toString() + ".jpg"
        )
        try {
            val fileOutputStream = FileOutputStream(fileSaveImage)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            Toast.makeText(
                this,
                "File Saved Successfully",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}