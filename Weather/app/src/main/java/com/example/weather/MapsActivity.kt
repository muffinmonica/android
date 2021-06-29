package com.example.weather

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weather.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var mMap: GoogleMap
    private var marker: Marker? = null;
    private lateinit var binding: ActivityMapsBinding
    private var location: LatLng? = null;
    private lateinit var cityInfo: LinearLayout;
    private lateinit var dbHelper: DBHelper;
    private lateinit var city: City;
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }

        binding = ActivityMapsBinding.inflate(layoutInflater);
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this);
        cityInfo = findViewById(R.id.map_city_info);

        dbHelper = DBHelper(this);

        val gpsFab = findViewById<FloatingActionButton>(R.id.gps_fab);
        gpsFab.setOnClickListener {
            getLocation();
        }
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
            R.id.show_list_item -> {
                val intent = Intent(this, CityListActivity::class.java);
                startActivity(intent);
                this.onPause();
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

        val london = LatLng(51.51, -.11)
//        mMap.addMarker(MarkerOptions().position(london).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(london))
        mMap.setOnMapClickListener {
            marker?.remove();
            marker = mMap.addMarker(MarkerOptions().position(it));

            Thread() {
                run {
                    city = Response.getCity(it, this);
                }
                runOnUiThread {
                    if(city.id == 0) {
                        cityInfo.visibility = View.GONE;
                        location = null;
                        return@runOnUiThread;
                    }
                    location = it;
                    cityInfo.visibility = View.VISIBLE;
                    cityInfo.findViewById<TextView>(R.id.wind_speed_text).text = getString(R.string.wind_speed_res).format(city.windSpeed);
                    cityInfo.findViewById<TextView>(R.id.temp_text).text = "${city.temperature} °C";
                    cityInfo.findViewById<ImageView>(R.id.weather_img).setImageResource(city.getWeatherIcon(this));
                    val displayName = city.cityName;
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

//            val intent = Intent(this, CityListActivity::class.java);
//            intent.putExtra("coords", location);
//            startActivity(intent);
//            this.onPause();
        } ?: run {
            Toast.makeText(this, "No location chosen!", Toast.LENGTH_SHORT).show();
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        try {
            val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(!gpsEnabled) {
                Toast.makeText(this, "GPS is turned off", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ex: Exception) {}
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(this, "No GPS permission was given", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        this.location = LatLng(location.latitude, location.longitude);
        marker?.remove();
        marker = mMap.addMarker(MarkerOptions().position(this.location!!));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(this.location, 5.75f))
        Thread() {
            run {
                city = Response.getCity(this.location!!, this);
            }
            runOnUiThread {
                if(city.id == 0) {
                    cityInfo.visibility = View.GONE;
                    this.location = null;
                    return@runOnUiThread;
                }
                cityInfo.visibility = View.VISIBLE;
                cityInfo.findViewById<TextView>(R.id.wind_speed_text).text = getString(R.string.wind_speed_res).format(city.windSpeed);
                cityInfo.findViewById<TextView>(R.id.temp_text).text = "${city.temperature} °C";
                cityInfo.findViewById<ImageView>(R.id.weather_img).setImageResource(city.getWeatherIcon(this));
                val displayName = city.cityName;
                cityInfo.findViewById<TextView>(R.id.location_text).text = displayName;

                val id = resources.getIdentifier("flag_${city.countryCode.lowercase()}", "drawable", packageName);
                cityInfo.findViewById<ImageView>(R.id.country_flag).setImageResource(id);
            }
        }.start()
    }
}