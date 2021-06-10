package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng

class CityListActivity : AppCompatActivity() {
    private lateinit var map: Map<String, String>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list);

        Thread {
            run {
                val location = intent.extras!!["coords"] as LatLng;
                map = Response.getLocationMap(location, this@CityListActivity);
            }
            runOnUiThread {
                findViewById<TextView>(R.id.temp_text).text = "${map["temp"]} °C";
                findViewById<TextView>(R.id.wind_speed_text).text = "${map["wind_speed"]} m/s";
                findViewById<TextView>(R.id.location_text).text = "${map["location"]}";

                val id = resources.getIdentifier("flag_${map["country_code"]!!.lowercase()}", "drawable", packageName);
                findViewById<ImageView>(R.id.country_flag).setImageResource(id);
            }
        }.start()
    }
}