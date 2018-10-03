package com.coolapps.goonerpi.notes.utilities.listeners

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.coolapps.goonerpi.notes.R
import com.coolapps.goonerpi.notes.data.Note
import com.coolapps.goonerpi.notes.utilities.Importance
import com.coolapps.goonerpi.notes.utilities.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton
import java.util.*

class OnSaveClickListener(
        private val fragment: Fragment,
        private val navController: NavController,
        private val noteId: String?,
        private val head: String,
        private val body: String,
        private val importance: Importance,
        private val photo: String,
        private val coordinates: String,
        private val date: Long,
        private val onInsert: (Note) -> Unit,
        private val onUpdate: (Note) -> Unit
) : View.OnClickListener {

    override fun onClick(v: View?) {

        with(fragment) {

            if (head.isEmpty()) {
                alert("Заполните заголовок!") {
                    yesButton { }
                }.show()
            }
            else {
                if (noteId == null)
                    onInsert(
                            Note(
                                    UUID.randomUUID().toString(),
                                    head,
                                    body,
                                    importance,
                                    photo,
                                    coordinates,
                                    date))
                else
                    onUpdate(
                            Note(
                                    noteId,
                                    head,
                                    body,
                                    importance,
                                    photo,
                                    coordinates,
                                    date
                            )
                    )

                hideKeyboard()
                navController.navigate(R.id.action_global_NotesListFragment)
                v?.let { Snackbar.make(it, getString(R.string.note_saved), Snackbar.LENGTH_SHORT).show() }
            }
        }

    }
}