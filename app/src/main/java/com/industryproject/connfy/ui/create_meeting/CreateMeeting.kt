package com.industryproject.connfy.ui.create_meeting

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.SupportMapFragment
import com.industryproject.connfy.R
import com.industryproject.connfy.ui.dashboard_activity.DashboardViewModel

class CreateMeeting : Fragment() {
    private val model: DashboardViewModel by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Init event callers
        registerEvents()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.create_meeting, container, false)
    }

    private fun registerEvents() {
        requireView().findViewById<Button>(R.id.create_meeting_add_to_meeting).setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog(){
        // TODO
        lateinit var dialog: AlertDialog


        val choices = mutableListOf<String>()
        val checked = mutableListOf<Boolean>()
        for(contact in model.contacts.value?.data!!) {

            choices.add(contact.name?: "Unknown")

            if(model.creatingMeeting.invitedUsers?.any {it -> it.uid == contact.uid} == true) {
                checked.add(true)
            }
            else {
                checked.add(false)
            }
        }

        val choicesArray: Array<String> = choices.toTypedArray()
        val checkedArray: Array<Boolean> = checked.toTypedArray()

        val builder = AlertDialog.Builder(context)

        builder.setTitle("Choose members")

        builder.setMultiChoiceItems(choicesArray, checkedArray.toBooleanArray() ) { dialog, which, isChecked->
            checkedArray[which] = isChecked

            val selected = choicesArray[which]
            Toast.makeText(context,
                    "$selected $isChecked", Toast.LENGTH_SHORT).show();
        }

        builder.setPositiveButton("OK") { _, _ ->
            var queueUsersToAdd = mutableListOf<String>()
            for (i in choicesArray.indices) {
                val checked = checkedArray[i]

                if (checked) {

                    if(model.contacts.value != null) {
                        model.contacts.value!!.data[i].uid?.let { queueUsersToAdd.add(it) }
                    }
                }
            }
            model.creatingMeeting.invitedUsersIds = queueUsersToAdd
        }

        dialog = builder.create()

        dialog.show()
    }
}