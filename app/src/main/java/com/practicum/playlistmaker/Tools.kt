package com.practicum.playlistmaker

import android.content.Context
import android.util.TypedValue


object Tools {
        fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics).toInt()
        }

    fun declensions(context: Context, number: Int) : String{
        val result = number % 100
        return  when(result){
            1 -> context.getString(R.string.one_track)
            in 2..4 -> context.getString(R.string.from_2_to_4_tracks)
            else ->  context.getString(R.string.not_from_1_to_4_tracks)
        }

    }
    fun declensionsMinutes(context: Context, number: Int) : String{
        val result = number % 100
        return  when(result){
            1 -> context.getString(R.string.one_minute)
            in 2..4 -> context.getString(R.string.from_2_to_4_minutes)
            else ->  context.getString(R.string.not_from_1_to_4_minutes)
        }

    }
}