package com.android.drawappandroid

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Aashis on 09,March,2023
 */
class ColorListVH(view: View) : RecyclerView.ViewHolder(view) {
    var imbColor: ImageView? = view.findViewById(R.id.imbColor)
}