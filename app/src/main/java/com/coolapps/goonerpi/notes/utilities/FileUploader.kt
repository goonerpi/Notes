package com.coolapps.goonerpi.notes.utilities

import android.os.Environment
import java.io.File
import java.io.PrintWriter

class FileUploader {

    companion object {
        fun uploadToFile(filename: String, data: List<String>) {
            val sd_main = File(Environment.getExternalStorageDirectory(), "Notes")
            var success = true
            if (!sd_main.exists()) {
                success = sd_main.mkdir()
            }

            if (success) {
                val dest = File(sd_main, "$filename.txt")
                try {
                    PrintWriter(dest).use { out -> data.forEach { out.println("$it\n\n") } }
                } catch (e: Exception) {
                }

            } else {
            }
        }
    }


}

