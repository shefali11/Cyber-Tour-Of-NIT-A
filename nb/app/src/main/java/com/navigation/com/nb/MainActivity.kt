package com.navigation.com.nb

//import android.app.Fragment
//import android.app.FragmentManager
import android.net.Uri
import android.os.Bundle
//import android.support.v4.app.Fragment
//import android.support.v4.app.FragmentManager
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.support.v4.view.ViewPager
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.widget.Toolbar
import com.navigation.com.nb.R.styleable.Toolbar

//import android.support.v4.app.FragmentPagerAdapter


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener,Tab1.OnFragmentInteractionListener,Tab2.OnFragmentInteractionListener,Tab3.OnFragmentInteractionListener,Tab4.OnFragmentInteractionListener,Tab5.OnFragmentInteractionListener,Tab6.OnFragmentInteractionListener {
//    var viewPager: ViewPager? = null
//    var customSwip: gallery_custom_swipe? = null

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val tabLayout = findViewById<View>(R.id.tabLayout) as TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Departments(7)"))
        tabLayout.addTab(tabLayout.newTab().setText("Academic(4)"))
        tabLayout.addTab(tabLayout.newTab().setText("Hostels(5)"))
        tabLayout.addTab(tabLayout.newTab().setText("Canteens(8)"))

        tabLayout.addTab(tabLayout.newTab().setText("Banks & ATM(5)"))
        tabLayout.addTab(tabLayout.newTab().setText("Others(12)"))

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val viewPager = findViewById<View>(R.id.pager) as ViewPager
        val adapter = PagerAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.setOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))



        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        setSupportActionBar(toolbar)
//        if (getSupportActionBar() != null)
//        { getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar()!!.setDisplayShowHomeEnabled(true);
//        }
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)


    }

    override fun onBackPressed() {

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)

        } else {
            super.onBackPressed();
        }
    }
    //
//
//    onBackPressed}

//     fun onBackPressed(webView: WebView) {
//        if(webView.canGoBack()) {
//            webView.goBack()
//        }
//        else {
//            super.onBackPressed();
//        }
//
//
//    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here


        when (item.itemId) {
            R.id.nav_home -> {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
            }
            R.id.nav_camera -> {
                // Handle the camera action
                val i = Intent(this, MapsActivity1::class.java)
                startActivity(i)
            }
            R.id.streetview -> {
                val i = Intent(this, street_view::class.java)
                startActivity(i)


            }
            R.id.nav_slideshow -> {

                startActivity(Intent(this, MapsActivityGeo_shortest::class.java))
            }
            R.id.nav_map -> {

                val i = Intent(this, MapsActivity::class.java)
                startActivity(i)

            }
            R.id.abt_nita ->{
//                val viewIntent = Intent("android.intent.action.VIEW",
//                        Uri.parse("http://www.nita.ac.in/"))
//                startActivity(viewIntent)
//                val webView: WebView
//                setContentView(R.layout.activity_webview_layout)
//                webView = findViewById<View>(R.id.help_webView1) as WebView
//                webView.settings.javaScriptEnabled = true
//                webView.loadUrl("http://www.nita.ac.in/")

                val i = Intent(this, Webview_layout::class.java)
                startActivity(i)


            }
            R.id.contact -> {
                val i=Intent(this, contact::class.java)
                startActivity(i)


            }
//            R.id.nav_send -> {
//
//            }


        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
