package com.coolapps.goonerpi.notes.ui

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coolapps.goonerpi.notes.R
import com.coolapps.goonerpi.notes.adapters.RecyclerViewAdapter
import com.coolapps.goonerpi.notes.viewmodels.NoteViewModel
import kotlinx.android.synthetic.main.fragment_list.view.*

class NotesListFragment : Fragment() {

    companion object {
        fun newInstance() = NotesListFragment()
    }

    private lateinit var viewModel: NoteViewModel
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)
        activity?.title = "Заметки"


        viewModel = ViewModelProviders.of(this@NotesListFragment).get(NoteViewModel::class.java)
        navController = findNavController()

        rootView.new_note_fab.setOnClickListener { navController.navigate(R.id.action_NotesListFragment_to_noteEditFragment) }
        with(rootView.notes_list) {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecyclerViewAdapter(viewModel.notesPreview, navController, onDelete = viewModel::delete)
        }

        viewModel.notesPreview.observe(this, Observer {
            rootView.notes_list.adapter?.notifyDataSetChanged()
            if (it.isEmpty()) {
                rootView.empty_list_text.visibility = View.VISIBLE
                rootView.notes_list.visibility = View.GONE
            } else {
                rootView.empty_list_text.visibility = View.GONE
                rootView.notes_list.visibility = View.VISIBLE
            }
        })

        return rootView
    }


}
