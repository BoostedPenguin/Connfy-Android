package com.industryproject.connfy.ui.dashboard_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.industryproject.connfy.adapters.DashboardMeetingsAdapter
import com.industryproject.connfy.R
import com.industryproject.connfy.models.Meeting
import com.industryproject.connfy.ui.dashboard_activity.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val model: DashboardViewModel by viewModels()

    private lateinit var meetingAdapter: DashboardMeetingsAdapter


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val meetingRecView = root.findViewById<RecyclerView>(R.id.meetings_recycler_view)
        meetingAdapter = DashboardMeetingsAdapter()

        meetingRecView?.adapter = meetingAdapter
        meetingRecView?.layoutManager = LinearLayoutManager(context)

        meetingAdapter.setOnCardClickListener(object : DashboardMeetingsAdapter.OnCardClickListener {
            override fun onCardClick(position: Int, meeting: Meeting) {

                val bundle = bundleOf(
                        Pair("MEETING_ID", meeting.uid),
                        Pair("title", meeting.title)
                )

                findNavController().navigate(R.id.action_nav_home_to_nav_meeting_view, bundle)
            }
        })

        model.getMeetings()

        model.meetings.observe(viewLifecycleOwner, Observer {
            setAdapterMeetings()
        })


        root.findViewById<SwitchMaterial>(R.id.meetings_toggle).setOnCheckedChangeListener {
            _, _ ->
            setAdapterMeetings()
        }

        return root
    }

    private fun setAdapterMeetings() {

        val cbx: SwitchMaterial = requireView().findViewById(R.id.meetings_toggle)

        if(cbx.isChecked) {
            model.meetings.value?.data?.let { meetingAdapter.setMeetings(it) }
            meetingAdapter.notifyDataSetChanged()
        }
        else {
            //meetingAdapter.setMeetings(meetings.filter { !it.outlook })
            model.meetings.value?.data?.let { meetingAdapter.setMeetings(it) }
            meetingAdapter.notifyDataSetChanged()
        }
    }
}