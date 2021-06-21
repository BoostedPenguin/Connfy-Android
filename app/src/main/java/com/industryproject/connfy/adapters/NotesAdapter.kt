package com.industryproject.connfy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.industryproject.connfy.Note
import com.industryproject.connfy.R


class NotesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onVoiceNotePlayClickListener: NotesAdapter.OnVoiceNotePlayClick? = null

    // Replace with data model
    private var notes: MutableList<Note> = mutableListOf()


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            1 -> {
                // IF VOICE
                val vh1: NotesAdapter.VoiceNoteHolder = holder as VoiceNoteHolder
                vh1.note = notes[position]

                vh1.title.text = notes[position].name
                vh1.body.text = notes[position].body
            }
            0 -> {
                // IF TEXT
                val vh2: NotesAdapter.NotesHolder = holder as NotesHolder
                vh2.note = notes[position]

                vh2.title.text = notes[position].name
                vh2.body.text = notes[position].body
            }
        }
    }

    fun setNotes(notes: MutableList<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            0 -> {
                // If text
                val v1: View =
                    inflater.inflate(R.layout.notes_item, parent, false)
                viewHolder = NotesHolder(v1)
            }
            1 -> {
                // If voice
                val v2: View =
                    inflater.inflate(R.layout.voice_notes_item, parent, false)
                viewHolder = VoiceNoteHolder(v2)
            }
        }

        return viewHolder;
    }

    override fun getItemViewType(position: Int): Int {
        // If note is voice 1, note is text 0
        return if(notes[position].isVoice) 1 else 0
    }

    override fun getItemCount(): Int {
        return notes.size
    }


    inner class NotesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val playNoteButton: MaterialButton = itemView.findViewById(R.id.cardTextNotePlayIcon)
        internal lateinit var note: Note;
        internal val title: TextView = itemView.findViewById(R.id.cardTextNoteName)
        internal val body: TextView = itemView.findViewById(R.id.cardTextNoteBody)

        init {
            playNoteButton.setOnClickListener {
                val position = adapterPosition

                if (onVoiceNotePlayClickListener != null && position != RecyclerView.NO_POSITION) {
                    onVoiceNotePlayClickListener!!.onVoiceNotePlayClick(position, notes[adapterPosition])
                }
            }
        }
    }

    inner class VoiceNoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal lateinit var note: Note;
        internal val title: TextView = itemView.findViewById(R.id.cardVoiceNoteName)
        internal val body: TextView = itemView.findViewById(R.id.cardVoiceNoteDate)
        internal val playNoteButton: MaterialButton = itemView.findViewById(R.id.cardVoiceNotePlayIcon)
        init {
            playNoteButton.setOnClickListener {
                val position = adapterPosition

                if (onVoiceNotePlayClickListener != null && position != RecyclerView.NO_POSITION) {
                    onVoiceNotePlayClickListener!!.onVoiceNotePlayClick(position, notes[adapterPosition])
                }
            }
        }
    }

    interface OnVoiceNotePlayClick {
        fun onVoiceNotePlayClick(position: Int, meeting: Note)
    }

    fun setOnVoiceNotePlayClickListener(listener: OnVoiceNotePlayClick?) {
        this.onVoiceNotePlayClickListener = listener
    }
}