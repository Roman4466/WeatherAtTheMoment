package com.example.weatherNow.features

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.weatherNow.R
import com.example.weatherNow.databinding.ActivityMainBinding
import com.example.weatherNow.features.history.HistoryFragment
import com.example.weatherNow.features.map.MapsFragment
import com.example.weatherNow.features.search.SearchFragment
import com.example.weatherNow.model.Repository
import com.example.weatherNow.App
import com.google.android.material.navigation.NavigationView

class MainActivity :
    AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // add a drawerMenu to switch between the request and the search history
        val toolbar = initToolbar()
        initDrawer(toolbar)

        Repository.initProvider(App.instance.db.weatherDao)

        // if this is the first entry into the application,
        // then I show a fragment with a request
        if (savedInstanceState == null)
            replaceFragment(SearchFragment())
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

    private fun initDrawer(toolbar: Toolbar) {
        val drawer = binding?.drawerLayout
        val navigationView = binding?.navView
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer?.addDrawerListener(toggle)
        toggle.syncState()
        navigationView?.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.nav_home -> {
                replaceFragment(SearchFragment())
            }
            R.id.nav_search -> {
                val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

                replaceFragment(HistoryFragment())
            }
            R.id.nav_gps -> {
                replaceFragment(MapsFragment())
            }
        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun initToolbar(): Toolbar {
        val toolbar = binding?.appBarMain?.toolbar
        setSupportActionBar(toolbar)
        return toolbar!!
    }

    override fun onBackPressed() {
        val drawer = binding?.drawerLayout as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}