package com.industryproject.connfy

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView


class DashboardActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var appBarConfigurationRight: AppBarConfiguration
    private lateinit var layout: DrawerLayout
    private lateinit var adapter: ContactRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


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

        if (findNavController(R.id.nav_host_fragment).currentDestination?.id == R.id.nav_home) {
            Toast.makeText(applicationContext, "Should exit app on back press here", Toast.LENGTH_SHORT).show()
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
}