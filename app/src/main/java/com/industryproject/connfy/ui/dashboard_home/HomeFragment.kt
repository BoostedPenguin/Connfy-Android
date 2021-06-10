package com.industryproject.connfy.ui.dashboard_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.industryproject.connfy.adapters.DashboardMeetingsAdapter
import com.industryproject.connfy.MeetingExample
import com.industryproject.connfy.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var meetingAdapter: DashboardMeetingsAdapter

    private var meetings: List<MeetingExample> = listOf(
            MeetingExample("Meeting for budget", 5, "21/04/2014", true),
            MeetingExample("Project owner talk", 3, "11/11/2011", false),
            MeetingExample("Brainstorm with team", 15, "21/04/2066", false),
            MeetingExample("Discuss features", 33, "11/11/2012", true),
            MeetingExample("Meeting for budget", 5, "21/04/2014", true),
            MeetingExample("Project owner talk", 3, "11/11/2011", false),
            MeetingExample("Brainstorm with team", 15, "21/04/2066", false),
            MeetingExample("Discuss features", 33, "11/11/2012", true),
    )


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)



        val meetingRecView = root.findViewById<RecyclerView>(R.id.meetings_recycler_view)
        meetingAdapter = DashboardMeetingsAdapter()

        meetingRecView?.adapter = meetingAdapter
        meetingRecView?.layoutManager = LinearLayoutManager(context)

        meetingAdapter.setOnCardClickListener(object : DashboardMeetingsAdapter.OnCardClickListener {
            override fun onCardClick(position: Int, meeting: MeetingExample) {

                findNavController().navigate(R.id.action_nav_home_to_nav_meeting_view)
            }
        })

        meetingAdapter.setMeetings(meetings)


        root.findViewById<SwitchMaterial>(R.id.meetings_toggle).setOnCheckedChangeListener {
            _, isChecked ->

            if(isChecked) {
                meetingAdapter.setMeetings(meetings)
                meetingAdapter.notifyDataSetChanged()
            }
            else {
                meetingAdapter.setMeetings(meetings.filter { !it.outlook })
                meetingAdapter.notifyDataSetChanged()
            }
        }

        return root
    }
}