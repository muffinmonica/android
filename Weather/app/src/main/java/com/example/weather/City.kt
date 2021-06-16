package com.example.weather

import java.util.*

class City {
    var cityName = "";
    var countryCode = "";
    var latitude = .0;
    var longitude = .0;
    var temperature = 0;
    var windSpeed = .0;

    public fun getCountryName(): String = Locale("", countryCode).displayCountry;
    public fun getDisplayCoords(): String = "%.2f, %.2f".format(latitude, longitude);
}