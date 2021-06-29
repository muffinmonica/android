package com.example.weather

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlin.math.sign

class DBHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "cities.db";
        private const val DATABASE_VERSION = 17;

        private const val WEATHER_TABLE = "weather";
        private const val WEATHER_ID = "id";
        private const val WEATHER_CITY_NAME = "city_name";
        private const val WEATHER_COUNTRY_CODE = "country_code";
        private const val WEATHER_TEMPERATURE = "temperature";
        private const val WEATHER_WIND_SPEED = "wind_speed";
        private const val WEATHER_WEATHER = "weather";
        // TODO add probably direction

        private const val UPDATE_TABLE = "update_table";
        private const val UPDATE_ID = "id";
        private const val UPDATE_WHEN_ADDED = "when_added";

        private const val UPDATE_DELAY = 600; // in sec
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("create table $WEATHER_TABLE (" +
                "$WEATHER_ID integer not null," +
                "$WEATHER_CITY_NAME text default \"\"," +
                "$WEATHER_COUNTRY_CODE text," +
                "$WEATHER_TEMPERATURE integer," +
                "$WEATHER_WIND_SPEED real," +
                "$WEATHER_WEATHER text," +
                "primary key($WEATHER_ID)" +
                ")")
        db.execSQL("create table $UPDATE_TABLE (" +
                "$UPDATE_ID integer not null," +
                "$UPDATE_WHEN_ADDED integer default 0," +
                "primary key($UPDATE_ID)," +
                "foreign key($UPDATE_ID) references $WEATHER_TABLE($WEATHER_ID)" +
                ")")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("drop table if exists $WEATHER_TABLE");
        db.execSQL("drop table if exists $UPDATE_TABLE");
        onCreate(db);
    }

    fun removeCity(city: City): Boolean {
        val db = this.writableDatabase;
        val a = db.delete(WEATHER_TABLE, "$WEATHER_ID = ?", arrayOf(city.id.toString())) > 0;
        val b = db.delete(UPDATE_TABLE, "$UPDATE_ID = ?", arrayOf(city.id.toString())) > 0;
        return a && b;
    }

    fun addCity(city: City): Long {
        val db = this.writableDatabase;
        val values = ContentValues();
        values.put(WEATHER_CITY_NAME, city.cityName);
        values.put(WEATHER_ID, city.id);
        values.put(WEATHER_COUNTRY_CODE, city.countryCode);
        values.put(WEATHER_TEMPERATURE, city.temperature);
        values.put(WEATHER_WIND_SPEED, city.windSpeed);
        values.put(WEATHER_WEATHER, city.weather);

        val result = db.insert(WEATHER_TABLE, null, values);

        val values2 = ContentValues();
        values2.put(UPDATE_ID, city.id);
        values2.put(UPDATE_WHEN_ADDED, System.currentTimeMillis() / 1000);

        db.insert(UPDATE_TABLE, null, values2);

        db.close();
        return result;
    }

    fun getCity(id: Int, context: Context): City {
//        val db = this.readableDatabase;
//        val query = "select $WEATHER_UPDATE_AFTER from $WEATHER_TABLE where $WEATHER_LAT = ${coords.latitude} and $WEATHER_LON = ${coords.longitude}";
//        val result = db.rawQuery(query, null);
//        var update = false;
//        if(result.moveToFirst()) {
//            val time = result.getInt(0);
//            update = System.currentTimeMillis() > time;
//        } else {
//            db.close();
//            result.close();
//
//            val city = Response.getCity(coords, context);
//            addCity(city);
//            return city;
//        }
//        db.close();
//        result.close();

        return getCityRaw(id);
    }

    private fun getCityRaw(id: Int): City {
        val city = City();
        val db = this.readableDatabase;
        val query = "select * from $WEATHER_TABLE where $WEATHER_ID = $id";
        val result = db.rawQuery(query, null);
        if(result.moveToFirst()) {
            city.id = result.getInt(result.getColumnIndex(WEATHER_ID));
            city.cityName = result.getString(result.getColumnIndex(WEATHER_CITY_NAME));
            city.countryCode = result.getString(result.getColumnIndex(WEATHER_COUNTRY_CODE));
            city.temperature = result.getInt(result.getColumnIndex(WEATHER_TEMPERATURE));
            city.windSpeed = result.getDouble(result.getColumnIndex(WEATHER_WIND_SPEED));
            city.weather = result.getString(result.getColumnIndex(WEATHER_WEATHER));
        }
        result.close();
        db.close();
        return city;
    }

    fun updateCity(id: Int, context: Context, db: SQLiteDatabase?) {
        val city = Response.getCity(id, context);

        val values = ContentValues();
//        values.put(WEATHER_CITY_NAME, city.cityName);
//        values.put(WEATHER_LAT, city.latitude.toString());
//        values.put(WEATHER_LON, city.longitude.toString());
//        values.put(WEATHER_COUNTRY_CODE, city.countryCode);
        values.put(WEATHER_TEMPERATURE, city.temperature);
        values.put(WEATHER_WIND_SPEED, city.windSpeed);

        val r = db!!.update(WEATHER_TABLE, values, "$WEATHER_ID = $id",
            null)


        val values2 = ContentValues();
//        values2.put(UPDATE_LAT, coords.latitude.toString());
//        values2.put(UPDATE_LON, coords.longitude.toString());
        values2.put(UPDATE_WHEN_ADDED, System.currentTimeMillis() / 1000);

        val res = db.update(UPDATE_TABLE, values2, "$UPDATE_ID = $id",
            null);
    }

    fun updateAll(context: Context, forceUpdate: Boolean = false) {
        var db = this.readableDatabase;
        val query = "select * from $UPDATE_TABLE"; // where $UPDATE_WHEN_ADDED + $UPDATE_DELAY < ${System.currentTimeMillis() / 1000}
        val result = db.rawQuery(query, null);
        val list = ArrayList<Int>();
        if(result.moveToFirst()) {
            do {
                val time = result.getInt(result.getColumnIndex(UPDATE_WHEN_ADDED));
                if(forceUpdate || (System.currentTimeMillis() / 1000 > time + UPDATE_DELAY)) {
                    val id = result.getInt(result.getColumnIndex(UPDATE_ID));
                    list.add(id);
                }
            } while(result.moveToNext())
        }
        result.close();
        db.close();

        db = this.writableDatabase;
        list.forEach { id ->
            updateCity(id, context, db);
        }
        db.close();
    }

    fun getCityList(context: Context, forceUpdate: Boolean = false): ArrayList<City> {
        updateAll(context, forceUpdate);
        val list = ArrayList<City>();
        val db = this.readableDatabase;
        val query = "select * from $WEATHER_TABLE";
        val result = db.rawQuery(query, null);
        if(result.moveToFirst()) {
            do {
                val city = City();
                city.id = result.getInt(result.getColumnIndex(WEATHER_ID));
                city.cityName = result.getString(result.getColumnIndex(WEATHER_CITY_NAME));
                city.countryCode = result.getString(result.getColumnIndex(WEATHER_COUNTRY_CODE));
                city.temperature = result.getInt(result.getColumnIndex(WEATHER_TEMPERATURE));
                city.windSpeed = result.getDouble(result.getColumnIndex(WEATHER_WIND_SPEED));
                city.weather = result.getString(result.getColumnIndex(WEATHER_WEATHER));
                list.add(city);
            } while(result.moveToNext())
        }
        result.close();
        db.close();
        return list;
    }
}
