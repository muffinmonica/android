package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RecoverySystem
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng

class CityListActivity : AppCompatActivity() {
    private lateinit var city: City;
    private lateinit var dbHelper: DBHelper;
    private lateinit var cityList: ArrayList<City>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list);
        dbHelper = DBHelper(this);

        val recyclerView = findViewById<RecyclerView>(R.id.city_list);
        recyclerView.layoutManager = LinearLayoutManager(this);

        Thread {
            run {
                cityList = dbHelper.getCityList(this);
            }
            runOnUiThread {
                val adapter = CityListAdapter(cityList, this)
                recyclerView.adapter = adapter;
            }
        }.start()
    }
}