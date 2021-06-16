package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng

class CityListActivity : AppCompatActivity() {
    private lateinit var city: City;
    private lateinit var dbHelper: DBHelper;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list);
        dbHelper = DBHelper(this);

        Thread {
            run {
                val location = intent.extras!!["coords"] as LatLng;
                city = dbHelper.getCity(location, this@CityListActivity);
            }
            runOnUiThread {
                val listElem = findViewById<RelativeLayout>(R.id.city_list);
                listElem.findViewById<TextView>(R.id.wind_speed_text).text = "${city.windSpeed} m/s";
                listElem.findViewById<TextView>(R.id.temp_text).text = "${city.temperature} °C";
                val displayName = if(city.cityName == "") city.getDisplayCoords() else city.cityName;
                listElem.findViewById<TextView>(R.id.location_text).text = "$displayName";

                val id = resources.getIdentifier("flag_${city.countryCode.lowercase()}", "drawable", packageName);
                listElem.findViewById<ImageView>(R.id.country_flag).setImageResource(id);
            }
        }.start()
    }
}