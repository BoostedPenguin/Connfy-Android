package com.industryproject.connfy.ui.dashboard_activity

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.industryproject.connfy.R
import com.industryproject.connfy.adapters.ContactRecyclerViewAdapter
import com.industryproject.connfy.models.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var appBarConfigurationRight: AppBarConfiguration
    private lateinit var layout: DrawerLayout
    private lateinit var adapter: ContactRecyclerViewAdapter

    private val model: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        setContentView(R.layout.activity_dashboard)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        setupDrawers()

        // Store http data in viewmodel
        getDataOnStart()

        // Initiate observers
        initObservers()
    }

    private fun initObservers() {
        model.contacts.observe(this, Observer {
            if(it?.data != null) {
                adapter.setContacts(it.data)
            }
        })

        model.thisUser.observe(this, Observer {
            it?.data?.let { it1 ->
                val headerLayout: View = findViewById<NavigationView>(R.id.nav_view).getHeaderView(0)

                val displayName: String = it1.name ?: auth.currentUser?.displayName?: ""
                headerLayout.findViewById<TextView>(R.id.leftDrawerPersonName).text = displayName
                headerLayout.findViewById<TextView>(R.id.leftDrawerPersonEmail).text = auth.currentUser?.email
            }
        })
    }

    private fun getDataOnStart() {
        model.getContacts()
        model.getMainUserInfo()
    }

    private fun setupDrawers() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)


        val navViewRight: NavigationView = findViewById(R.id.nav_view_right)
        val navControllerRight = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfigurationRight = AppBarConfiguration(setOf(
                R.id.nav_home), drawerLayout)
        setupActionBarWithNavController(navControllerRight, appBarConfigurationRight)
        navViewRight.setupWithNavController(navControllerRight)


        layout = findViewById(R.id.drawer_layout)
        layout.addDrawerListener(object : SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                if(layout.isDrawerOpen(Gravity.LEFT)) {
                    layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT)
                }

                if(layout.isDrawerOpen(Gravity.RIGHT)) {
                    layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT )
                }
                super.onDrawerOpened(drawerView)
            }

            override fun onDrawerClosed(drawerView: View) {

                if(!layout.isDrawerOpen(Gravity.LEFT)) {
                    layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT)
                }

                if(!layout.isDrawerOpen(Gravity.RIGHT)) {
                    layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT )
                }
                super.onDrawerClosed(drawerView)
            }
        })

        val navigationDrawer = layout.findViewById<NavigationView>(R.id.nav_view_right)
        val cons = navigationDrawer.getHeaderView(0)
        val recyclerView: RecyclerView = cons.findViewById(R.id.contacts_recycler_view)
        adapter = ContactRecyclerViewAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)


        adapter.setOnContactButtonClickListener(object: ContactRecyclerViewAdapter.OnContactButtonClickListener {
            override fun onContactButtonClick(position: Int, person: User) {
                layout.closeDrawer(Gravity.RIGHT)
                navController.navigateUp()


                val bundle = bundleOf(
                        Pair("PERSON_NAME", person.name),
                        Pair("PERSON_EMAIL", person.email),
                        Pair("PERSON_UID", person.uid)
                )
                findNavController(R.id.nav_host_fragment).navigate(R.id.nav_profile_view, bundle)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_contacts -> {
                layout.openDrawer(Gravity.RIGHT)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {

        when {
            layout.isDrawerOpen(Gravity.LEFT) -> {
                layout.closeDrawer(Gravity.LEFT)
                return
            }
            layout.isDrawerOpen(Gravity.RIGHT) -> {
                layout.closeDrawer(Gravity.RIGHT)
                return
            }
        }

        if (findNavController(R.id.nav_host_fragment).currentDestination?.id == R.id.nav_home) {


            val builder = AlertDialog.Builder(this)
            builder.setMessage("Exit application")
                    .setPositiveButton("Exit"
                    ) { _, _ ->
                        this.finishAffinity();
                        finishAndRemoveTask();
                    }
                    .setNegativeButton("Cancel", null)
            // Create the AlertDialog object and return it
            builder.create()
            builder.show()

            return
        }
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.dashboard, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_sign_out -> {

                val builder = AlertDialog.Builder(this)
                builder.setMessage("Are you sure you wish to sign out of Connfy?")
                        .setPositiveButton("Sign out"
                        ) { _, _ ->
                            setResult(301)
                            finish()
                        }
                        .setNegativeButton("Cancel", null)
                // Create the AlertDialog object and return it
                builder.create()
                builder.show()

            }

//            R.id.nav_fitness_tracker -> {
//                //model.getMeetings()
//                model.updateMeeting("9GHGOcViNC2XugFixIbf");
//                //model.updateMeeting("Mts2gIncb7OsAzlhKUz3");
//            }
        }
        return true
    }
}