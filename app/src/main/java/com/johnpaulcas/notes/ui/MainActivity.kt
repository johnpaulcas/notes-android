package com.johnpaulcas.notes.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johnpaulcas.notes.R
import com.johnpaulcas.notes.adapters.NotesAdapter
import com.johnpaulcas.notes.db.model.Note
import com.johnpaulcas.notes.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NotesAdapter.OnItemClickListener {

    val ADD_NOTE_REQUEST = 1
    val EDIT_NOTE_REQUEST = 2

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.setHasFixedSize(true)

        val notesAdapter = NotesAdapter(this, this)
        rvNotes.adapter = notesAdapter

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.allNotes.observe(this, Observer { notes ->
            notes?.let {
                notesAdapter.submitList(notes)
            }
        })

        fabAdNote.setOnClickListener {
            addNote()
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(notesAdapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT)
            }

        }).attachToRecyclerView(rvNotes)
    }

    private fun addNote() {
        val intent = Intent(this, AddEditNoteActivity::class.java)
        startActivityForResult(intent, ADD_NOTE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.let {
                val title = it.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
                val description = it.getStringExtra(AddEditNoteActivity.EXTRA_DESCIPTION)
                val priority = it.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 0)

                val note = Note(title, description, priority)

                noteViewModel.insert(note)

                Toast.makeText(this@MainActivity, "Note added successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.let {
                val id = it.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)
                if (id != -1) {
                    val title = it.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
                    val description = it.getStringExtra(AddEditNoteActivity.EXTRA_DESCIPTION)
                    val priority = it.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 0)

                    val note = Note(title, description, priority)
                    note.id = id

                    noteViewModel.update(note)

                    Toast.makeText(this@MainActivity, "Note update successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this@MainActivity, "Note can't be updated.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            Toast.makeText(this, "Note not save", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteAllNotes -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(this@MainActivity, "Delete all notes", Toast.LENGTH_SHORT)
                    .show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(note: Note) {
        val editIntent = Intent(this, AddEditNoteActivity::class.java)
        editIntent.putExtra(AddEditNoteActivity.EXTRA_ID, note.id)
        editIntent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.title)
        editIntent.putExtra(AddEditNoteActivity.EXTRA_DESCIPTION, note.description)
        editIntent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.priority)
        startActivityForResult(editIntent, EDIT_NOTE_REQUEST)
    }
}
