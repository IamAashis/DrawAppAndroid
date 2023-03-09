package com.android.drawappandroid

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Aashis on 09,March,2023
 */
class ColorListVH(view: View) : RecyclerView.ViewHolder(view) {
    var imbColor: TextView? = view.findViewById(R.id.imbColor)
}