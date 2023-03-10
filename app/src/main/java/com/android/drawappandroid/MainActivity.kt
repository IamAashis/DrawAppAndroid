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
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import java.io.File
import java.io.FileOutputStream
import java.util.*


class MainActivity : AppCompatActivity() {

    private var imageView: ImageView? = null
    private var imvEraser: ImageView? = null
    private var submitButton: Button? = null
    private var rcvColor: RecyclerView? = null
    private var sbrSeekBar: SeekBar? = null
    private var imbColorPicker: ImageButton? = null
    private var vewPen: View? = null
    private var vewColorSelected: View? = null
    private var vewEarser: View? = null
    private var floatStartX = -1f
    private var floatStartY: Float = -1f
    private var floatEndX = -1f
    private var floatEndY: Float = -1f
    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private val paint: Paint = Paint()
    lateinit var colorAdapter: ColorAdapter
    var colorList = mutableListOf<ColorData>()
    var selectedItem = "imbPen"

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
        rcvColor = findViewById(R.id.rcvColor)
        imvEraser = findViewById(R.id.imvEraser)
        sbrSeekBar = findViewById(R.id.sbrSeekBar)
        imbColorPicker = findViewById(R.id.imbColorPicker)
        vewPen = findViewById(R.id.vewPen)
        vewColorSelected = findViewById(R.id.vewColorSelected)
        vewEarser = findViewById(R.id.vewEarser)
        submitButton?.setOnClickListener { buttonSaveImage(submitButton) }
        imvEraser?.setOnClickListener {
            selectedItem = "eraser"
            paint.color = Color.WHITE
            drawLine()
        }
        initRecyclerView()
        addColorList()
        itemSelected()
    }

    private fun itemSelected() {
        when (selectedItem) {
            "imbPen" -> {
                rcvColor?.visibility = View.GONE
                sbrSeekBar?.visibility = View.VISIBLE
                vewPen?.visibility = View.VISIBLE
                vewColorSelected?.visibility = View.INVISIBLE
                vewEarser?.visibility = View.INVISIBLE
            }
            "eraser" -> {
                rcvColor?.visibility = View.GONE
                sbrSeekBar?.visibility = View.VISIBLE
                vewPen?.visibility = View.INVISIBLE
                vewColorSelected?.visibility = View.INVISIBLE
                vewEarser?.visibility = View.VISIBLE
            }
            "colorPicker" -> {
                rcvColor?.visibility = View.VISIBLE
                sbrSeekBar?.visibility = View.GONE
                vewPen?.visibility = View.INVISIBLE
                vewColorSelected?.visibility = View.VISIBLE
                vewEarser?.visibility = View.INVISIBLE
            }
        }

    }

    private fun addColorList() {
        colorList.addAll(
            mutableListOf(
                ColorData(1, "#D40FF9"), ColorData(2, "#47986B"),
                ColorData(2, "#6768AB"), ColorData(2, "#A1C7CB"),
                ColorData(2, "#5F4342"), ColorData(2, "#EE9D32")
            )
        )
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rcvColor?.layoutManager = layoutManager
        colorAdapter = ColorAdapter(colorList) {

        }
        rcvColor?.layoutManager = layoutManager
        rcvColor?.adapter = colorAdapter
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
            paint.strokeWidth = 18f
        }
        drawLine()
        imageView?.setImageBitmap(bitmap)
    }

    private fun drawLine() {
        canvas?.drawLine(
            floatStartX,
            floatStartY - 220,
            floatEndX,
            floatEndY - 220,
            paint
        )
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