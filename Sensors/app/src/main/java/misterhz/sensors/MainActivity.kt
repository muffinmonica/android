package misterhz.sensors

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity(), SensorEventListener {

    var latitude = .0;
    var longitude = .0;
    lateinit var orientationLabel: TextView;
    lateinit var gpsLabel: TextView;
    lateinit var lightLabel: TextView;
    lateinit var temperatureLabel: TextView;
    lateinit var proximityLabel: TextView;

    lateinit var sensorManager: SensorManager;
    lateinit var temperatureSensor: Sensor;
    lateinit var lightSensor: Sensor;
    lateinit var proximitySensor: Sensor;

    fun Double.format(digits: Int) = "%.${digits}f".format(this);

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        orientationLabel = findViewById<TextView>(R.id.orientation_label);
        gpsLabel = findViewById<TextView>(R.id.location_label);
        lightLabel = findViewById<TextView>(R.id.light_label);
        temperatureLabel = findViewById<TextView>(R.id.temperature_label);
        proximityLabel = findViewById<TextView>(R.id.proximity_label);

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager;
        val locationListener = LocationListener { location ->
            latitude = location.latitude;
            longitude = location.longitude;
            gpsLabel.text = "Latitude: ${latitude.format(4)}\nLongitude: ${longitude.format(4)}";
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000.toLong(), .0.toFloat(), locationListener);

        Thread() {
            while(true) {
                refreshOrientation();
                Thread.sleep(100);
            }
        }.start();

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager;

        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private fun refreshOrientation() {
        runOnUiThread {
            if (resources.configuration.orientation == 1) {
                orientationLabel.text = "Orientation: portrait"
            } else {
                orientationLabel.text = "Orientation: landscape"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Toast.makeText(this, "onAccuracyChanged not implemented", Toast.LENGTH_SHORT).show();
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return;
        when(event.sensor.type) {
            Sensor.TYPE_LIGHT -> lightLabel.text = "${event.values[0]} lux";
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                temperatureLabel.text = "${event.values[0]} deg";
//                Thread.sleep(1000);
            }
            Sensor.TYPE_PROXIMITY -> proximityLabel.text = "Proximity: ${event.values[0]} cm"
        }
    }

    override fun onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}