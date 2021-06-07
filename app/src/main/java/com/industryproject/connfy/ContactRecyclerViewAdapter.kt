package com.industryproject.connfy

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class ContactRecyclerViewAdapter : RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactHolder>() {

    // Replace with data model
    private var contacts: List<UserExample> = ArrayList()
    private var contactButtonClickListener: OnContactButtonClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.contacts_recyclerview_item, parent, false)

        return ContactHolder(itemView)
    }

    fun setContacts(contacts: List<UserExample>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val currentContact = contacts[position]
        holder.contact = currentContact
        holder.contactButton.text = currentContact.name
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    interface OnContactButtonClickListener {
        fun onContactButtonClick(position: Int, person: UserExample)
    }

    fun setOnContactButtonClickListener(listener: OnContactButtonClickListener?) {
        this.contactButtonClickListener = listener
    }

    inner class ContactHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val contactButton: TextView = itemView.findViewById(R.id.buttonContact)
        internal lateinit var contact: UserExample
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