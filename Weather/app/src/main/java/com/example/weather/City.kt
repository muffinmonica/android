package com.example.weather

import android.content.Context
import java.util.*

class City {
    var cityName = "";
    var countryCode = "";
    var temperature = 0;
    var windSpeed = .0;
    var id = -1;
    var weather = "";

    public fun getCountryName(): String = Locale("", countryCode).displayCountry;

// val id = resources.getIdentifier("flag_${city.countryCode.lowercase()}", "drawable", packageName);

    fun getWeatherIcon(context: Context): Int {
        val pic = when(weather) {
            "Thunderstorm" -> "thunder";
            "Drizzle" -> "shower_rain";
            "Rain" -> if(id % 2 == 0) "rain" else "shower_rain";
            "Snow" -> "snow";
            "Clear" -> "sun";
            "Clouds" -> if(id % 2 == 0) "cloudy" else "few_clouds";
            else -> "fog";
        }
        return context.resources.getIdentifier(pic, "drawable", context.packageName)
    }
}