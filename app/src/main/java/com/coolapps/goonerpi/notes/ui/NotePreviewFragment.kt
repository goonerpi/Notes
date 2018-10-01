package com.coolapps.goonerpi.notes.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.coolapps.goonerpi.notes.R
import com.coolapps.goonerpi.notes.utilities.Importance
import com.coolapps.goonerpi.notes.viewmodels.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_note_preview.view.*
import org.jetbrains.anko.backgroundResource

class NotePreviewFragment : Fragment() {


    private lateinit var viewModel: NoteViewModel
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_note_preview, container, false)
        viewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        navController = findNavController()
        activity?.title = "Просмотр"
        val id = arguments?.getString("uuid")

        rootView.apply {
            note_preview_head.movementMethod = ScrollingMovementMethod()
            note_preview_text.movementMethod = ScrollingMovementMethod()
        }

        viewModel.notes.observe(this, Observer {
            val note = viewModel.notes.value?.find { it.id == id }
            val position = viewModel.notes.value?.indexOf(note)
            with(rootView) {
                note_preview_head.text = position?.let {
                    viewModel.notes.value?.get(it)?.title
                }
                note_preview_text.text = position?.let {
                    viewModel.notes.value?.get(it)?.body
                }

                note?.let {
                    note_preview_divider.backgroundResource = if (note.importance == Importance.DEFAULT)
                        R.color.colorBackgroundItem
                    else
                        note.importance.colorRes
                }


                val noteId = bundleOf("uuid" to id)

                note_preview_editButton.setOnClickListener {
                    navController.navigate(R.id.action_notePreviewFragment_to_noteEditFragment, noteId)
                }
                note_preview_deleteButton.setOnClickListener {
                    note?.let(viewModel::delete)
                    navController.navigate(R.id.action_global_NotesListFragment)
                    Snackbar.make(rootView, getString(R.string.note_deleted), Snackbar.LENGTH_SHORT).show()
                }
            }


        })

        return rootView
    }


}
