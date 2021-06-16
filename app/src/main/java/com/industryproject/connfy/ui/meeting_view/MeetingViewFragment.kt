package com.industryproject.connfy.ui.meeting_view

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.industryproject.connfy.R
import com.industryproject.connfy.adapters.MeetingUsersAdapter
import com.industryproject.connfy.models.GeoLocation
import com.industryproject.connfy.models.User
import com.industryproject.connfy.ui.dashboard_activity.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class MeetingViewFragment : Fragment(), OnMapReadyCallback {

    private var googleApiKey = ""
    private var googleMap: GoogleMap? = null
    private var locationPermissionGranted: Boolean = false
    private lateinit var contactsAdapter: MeetingUsersAdapter

    private val model: DashboardViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        googleApiKey = activity?.resources?.getString(R.string.google_maps_key).toString()
        return inflater.inflate(R.layout.meeting_view_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkLocationPermissions()


        // google map initialization
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)


        // Invited Users adapter
        initUserRecyclerView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get meeting details
        disableOnStart(view)
        model.getMeeting(arguments?.getString("MEETING_ID")!!)

        model.currentMeeting.observe(viewLifecycleOwner, Observer {
            model.currentMeeting.value?.data?.geoLocation
            if (it?.data?.title != null) {
                enableOnReady(requireView())
            }
        })
    }

    private fun disableOnStart(view: View) {
        val s = view.allViews

        s.iterator().forEach {
            if (it.id == R.id.progressBar_cyclic || it.id == R.id.meeting_view_constraint) {
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroy() {
        model.currentMeeting.value = null
        super.onDestroy()
    }

    private fun enableOnReady(view: View) {
        val s = view.allViews

        s.iterator().forEach {
            if (it.id == R.id.progressBar_cyclic) {
                it.visibility = View.GONE
            } else {
                it.visibility = View.VISIBLE
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        model.currentMeeting.observe(viewLifecycleOwner, {
            val geoLocationList = model.currentMeeting.value?.data?.geoLocation!!
            Log.d("GEO LOCATION list", geoLocationList.toString())

            for (cords in geoLocationList) {
                Log.d("GEO LOCATION latitude", cords._latitude.toString())
                Log.d("GEO LOCATION longitude", cords._longitude.toString())
                googleMap!!.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            cords._latitude,
                            cords._longitude
                        )
                    )
                )
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        (LatLng(
                            cords._latitude,
                            cords._longitude
                        )), 14.5f
                    )
                )
            }

            // Request directions only if there are 2 geo points or more in the list
            if (geoLocationList.size > 1) {
                Toast.makeText(context, "Getting directions is not available.", Toast.LENGTH_LONG)
                    .show()

                /* Uncomment this after setting up the billing account

                val path: MutableList<List<LatLng>> = ArrayList()
                val urlDirections = getDirectionsUrl(geoLocationList)

                val directionsRequest = object : StringRequest(
                    Request.Method.GET,
                    urlDirections,
                    Response.Listener<String> { response ->
                        val jsonResponse = JSONObject(response)
                        // Get routes
                        val routes = jsonResponse.getJSONArray("routes")
                        val legs = routes.getJSONObject(0).getJSONArray("legs")
                        val steps = legs.getJSONObject(0).getJSONArray("steps")
                        for (i in 0 until steps.length()) {
                            val points =
                                steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                            path.add(PolyUtil.decode(points))
                        }
                        for (i in 0 until path.size) {
                            this.googleMap!!.addPolyline(
                                PolylineOptions().addAll(path[i]).color(Color.BLUE)
                            )
                        }
                    },
                    Response.ErrorListener { _ ->
                    }) {}
                val requestQueue = Volley.newRequestQueue(requireContext())
                requestQueue.add(directionsRequest)
                */
            }
        })
    }

    private fun getDirectionsUrl(geoLocationPoints: List<GeoLocation>): String {
        // Get the last point's index in the list
        val last = geoLocationPoints.lastIndex

        // Origin of route
        val strOrigin =
            "origin=" + geoLocationPoints[0]._latitude + "," + geoLocationPoints[0]._longitude

        // Destination of route
        val strDest =
            "destination=" + geoLocationPoints[last]._latitude + "," + geoLocationPoints[last]._longitude

        // Get walking directions
        val mode = "mode=walking"

        // Building the parameters to the web service
        var parameters = "$strOrigin&$strDest&$mode"

        // Waypoints
        var waypoints = "waypoints="
        // Add waypoints to parameters only if there are more than 2 geoPoints in the list.
        // Requests using more than 10 waypoints (between 11 and 25) are billed at a higher rate
        if (geoLocationPoints.size in 3..10) {
            for (point in geoLocationPoints) {
                waypoints += "${point._latitude}%2C${point._longitude},"
            }
            // Remove the comma after the last waypoint
            waypoints.dropLast(1)

            parameters += "&$waypoints"
        }

        // Output format
        val output = "json"

        // Api key
        val apiKey = "key=$googleApiKey"

        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&$apiKey"
    }

    private fun showDialog() {
        // TODO
        lateinit var dialog: AlertDialog


        val choices = mutableListOf<String>()
        val checked = mutableListOf<Boolean>()
        for (contact in model.contacts.value?.data!!) {

            choices.add(contact.name ?: "Unknown")

            if (model.currentMeeting.value?.data?.invitedUsers?.any { it -> it.uid == contact.uid } == true) {
                checked.add(true)
            } else {
                checked.add(false)
            }
        }

        val choicesArray: Array<String> = choices.toTypedArray()
        val checkedArray: Array<Boolean> = checked.toTypedArray()

        val builder = AlertDialog.Builder(context)

        builder.setTitle("Choose members")

        builder.setMultiChoiceItems(
            choicesArray,
            checkedArray.toBooleanArray()
        ) { dialog, which, isChecked ->
            checkedArray[which] = isChecked

            val selected = choicesArray[which]
            Toast.makeText(
                context,
                "$selected $isChecked", Toast.LENGTH_SHORT
            ).show();
        }

        builder.setPositiveButton("OK") { _, _ ->
            var queueUsersToAdd = mutableListOf<String>()
            for (i in choicesArray.indices) {
                val checked = checkedArray[i]

                if (checked) {
                    model.currentMeeting.value?.data?.invitedUsers?.get(i)?.uid?.let {
                        queueUsersToAdd.add(
                            it
                        )
                    }
                }
            }
        }

        dialog = builder.create()

        dialog.show()
    }

    private fun initUserRecyclerView() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.meetingUsersRecyclerView)
        contactsAdapter = MeetingUsersAdapter()

        recyclerView.adapter = contactsAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Add invited users to this method :)

        //contactsAdapter.setInvitedUsers(model.)

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayoutManager(context).orientation
        )

        recyclerView.addItemDecoration(dividerItemDecoration)

        contactsAdapter.setOnContactButtonClickListener(object :
            MeetingUsersAdapter.OnContactButtonClickListener {
            override fun onContactButtonClick(position: Int, person: User) {
                person.uid?.let { model.addContact(it) }
                person.uid?.let { contactsAdapter.addContact(it) }
            }
        })

        contactsAdapter.setOnItemClickListener(object : MeetingUsersAdapter.OnItemClickListener {
            override fun onContactButtonClick(position: Int, person: User) {

                val bundle = bundleOf(
                    Pair("PERSON_NAME", person.name),
                    Pair("PERSON_EMAIL", person.email),
                    Pair("PERSON_UID", person.uid)
                )
                findNavController().navigate(R.id.nav_profile_view, bundle)
            }
        })

        requireView().findViewById<Button>(R.id.meeting_add_to_meeting).setOnClickListener {
            //TODO
            //showDialog()
        }

        model.currentMeeting.observe(viewLifecycleOwner, Observer { it ->
            it?.data?.let { it1 ->
                if (it1.invitedUsers != null && it1.ownerName != null && it1.ownerUid != null && model.contacts.value?.data != null && model.thisUser.value?.data != null) {
                    contactsAdapter.setUserContent(
                        it1.invitedUsers,
                        it1.ownerUid,
                        it1.ownerName,
                        model.contacts.value!!.data,
                        model.thisUser.value?.data?.uid!!
                    )
                }
            }
        })
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
            -> {
            }
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