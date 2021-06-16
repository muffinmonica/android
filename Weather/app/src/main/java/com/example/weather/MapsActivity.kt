package com.example.weather

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var marker: Marker? = null;
    private lateinit var binding: ActivityMapsBinding
    private var location: LatLng? = null;
    private lateinit var cityInfo: LinearLayout;
    private lateinit var dbHelper: DBHelper;
    private lateinit var city: City;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater);
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this);
        cityInfo = findViewById(R.id.map_city_info);

        dbHelper = DBHelper(this);
        dbHelper.getCityList(this);
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.remove_marker_item -> {
                marker?.remove();
                cityInfo.visibility = View.GONE;
                location = null;
            }
        }
        return super.onOptionsItemSelected(item)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val london = LatLng(51.51, -.11)
//        mMap.addMarker(MarkerOptions().position(london).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(london))
        mMap.setOnMapClickListener {
            location = it;
            marker?.remove();
            marker = mMap.addMarker(MarkerOptions().position(it));

            Thread() {
                run {
                    city = Response.getCity(it, this);
                }
                runOnUiThread {
                    cityInfo.visibility = View.VISIBLE;
                    cityInfo.findViewById<TextView>(R.id.wind_speed_text).text = getString(R.string.wind_speed_res).format(city.windSpeed);
                    cityInfo.findViewById<TextView>(R.id.temp_text).text = "${city.temperature} °C";
                    val displayName = if(city.cityName == "") city.getDisplayCoords() else city.cityName;
                    cityInfo.findViewById<TextView>(R.id.location_text).text = displayName;

                    val id = resources.getIdentifier("flag_${city.countryCode.lowercase()}", "drawable", packageName);
                    cityInfo.findViewById<ImageView>(R.id.country_flag).setImageResource(id);
                }
            }.start()
        }
    }

    fun showList(view: View) {
        location?.let {
            val result = dbHelper.addCity(city);
//            Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();

            val intent = Intent(this, CityListActivity::class.java);
            intent.putExtra("coords", location);
            startActivity(intent);
        } ?: run {
            Toast.makeText(this, "No location chosen!", Toast.LENGTH_SHORT).show();
        }
    }
}