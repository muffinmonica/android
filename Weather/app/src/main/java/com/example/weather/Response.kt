package com.example.weather

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.util.*
import kotlin.math.roundToInt

class Response {
    companion object {
        public fun getLocationMap(location: LatLng, context: Context): Map<String, String> {
            val map = mutableMapOf<String, String>();

            var query = "https://api.openweathermap.org/data/2.5/weather?lat=%.5f&lon=%.5f&appid=%s";
            query = query.format(location.latitude, location.longitude, context.getString(R.string.weather_key))
            val response = URL(query).readText();

            val jsonObject = JSONObject(response);

            val temp = jsonObject.getJSONObject("main")
            map["temp"] = (temp["temp"] as Double - 273.15).roundToInt().toString();

            val wind = jsonObject.getJSONObject("wind");
            map["wind_speed"] = wind["speed"].toString();

            var countryCode = "";
            val sys = jsonObject.getJSONObject("sys");
            countryCode = try {
                sys["country"] as String;
            } catch(ex: JSONException) {
                "N/A";
            }

            map["country_code"] = countryCode;
            map["country"] = Locale("", countryCode).displayCountry;

            val coords = "%.2f, %.2f".format(location.latitude, location.longitude)

            try {
                val name = jsonObject["name"] as String
                map["location"] = if(name != "") name else coords;
            } catch(ex: JSONException) {
                map["location"] = coords;
            }

            return map;
        }
    }

    fun Double.format(digits: Int) = "%.${digits}f".format(this)
}