package com.example.weather

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.android.gms.maps.model.LatLng

class DBHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "cities.db";
        private const val DATABASE_VERSION = 6;

        private const val WEATHER_TABLE = "weather";
        private const val WEATHER_CITY_NAME = "city_name";
        private const val WEATHER_COUNTRY_CODE = "country_code"
        private const val WEATHER_LAT = "lat";
        private const val WEATHER_LON = "lon";
        private const val WEATHER_TEMPERATURE = "temperature";
        private const val WEATHER_WIND_SPEED = "wind_speed";
        private const val WEATHER_UPDATE_AFTER = "update_after"
        // TODO probably direction

        private const val UPDATE_DELAY = 10 * 60;
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("create table $WEATHER_TABLE (" +
                "$WEATHER_LAT real not null," +
                "$WEATHER_LON real not null," +
                "$WEATHER_CITY_NAME text default \"\"," +
                "$WEATHER_COUNTRY_CODE text," +
                "$WEATHER_TEMPERATURE integer," +
                "$WEATHER_WIND_SPEED real," +
                "$WEATHER_UPDATE_AFTER integer default 0," +
                "primary key($WEATHER_LAT, $WEATHER_LON)" +
                ")")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("drop table if exists $WEATHER_TABLE");
        onCreate(db);
    }

    fun addCity(city: City): Long {
        val db = this.writableDatabase;
        val values = ContentValues();
        values.put(WEATHER_CITY_NAME, city.cityName);
        values.put(WEATHER_LAT, city.latitude);
        values.put(WEATHER_LON, city.longitude);
        values.put(WEATHER_COUNTRY_CODE, city.countryCode);
        values.put(WEATHER_TEMPERATURE, city.temperature);
        values.put(WEATHER_WIND_SPEED, city.windSpeed);
        values.put(WEATHER_UPDATE_AFTER, System.currentTimeMillis() / 1000 + UPDATE_DELAY);

        val result = db.insert(WEATHER_TABLE, null, values);
        db.close();
        return result;
    }

    fun getCity(coords: LatLng, context: Context): City {
        val db = this.readableDatabase;
        val query = "select $WEATHER_UPDATE_AFTER from $WEATHER_TABLE where $WEATHER_LAT = ${coords.latitude} and $WEATHER_LON = ${coords.longitude}";
        val result = db.rawQuery(query, null);
        if(result.moveToFirst()) {
            val time = result.getInt(0);
            if(time < System.currentTimeMillis()) {
                updateCity(coords, context);
            }
        } else {
            db.close();
            result.close();

            val city = Response.getCity(coords, context);
            addCity(city);
            return city;
        }
        db.close();
        result.close();

        return getCityRaw(coords);
    }

    private fun getCityRaw(coords: LatLng): City {
        val city = City();
        val db = this.readableDatabase;
        val query = "select * from $WEATHER_TABLE where $WEATHER_LAT = ${coords.latitude} and $WEATHER_LON = ${coords.longitude}";
        val result = db.rawQuery(query, null);
        if(result.moveToFirst()) {
            city.longitude = result.getDouble(result.getColumnIndex(WEATHER_LON));
            city.latitude = result.getDouble(result.getColumnIndex(WEATHER_LAT));
            city.cityName = result.getString(result.getColumnIndex(WEATHER_CITY_NAME));
            city.countryCode = result.getString(result.getColumnIndex(WEATHER_COUNTRY_CODE));
            city.temperature = result.getInt(result.getColumnIndex(WEATHER_TEMPERATURE));
            city.windSpeed = result.getDouble(result.getColumnIndex(WEATHER_WIND_SPEED));

        }
        result.close();
        db.close();
        return city;
    }

    fun updateCity(coords: LatLng, context: Context) {
        val db = this.readableDatabase;
        val city = Response.getCity(coords, context);

        val values = ContentValues();
        values.put(WEATHER_CITY_NAME, city.cityName);
        values.put(WEATHER_LAT, city.latitude);
        values.put(WEATHER_LON, city.longitude);
        values.put(WEATHER_COUNTRY_CODE, city.countryCode);
        values.put(WEATHER_TEMPERATURE, city.temperature);
        values.put(WEATHER_WIND_SPEED, city.windSpeed);
        values.put(WEATHER_UPDATE_AFTER, System.currentTimeMillis() / 1000 + UPDATE_DELAY);

        db.update(WEATHER_TABLE, values, "$WEATHER_LON = ? and $WEATHER_LAT = ?",
            arrayOf("${coords.longitude}", "${coords.latitude}"))
    }

    fun getCityList(context: Context): ArrayList<City> {
        val list = ArrayList<City>();
        val db = this.readableDatabase;
        val query = "select * from $WEATHER_TABLE";
        val result = db.rawQuery(query, null);
        if(result.moveToFirst()) {
            do {
                val city = City();
                city.longitude = result.getDouble(result.getColumnIndex(WEATHER_LON));
                city.latitude = result.getDouble(result.getColumnIndex(WEATHER_LAT));
                city.cityName = result.getString(result.getColumnIndex(WEATHER_CITY_NAME));
                city.countryCode = result.getString(result.getColumnIndex(WEATHER_COUNTRY_CODE));
                city.temperature = result.getInt(result.getColumnIndex(WEATHER_TEMPERATURE));
                city.windSpeed = result.getDouble(result.getColumnIndex(WEATHER_WIND_SPEED));
                list.add(city);
            } while(result.moveToNext())
        }
        result.close();
        db.close();
        return list;
    }
}
