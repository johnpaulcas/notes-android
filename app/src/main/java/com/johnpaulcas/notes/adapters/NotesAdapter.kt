package com.johnpaulcas.notes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.johnpaulcas.notes.R
import com.johnpaulcas.notes.db.model.Note
import kotlinx.android.synthetic.main.note_item.view.*

/**
 * Created by johnpaulcas on 06/06/2020.
 */
class NotesAdapter : ListAdapter<Note, NotesAdapter.NoteViewHolder> {

    private var noteAdapterListener: NotesAdapter.OnItemClickListener
    private var inflater: LayoutInflater

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>(){
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return (oldItem.title.equals(newItem.title)
                        && oldItem.description.equals(newItem.description)
                        && oldItem.priority == newItem.priority)
            }
        }
    }

    constructor(context: Context, noteAdapterListener: OnItemClickListener): super(DIFF_CALLBACK) {
        inflater = LayoutInflater.from(context)
        this.noteAdapterListener = noteAdapterListener
    }

    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindData(note: Note) {
            itemView.tvTitle.text = note.title
            itemView.tvDescription.text = note.description
        }
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = inflater.inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bindData(note)

        holder.itemView.setOnClickListener {
            noteAdapterListener.onItemClick(note)
        }
    }

    internal fun getNoteAt(position: Int): Note {
        return getItem(position)
    }
}