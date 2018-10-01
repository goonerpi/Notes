package com.coolapps.goonerpi.notes.ui

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.coolapps.goonerpi.notes.App
import com.coolapps.goonerpi.notes.R
import com.coolapps.goonerpi.notes.data.Note
import com.coolapps.goonerpi.notes.utilities.Importance
import com.coolapps.goonerpi.notes.utilities.hideKeyboard
import com.coolapps.goonerpi.notes.viewmodels.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_note_edit.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.support.v4.toast
import java.util.*


class NoteEditFragment : Fragment() {


    private lateinit var viewModel: NoteViewModel
    private lateinit var navController: NavController


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        activity?.title = "Редактор"
        viewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        navController = findNavController()

        val rootView = inflater.inflate(R.layout.fragment_note_edit, container, false)
        val id = arguments?.getString("uuid")

        viewModel.notes.observe(this, Observer {
            val note = viewModel.notes.value?.find { it.id == id }
            var importance = note?.importance ?: Importance.DEFAULT


            with(rootView) {
                note_edit_deleteButton.isEnabled = false
                id?.let {
                    note_edit_head.setText(note?.title)
                    note_edit_body.setText(note?.body)
                    note_edit_deleteButton.isEnabled = true
                    note_edit_importanceButton.imageResource = Importance.DEFAULT.getResource(importance)
                }



                note_edit_saveButton.setOnClickListener {
                    //val uuid = id ?: UUID.randomUUID().toString()
                    if (id == null)
                        viewModel.insert(
                                Note(
                                        UUID.randomUUID().toString(),
                                        note_edit_head.text.toString(),
                                        note_edit_body.text.toString(),
                                        importance,
                                        "http://i.imgur.com/DvpvklR.png",
                                        "",
                                        System.currentTimeMillis()))
                    else
                        if (note != null) {
                            viewModel.update(
                                    Note(
                                            id,
                                            note_edit_head.text.toString(),
                                            note_edit_body.text.toString(),
                                            importance,
                                            "http://i.imgur.com/DvpvklR.png",
                                            "",
                                            note.date
                                    )
                            )
                        }

                    hideKeyboard()
                    navController.navigate(R.id.action_global_NotesListFragment)
                    Snackbar.make(rootView, getString(R.string.note_saved), Snackbar.LENGTH_SHORT).show()


                }

                note_edit_deleteButton.setOnClickListener {
                    note?.let(viewModel::delete)
                    hideKeyboard()
                    navController.navigate(R.id.action_global_NotesListFragment)
                    Snackbar.make(rootView, getString(R.string.note_deleted), Snackbar.LENGTH_SHORT).show()


                }
                note_edit_importanceButton.setOnClickListener {
                    val popupMenu = PopupMenu(App.applicationContext(), it)
                    val inflater = popupMenu.menuInflater
                    inflater.inflate(R.menu.importance_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
                        when (item?.itemId) {
                            R.id.importance_high -> {
                                importance = Importance.HIGH
                                note_edit_importanceButton.imageResource = Importance.DEFAULT.getResource(importance)

                                true
                            }
                            R.id.importance_medium -> {
                                importance = Importance.MEDIUM
                                note_edit_importanceButton.imageResource = Importance.DEFAULT.getResource(importance)
                                true
                            }
                            R.id.importance_low -> {
                                importance = Importance.LOW
                                note_edit_importanceButton.imageResource = Importance.DEFAULT.getResource(importance)
                                true
                            }
                            else -> {
                                importance = Importance.DEFAULT
                                note_edit_importanceButton.imageResource = Importance.DEFAULT.getResource(importance)
                                true
                            }
                        }
                    }
                    popupMenu.show()
                }
            }
        })




        return rootView
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.edit_fragment_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.coordinates_item -> toast("lul")
            R.id.upload_item -> toast("luul")
            R.id.photo_item -> toast("luuul")
        }
        return super.onOptionsItemSelected(item)
    }
}