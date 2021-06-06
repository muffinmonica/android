package com.example.cards

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.MenuItemCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ShareActionProvider
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    private lateinit var shareActionProvider: ShareActionProvider;
    private val listTab = Tab1Fragment();
    private val detailsTab = Tab2Fragment();
    lateinit var pager: ViewPager;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar);
        setSupportActionBar(toolbar);

        val pagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        pager = findViewById<ViewPager>(R.id.pager)
        pager.adapter = pagerAdapter;

        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(pager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu);

        val menuItem = menu!!.findItem(R.id.action_share);
        shareActionProvider = MenuItemCompat.getActionProvider(menuItem) as ShareActionProvider;
        val id = if(listTab.carId == -1) 0 else listTab.carId;
        val str = "${Car.cars[id].name}\nColor is ${Car.cars[id].color}\n${Car.cars[id].details}"
        setShareActionIntent(str);
        return super.onCreateOptionsMenu(menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_action -> {
//                val intent = Intent(this, ActionActivity::class.java);
//                startActivity(intent);
                pager.setCurrentItem(2, true)
                true;
            }
            else -> super.onOptionsItemSelected(item);
        }
    }

    private fun setShareActionIntent(text: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        shareActionProvider.setShareIntent(intent)
    }

    inner class SectionsPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> TopFragment()
            1 -> listTab;
            else -> detailsTab;
        }

        override fun getPageTitle(position: Int): CharSequence? = when (position) {
            0 -> "Start"
            1 -> "Car List"
            2 -> "Car Details"
            else -> null
        }
    }
    fun onClickButton(view: View) {
        if(listTab.carId == -1) return;
        detailsTab.id = listTab.carId;
    }
}