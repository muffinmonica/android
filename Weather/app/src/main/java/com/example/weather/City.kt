package com.example.weather

import java.util.*

class City {
    var cityName = "";
    var countryCode = "";
    var temperature = 0;
    var windSpeed = .0;
    var id = -1;

    public fun getCountryName(): String = Locale("", countryCode).displayCountry;
}