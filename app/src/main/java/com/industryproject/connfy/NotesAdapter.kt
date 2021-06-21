package com.industryproject.connfy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter (
    private val notes : ArrayList<Note>
        ) : RecyclerView.Adapter<NotesAdapter.DataViewHolder>(){
    private var cardClickListener: OnCardClickListener? = null

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var note: Note
        internal var cardVoiceNoteName: TextView = itemView.findViewById(R.id.cardVoiceNoteName)
        internal var cardTextNoteBody: TextView = itemView.findViewById(R.id.cardTextNoteBody)
        private val cardView: CardView = itemView.findViewById(R.id.cardMapMeeting)

        init {
            cardView.setOnClickListener {
                val position = adapterPosition

                if (cardClickListener != null && position != RecyclerView.NO_POSITION) {
                    cardClickListener!!.onCardClick(position, notes[adapterPosition])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            1 -> {
                val v1: View =
                    inflater.inflate(R.layout.meeting_card_item, parent, false)
                viewHolder = DataViewHolder(v1)
            }
            0 -> {
                val v2: View =
                    inflater.inflate(R.layout.meeting_card_map_item, parent, false)
                viewHolder = DataViewHolder(v2)
            }
        }

        return viewHolder;
    }


    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(notes[position])
    interface OnCardClickListener {
        fun onCardClick(position: Int, meeting: MeetingExample)
    }

    fun setOnCardClickListener(listener: OnCardClickListener?) {
        this.cardClickListener = listener
    }


    fun addData(list: List<Note>) {
        notes.addAll(list)
    }

}

inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    lateinit var note: Note
    internal var cardMeetingName: TextView = itemView.findViewById(R.id.cardMapMeetingName)
    internal var cardMeetingAmountPeople: TextView = itemView.findViewById(R.id.cardMapMeetingAmountPeople)
    internal var cardMeetingDate: TextView = itemView.findViewById(R.id.cardMapMeetingDate)
    internal var cardMapMeetingImage: ImageView = itemView.findViewById(R.id.cardMapMeetingImage)
    private val cardView: CardView = itemView.findViewById(R.id.cardMapMeeting)

    init {
        cardView.setOnClickListener {
            val position = adapterPosition

            if (cardClickListener != null && position != RecyclerView.NO_POSITION) {
                cardClickListener!!.onCardClick(position, meetings[adapterPosition])
            }
        }
    }
}
data class Note (
val title : String = ""
)
