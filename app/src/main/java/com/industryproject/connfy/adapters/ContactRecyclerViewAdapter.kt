package com.industryproject.connfy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.industryproject.connfy.R
import com.industryproject.connfy.models.User

class ContactRecyclerViewAdapter : RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactHolder>() {

    // Replace with data model
    private var contacts: List<User> = ArrayList()
    private var contactButtonClickListener: OnContactButtonClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.contacts_recyclerview_item, parent, false)

        return ContactHolder(itemView)
    }

    fun setContacts(contacts: List<User>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val currentContact = contacts[position]
        holder.contact = currentContact
        val displayName: String = currentContact.email?: currentContact.name?: ""

        holder.contactButton.text =  displayName
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
        internal val contactButton: TextView = itemView.findViewById(R.id.buttonContact)
        internal lateinit var contact: User
        init {
            //TODO Listeners go here

            contactButton.setOnClickListener {
                val position = adapterPosition

                if (contactButtonClickListener != null && position != RecyclerView.NO_POSITION) {
                    contactButtonClickListener!!.onContactButtonClick(position, contact)
                }
            }
        }
    }

}