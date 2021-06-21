package com.industryproject.connfy.ui.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.industryproject.connfy.Note
import com.industryproject.connfy.R
import com.industryproject.connfy.adapters.ContactRecyclerViewAdapter
import com.industryproject.connfy.adapters.DashboardMeetingsAdapter
import com.industryproject.connfy.adapters.NotesAdapter
import com.industryproject.connfy.models.Meeting

class NotesFragment : Fragment() {

    private lateinit var notesAdapter: NotesAdapter

    private var notesList = mutableListOf<Note>(
        Note("Fix frontend bug", false, "The button on the left sidebar is off centered", null),
        Note("Voice recording #1", true, "16:34 16/04/2021", "someurl"),
        Note("Implement authentication", false, "Add authentication with 3rd party providers such as google, microsoft and github", null),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_notes, container, false)

        val notesRecView = root.findViewById<RecyclerView>(R.id.notes_recycler)
        notesAdapter = NotesAdapter()

        notesRecView?.adapter = notesAdapter
        notesRecView?.layoutManager = LinearLayoutManager(context)

        notesAdapter.setOnVoiceNotePlayClickListener(object: NotesAdapter.OnVoiceNotePlayClick {
            override fun onVoiceNotePlayClick(position: Int, meeting: Note) {
                TODO("Not yet implemented")
            }
        } )

        notesAdapter.setNotes(notesList)

        return root
    }
}