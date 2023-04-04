package com.android.drawappandroid

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Aashis on 09,March,2023
 */

class ColorAdapter(
    private var colorList: List<ColorData>?, private val onUserClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selected = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_color_list, parent, false)
        return ColorListVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentColorsData = colorList?.get(position)
        val holderListView = holder as ColorListVH
        holderListView.imbColor?.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(currentColorsData?.colorCode))
    }

    override fun getItemCount() = colorList?.count() ?: 0
}