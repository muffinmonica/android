package com.example.weather

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import org.json.JSONException
import org.json.JSONObject
import java.lang.ClassCastException
import java.net.URL
import kotlin.math.roundToInt

class Response {
    companion object {
        public fun getCity(location: LatLng, context: Context): City {
            val city = City();

            var query = "https://api.openweathermap.org/data/2.5/weather?lat=%.5f&lon=%.5f&appid=%s";
            query = query.format(location.latitude, location.longitude, context.getString(R.string.weather_key))
            val response = URL(query).readText();

            val jsonObject = JSONObject(response);

            val temp = jsonObject.getJSONObject("main")

            try {
                city.temperature = (temp["temp"] as Double - 273.15).roundToInt();
            } catch(e: ClassCastException) {
                city.temperature = (temp["temp"] as Int - 273.15).roundToInt();
            }

            val wind = jsonObject.getJSONObject("wind");

            try {
                city.windSpeed = wind["speed"] as Double;
            } catch(e: ClassCastException) {
                city.windSpeed = wind["speed"] as Int + .0;
            }

            val sys = jsonObject.getJSONObject("sys");
            city.countryCode = try {
                sys["country"] as String;
            } catch(ex: JSONException) {
                "N/A";
            }
            city.id = try {
                jsonObject["id"] as Int;
            } catch(ex: JSONException) {
                -1;
            }

            try {
                val name = jsonObject["name"] as String
                city.cityName = name;
            } catch(ex: JSONException) {
                city.cityName = "";
            }

            city.weather = jsonObject.getJSONArray("weather").getJSONObject(0)["main"] as String;
            return city;
        }
        public fun getCity(id: Int, context: Context): City {
            val city = City();

            var query = "https://api.openweathermap.org/data/2.5/weather?id=%d&appid=%s";
            query = query.format(id, context.getString(R.string.weather_key))
            val response = URL(query).readText();

            val jsonObject = JSONObject(response);

            val temp = jsonObject.getJSONObject("main")

            try {
                city.temperature = (temp["temp"] as Double - 273.15).roundToInt();
            } catch(e: ClassCastException) {
                city.temperature = (temp["temp"] as Int - 273.15).roundToInt();
            }

            val wind = jsonObject.getJSONObject("wind");

            try {
                city.windSpeed = wind["speed"] as Double;
            } catch(e: ClassCastException) {
                city.windSpeed = wind["speed"] as Int + .0;
            }

            val sys = jsonObject.getJSONObject("sys");
            city.countryCode = try {
                sys["country"] as String;
            } catch(ex: JSONException) {
                "N/A";
            }
            city.id = try {
                sys["id"] as Int;
            } catch(ex: JSONException) {
                -1;
            }

            try {
                val name = jsonObject["name"] as String
                city.cityName = name;
            } catch(ex: JSONException) {
                city.cityName = "";
            }
            return city;
        }
    }
}