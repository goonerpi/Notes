package com.coolapps.goonerpi.notes

import android.app.Application
import android.content.Context
import com.coolapps.goonerpi.notes.data.AppDatabase
import com.coolapps.goonerpi.notes.data.NotesRepository

class App : Application() {

    companion object {
        private var instance: App? = null

        lateinit var database: AppDatabase

        val repository: NotesRepository by lazy { NotesRepository.getInstance() }

        fun getInstance(): App? {
            return instance
        }

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }


    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(applicationContext)
        instance = this
    }
}