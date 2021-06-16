package com.industryproject.connfy.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.industryproject.connfy.R
import com.industryproject.connfy.models.FBDate
import com.industryproject.connfy.models.Meeting
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DashboardMeetingsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var cardClickListener: OnCardClickListener? = null
    private var meetings: List<Meeting> = ArrayList()

    @SuppressLint("SimpleDateFormat")
    private val sfd = SimpleDateFormat("dd-MM-yyyy HH:mm")

    fun setMeetings(meetings: List<Meeting>) {
        this.meetings = meetings
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            1 -> {
                val v1: View =
                    inflater.inflate(R.layout.meeting_card_item, parent, false)
                viewHolder = MeetingHolderOutlook(v1)
            }
            0 -> {
                val v2: View =
                    inflater.inflate(R.layout.meeting_card_map_item, parent, false)
                viewHolder = MeetingHolder(v2)
            }
        }

        return viewHolder;
    }

    override fun getItemViewType(position: Int): Int {
        return if(meetings[position].isOutlook == true) 1 else 0
    }

    private fun getDisplayDate(timestamp: FBDate?) : String {
        return if(timestamp == null)
            "Unknown"
        else
            sfd.format(Date(timestamp._seconds * 1000L))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            1 -> {
                val vh1: MeetingHolderOutlook = holder as MeetingHolderOutlook
                vh1.meeting = meetings[position]

                vh1.cardMeetingName.text = meetings[position].title
                vh1.cardMeetingAmountPeople.text = meetings[position].invitedUsersIds?.size.toString()

                vh1.cardMeetingDate.text = getDisplayDate(meetings[position].date)
            }
            0 -> {
                val vh2: MeetingHolder = holder as MeetingHolder
                vh2.meeting = meetings[position]

                vh2.cardMeetingName.text = meetings[position].title
                vh2.cardMeetingAmountPeople.text = meetings[position].invitedUsersIds?.size.toString()
                vh2.cardMeetingDate.text = getDisplayDate(meetings[position].date)
            }
        }
    }

    interface OnCardClickListener {
        fun onCardClick(position: Int, meeting: Meeting)
    }

    fun setOnCardClickListener(listener: OnCardClickListener?) {
        this.cardClickListener = listener
    }

    override fun getItemCount(): Int {
        return meetings.size
    }

    inner class MeetingHolderOutlook(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var meeting: Meeting
        internal var cardMeetingName: TextView = itemView.findViewById(R.id.cardMeetingName)
        internal var cardMeetingAmountPeople: TextView = itemView.findViewById(R.id.cardMeetingAmountPeople)
        internal var cardMeetingDate: TextView = itemView.findViewById(R.id.cardMeetingDate)

        private val cardView: CardView = itemView.findViewById(R.id.cardMeeting)

        init {
            cardView.setOnClickListener {
                val position = adapterPosition

                if (cardClickListener != null && position != RecyclerView.NO_POSITION) {
                    cardClickListener!!.onCardClick(position, meetings[adapterPosition])
                }
            }
        }
    }

    inner class MeetingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var meeting: Meeting
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
}