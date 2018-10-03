package com.coolapps.goonerpi.notes.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
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
import com.coolapps.goonerpi.notes.utilities.insertCircleImage
import com.coolapps.goonerpi.notes.utilities.listeners.OnDeleteNoteClickListener
import com.coolapps.goonerpi.notes.utilities.listeners.OnFullscreenImageClickListener
import com.coolapps.goonerpi.notes.viewmodels.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_note_edit.*
import kotlinx.android.synthetic.main.fragment_note_edit.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


class NoteEditFragment : Fragment(), EasyPermissions.PermissionCallbacks {


    companion object {
        private const val REQUEST_SELECT_IMAGE_IN_ALBUM = 0
        private const val REQUEST_CODE_READ_EXTERNAL_PERM = 1
    }

    private lateinit var viewModel: NoteViewModel
    private lateinit var navController: NavController
    var note: Note? = null
    private var photo = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        activity?.title = "Редактор"
        viewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        navController = findNavController()

        val rootView = inflater.inflate(R.layout.fragment_note_edit, container, false)
        val id = arguments?.getString("uuid")


        viewModel.notes.observe(this, Observer {
            note = viewModel.notes.value?.find { it.id == id }
            var importance = note?.importance ?: Importance.DEFAULT
            val date = note?.date ?: System.currentTimeMillis()
            photo = note?.photo ?: ""


            with(rootView) {
                note_edit_deleteButton.isEnabled = false
                note_edit_photo.visibility = View.GONE
                id?.let {
                    note_edit_head.setText(note?.title)
                    note_edit_body.setText(note?.body)
                    note_edit_deleteButton.isEnabled = true
                    note_edit_importanceButton.imageResource = Importance.DEFAULT.getResource(importance)
                    insertCircleImage(note?.photo, note_edit_photo)

                }



                note_edit_saveButton.setOnClickListener {
                    //val uuid = id ?: UUID.randomUUID().toString()
                    if (note_edit_head.text.toString().isEmpty())
                        alert("Заполните заголовок!") {
                            yesButton { }
                        }.show()
                    else {
                        if (id == null)
                            viewModel.insert(
                                    Note(
                                            UUID.randomUUID().toString(),
                                            note_edit_head.text.toString(),
                                            note_edit_body.text.toString(),
                                            importance,
                                            photo,
                                            "",
                                            date))
                        else
                            if (note != null) {
                                viewModel.update(
                                        Note(
                                                id,
                                                note_edit_head.text.toString(),
                                                note_edit_body.text.toString(),
                                                importance,
                                                photo,
                                                "",
                                                date
                                        )
                                )
                            }
                        hideKeyboard()
                        navController.navigate(R.id.action_global_NotesListFragment)
                        Snackbar.make(rootView, getString(R.string.note_saved), Snackbar.LENGTH_SHORT).show()
                    }


                }

                note_edit_deleteButton.setOnClickListener(OnDeleteNoteClickListener(this@NoteEditFragment,
                        note?.id,
                        navController
                ) { note?.id?.let { it1 -> viewModel.delete(it1) } }
                )
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
            note_edit_photo.setOnClickListener(context?.let { it1 -> OnFullscreenImageClickListener(it1, note?.photo) })

        })
        return rootView
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.edit_fragment_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.coordinates_item -> toast("ss")
            R.id.photo_item -> selectImage()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun selectImage() {
        context?.let {
            if (EasyPermissions.hasPermissions(it, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(activity?.packageManager)?.also {
                        startActivityForResult(takePictureIntent, REQUEST_SELECT_IMAGE_IN_ALBUM)
                    }
                }
            } else {
                EasyPermissions.requestPermissions(this,
                        "Требуется разрешение на доступ к галерее!",
                        REQUEST_CODE_READ_EXTERNAL_PERM,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
            if (resultCode == RESULT_OK) {
                val selectedImage = imageReturnedIntent?.data
                photo = selectedImage.toString()
                insertCircleImage(photo, note_edit_photo)
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            REQUEST_CODE_READ_EXTERNAL_PERM -> selectImage()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}