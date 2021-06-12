package com.industryproject.connfy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.industryproject.connfy.R
import com.industryproject.connfy.models.User

class MeetingUsersAdapter : RecyclerView.Adapter<MeetingUsersAdapter.ContactHolder>() {

    // Replace with data model
    private var contacts: List<User> = ArrayList()
    private var contactButtonClickListener: OnContactButtonClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.meetings_recylerview_item, parent, false)

        return ContactHolder(itemView)
    }

    fun setInvitedUsers(contacts: List<User>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val currentContact = contacts[position]
        holder.contact = currentContact
        val displayName: String = currentContact.email?: currentContact.name?: ""

        holder.displayUser.text =  displayName
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    interface OnContactButtonClickListener {
        fun onContactButtonClick(position: Int, person: User)
    }

    fun setOnContactButtonClickListener(listener: OnContactButtonClickListener?) {
        this.contactButtonClickListener = listener
    }

    inner class ContactHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val displayUser: TextView = itemView.findViewById(R.id.meeting_user_name)
        internal val addUser: ImageButton = itemView.findViewById(R.id.meeting_add_user)

        internal lateinit var contact: User
        init {
            //TODO Listeners go here

            addUser.setOnClickListener {
                val position = adapterPosition

                if (contactButtonClickListener != null && position != RecyclerView.NO_POSITION) {
                    contactButtonClickListener!!.onContactButtonClick(position, contact)
                }
            }
        }
    }

}