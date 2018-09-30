package com.coolapps.goonerpi.notes.utilities

import androidx.annotation.ColorRes
import com.coolapps.goonerpi.notes.R

enum class Importance(@ColorRes val colorRes: Int) {
    DEFAULT(R.color.colorImportanceDefault),
    LOW(R.color.colorImportanceLow),
    MEDIUM(R.color.colorImportanceMedium),
    HIGH(R.color.colorImportanceHigh);

    fun getResource(importance: Importance?): Int =
            when (importance) {
                Importance.HIGH -> R.drawable.ic_sharp_priority_high_24px
                Importance.MEDIUM -> R.drawable.ic_sharp_priority_medium_24px
                Importance.LOW -> R.drawable.ic_sharp_priority_low_24px
                else -> R.drawable.ic_sharp_priority_default_24px
            }

}