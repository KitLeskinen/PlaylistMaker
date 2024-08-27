package com.practicum.playlistmaker

import android.content.Context
import android.util.TypedValue

class Tools {
    companion object{
        fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics).toInt()
        }
    }
}