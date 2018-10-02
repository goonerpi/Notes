package com.coolapps.goonerpi.notes.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.coolapps.goonerpi.notes.R
import com.coolapps.goonerpi.notes.data.Note
import com.coolapps.goonerpi.notes.utilities.FileUploader.Companion.uploadToFile
import com.coolapps.goonerpi.notes.utilities.Importance
import com.coolapps.goonerpi.notes.utilities.insertCircleImage
import com.coolapps.goonerpi.notes.viewmodels.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_note_preview.*
import kotlinx.android.synthetic.main.fragment_note_preview.view.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton


class NotePreviewFragment : Fragment() {


    private lateinit var viewModel: NoteViewModel
    private lateinit var navController: NavController
    var note: Note? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
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
            note = viewModel.notes.value?.find { it.id == id }
            val position = viewModel.notes.value?.indexOf(note)
            val importance = note?.importance ?: Importance.DEFAULT
            with(rootView) {
                note_preview_head.text = position?.let {
                    viewModel.notes.value?.get(it)?.title
                }
                note_preview_text.text = position?.let {
                    viewModel.notes.value?.get(it)?.body
                }
                insertCircleImage(note?.photo, note_preview_photo)

                note?.let {
                    note_preview_divider.backgroundResource = if (importance == Importance.DEFAULT)
                        R.color.colorBackgroundItem
                    else
                        importance.colorRes
                }

                val noteId = bundleOf("uuid" to id)

                note_preview_editButton.setOnClickListener {
                    navController.navigate(R.id.action_notePreviewFragment_to_noteEditFragment, noteId)
                }
                note_preview_deleteButton.setOnClickListener {

                    alert("Удалить?") {
                        yesButton {
                            note?.let(viewModel::delete)
                            navController.navigate(R.id.action_global_NotesListFragment)
                            Snackbar.make(rootView, getString(R.string.note_deleted), Snackbar.LENGTH_SHORT).show()
                        }
                        noButton {}
                    }.show()

                }
                note_preview_photo.setOnClickListener {
                }
            }
        })
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.preview_fragment_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.upload_item -> {

                val filename = note_preview_head.text.toString()
                val data = listOf(note_preview_head.text.toString(), note_preview_text.text.toString())
                if (filename == "") {
                    alert("Заполните заголовок заметки"){
                        yesButton { }
                    }.show()
                } else {
                    uploadToFile(filename, data)
                    this.view?.let { Snackbar.make(it, "Заметка сохранена в файл", Snackbar.LENGTH_SHORT).show() }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
