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
    private var contacts: List<String> = listOf("John doe", "Someone else", "Third person")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.contacts_recyclerview_item, parent, false)

        return ContactHolder(itemView)
    }


    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        val currentContact =contacts[position]
        holder.contactButton.text = currentContact
    }

    override fun getItemCount(): Int {
        return contacts.size
    }



    inner class ContactHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val contactButton: TextView = itemView.findViewById(R.id.buttonContact)

        init {
            //TODO Listeners go here
        }
    }

}