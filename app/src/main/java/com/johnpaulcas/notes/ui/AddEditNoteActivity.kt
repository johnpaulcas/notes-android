package com.johnpaulcas.notes.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.johnpaulcas.notes.R
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.note_item.*

class AddEditNoteActivity : AppCompatActivity() {

    companion object {

        val EXTRA_ID = "id"

        val EXTRA_TITLE = "title"
        val EXTRA_DESCIPTION = "description"
        val EXTRA_PRIORITY = "priority"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        npPriority.minValue = 1
        npPriority.maxValue = 10

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        val dataIntent = intent

        if (dataIntent.hasExtra(EXTRA_ID)) {
            setTitle(R.string.edit_note)
            etTitle.setText(dataIntent.getStringExtra(EXTRA_TITLE))
            etDescription.setText(dataIntent.getStringExtra(EXTRA_DESCIPTION))
            npPriority.value = dataIntent.getIntExtra(EXTRA_PRIORITY, 1)
        } else {
            setTitle(R.string.add_note)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveNote -> {
                saveNote()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()
        val priority = npPriority.value

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Title and Description cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val addEditNoteIntent = Intent()
        addEditNoteIntent.putExtra(EXTRA_TITLE, title)
        addEditNoteIntent.putExtra(EXTRA_DESCIPTION, description)
        addEditNoteIntent.putExtra(EXTRA_PRIORITY, priority)

        val id = intent.getIntExtra(EXTRA_ID, -1)
        if (id != -1) {
            addEditNoteIntent.putExtra(EXTRA_ID, id)
        }

        setResult(Activity.RESULT_OK, addEditNoteIntent)
        finish()
    }
}
