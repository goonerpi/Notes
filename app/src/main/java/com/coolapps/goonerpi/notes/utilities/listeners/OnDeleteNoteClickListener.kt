package com.coolapps.goonerpi.notes.utilities.listeners

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.coolapps.goonerpi.notes.R
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton

class OnDeleteNoteClickListener(val fragment: Fragment, val noteId: String?, val navController: NavController, val onDelete: (String) -> Unit) : View.OnClickListener {
    override fun onClick(v: View?) {
        with(fragment) {
            alert("Удалить?") {
                yesButton {
                    noteId?.let { onDelete.invoke(noteId) }
                    navController.navigate(R.id.action_global_NotesListFragment)
                    v?.let { it1 -> Snackbar.make(it1, getString(R.string.note_deleted), Snackbar.LENGTH_SHORT).show() }
                }
                noButton {}
            }.show()
        }

    }
}