package com.coolapps.goonerpi.notes.utilities.listeners

import android.R
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import com.coolapps.goonerpi.notes.utilities.insertImage
import org.jetbrains.anko.backgroundResource

class OnFullscreenImageClickListener(val context: Context, private val photoUri: String?) : View.OnClickListener {
    override fun onClick(v: View?) {
        val nagDialog = Dialog(context, R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        nagDialog.setCancelable(false)
        nagDialog.setContentView(com.coolapps.goonerpi.notes.R.layout.fullsize_image)
        val btnClose = nagDialog.findViewById(com.coolapps.goonerpi.notes.R.id.btnIvClose) as Button
        val ivPreview = nagDialog.findViewById(com.coolapps.goonerpi.notes.R.id.iv_preview_image) as ImageView
        ivPreview.backgroundResource = com.coolapps.goonerpi.notes.R.color.colorTextMain
        insertImage(photoUri, ivPreview)
        btnClose.setOnClickListener {
            nagDialog.dismiss()
        }
        nagDialog.show()

    }

}