package com.coolapps.goonerpi.notes.utilities

import android.view.View
import android.widget.ImageView
import com.coolapps.goonerpi.notes.R
import com.squareup.picasso.Picasso


fun insertCircleImage(source: String?, view: ImageView) {
    when (source) {
        "", null -> {
            view.visibility = View.GONE
        }
        else -> {
            view.visibility = View.VISIBLE
            Picasso.get().load(source).transform(CircleTransformation()).error(R.drawable.ic_sharp_error_24px).into(view)
        }
    }
}

fun insertImage(source: String?, view: ImageView) {
    when (source) {
        "", null -> {
            view.visibility = View.GONE
        }
        else -> {
            view.visibility = View.VISIBLE
            Picasso.get().load(source).error(R.drawable.ic_sharp_error_24px).into(view)
        }
    }
}
