package com.industryproject.connfy.ui.meeting_view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.industryproject.connfy.R
import com.industryproject.connfy.adapters.ContactRecyclerViewAdapter
import com.industryproject.connfy.adapters.MeetingUsersAdapter
import com.industryproject.connfy.models.User
import com.industryproject.connfy.ui.dashboard_activity.DashboardViewModel


class MeetingViewFragment : Fragment() {

    private var locationPermissionGranted: Boolean = false
    private lateinit var contactsAdapter: MeetingUsersAdapter

    private val model: DashboardViewModel by activityViewModels()


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        if (locationPermissionGranted) {
            googleMap.isMyLocationEnabled = true
        }
        val eindhoven = LatLng(51.441642, 5.4697225)
        googleMap.addMarker(MarkerOptions().position(eindhoven).title("Marker in Eindhoven"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eindhoven, 14f))
    }

    private fun initUserRecyclerView() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.createMeetingUsersRecyclerView)
        contactsAdapter = MeetingUsersAdapter()

        recyclerView.adapter = contactsAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Add invited users to this method :)
        val users = mutableListOf<User>(
                User("uid", "Some name", "Some email", false),
                User("uid2", "Some name2", "Some email2", false),
                User("uid3", "Some name3", "Some email3", false),
                User("uid4", "Some name4", "Some email4", false),
                User("uid", "Some name", "Some email", false),
                User("uid2", "Some name2", "Some email2", false),
                User("uid3", "Some name3", "Some email3", false),
                User("uid4", "Some name4", "Some email4", false),
        )
        contactsAdapter.setInvitedUsers(users)

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context,
                LinearLayoutManager(context).orientation)

        recyclerView.addItemDecoration(dividerItemDecoration)

        contactsAdapter.setOnContactButtonClickListener(object: MeetingUsersAdapter.OnContactButtonClickListener {
            override fun onContactButtonClick(position: Int, person: User) {
                TODO("Not yet implemented")
            }
        })
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.meeting_view_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkLocationPermissions()
        //google map initialization
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        // Invited Users adapter
        initUserRecyclerView()

    }

    private fun checkLocationPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                locationPermissionGranted = true
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            -> {}
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(
                    requireContext(),
                    "Permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}