package com.coolapps.goonerpi.notes.adapters

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.coolapps.goonerpi.notes.App
import com.coolapps.goonerpi.notes.R
import com.coolapps.goonerpi.notes.data.NoteShort
import com.coolapps.goonerpi.notes.utilities.insertCircleImage
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.list_item.view.*
import org.jetbrains.anko.backgroundResource

class RecyclerViewAdapter(private val notes: LiveData<List<NoteShort>>, private val navController: NavController, private val onDelete: (String) -> Unit) : RecyclerView.Adapter<RecyclerViewAdapter.NoteHolder>() {

    override fun getItemCount(): Int = notes.value?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.NoteHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return NoteHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.NoteHolder, position: Int) {
        notes.value?.get(position)?.let { holder.bind(it, navController, onDelete) }
    }

    class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(note: NoteShort, navController: NavController, onDelete: (String) -> Unit) {
            with(itemView) {
                list_item_head.text = note.title
                list_item_importance.backgroundResource = note.importance.colorRes

                insertCircleImage(note.photo, list_item_photo)

                val noteId = bundleOf("uuid" to note.id)
                val elevation = 8

                list_item_card.setOnClickListener { navController.navigate(R.id.action_NotesListFragment_to_notePreviewFragment, noteId) }
                list_item_card.setOnLongClickListener {
                    it.elevation += elevation
                    val popupMenu = PopupMenu(App.applicationContext(), it)
                    val inflater = popupMenu.menuInflater
                    inflater.inflate(R.menu.list_item_popup_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
                        when (item?.itemId) {
                            R.id.popup_edit -> {
                                navController.navigate(R.id.action_NotesListFragment_to_noteEditFragment, noteId)
                                true
                            }
                            R.id.popup_delete -> {
                                onDelete.invoke(note.id)
                                Snackbar.make(itemView, App.applicationContext().getString(R.string.note_deleted), Snackbar.LENGTH_SHORT).show()
                                false
                            }
                            else -> {
                                false
                            }

                        }
                    }

                    popupMenu.setOnDismissListener {
                        list_item_card.elevation -= elevation
                    }
                    popupMenu.show()
                    true
                }
            }

        }
    }
}