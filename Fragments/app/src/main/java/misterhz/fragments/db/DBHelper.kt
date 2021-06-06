package misterhz.fragments.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import misterhz.fragments.Route
import misterhz.fragments.Time

class DBHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_NAME = "routes.db";
        private val DATABASE_VERSION = 5;

        private val ROUTES_TABLE = "routes";
        private val ROUTES_ID = "id";
        private val ROUTES_START = "start";
        private val ROUTES_FINISH = "finish";
        private val ROUTES_DESC = "desc";

        private val TIMES_TABLE = "times";
        private val TIMES_ID = "id";
        private val TIMES_ROUTE_ID = "route_id";
        private val TIMES_TIME = "time";
        private val TIMES_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("create table $ROUTES_TABLE ($ROUTES_ID integer primary key, " +
                "$ROUTES_START text, $ROUTES_FINISH text, $ROUTES_DESC text)");
        db.execSQL("create table $TIMES_TABLE ($TIMES_ID integer primary key autoincrement, $TIMES_ROUTE_ID integer, " +
                "$TIMES_TIME integer, $TIMES_DATE integer, foreign key($TIMES_ROUTE_ID) references $ROUTES_TABLE($ROUTES_ID))");
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("drop table if exists $ROUTES_TABLE");
        db.execSQL("drop table if exists $TIMES_TABLE");
        onCreate(db);
    }

    fun getRouteList(): List<Route> {
        val list: MutableList<Route> = ArrayList();
        val db = this.readableDatabase;
        val query = "select * from $ROUTES_TABLE";
        val result = db.rawQuery(query, null);
        if(result.moveToFirst()) {
            do {
                val id = result.getInt(result.getColumnIndex(ROUTES_ID));
                val start = result.getString(result.getColumnIndex(ROUTES_START));
                val end = result.getString(result.getColumnIndex(ROUTES_FINISH));
                val desc = result.getString(result.getColumnIndex(ROUTES_DESC));
                list.add(Route(id, start, end, desc));
            } while(result.moveToNext());
        }
        result.close();
        db.close();
        return list;
    }

    fun addRoute(route: Route): Long {
        val db = this.writableDatabase;
        val values = ContentValues();
        values.put(ROUTES_ID, route.id);
        values.put(ROUTES_START, route.start);
        values.put(ROUTES_FINISH, route.finish);
        values.put(ROUTES_DESC, route.desc);

        val result = db.insert(ROUTES_TABLE, null, values);
        db.close();
        return result;
    }

    fun hasNoRoutes(): Boolean {
        val db = this.readableDatabase;
        val query = "select count(*) as c from $ROUTES_TABLE";
        val result = db.rawQuery(query, null);
        result.moveToFirst();
        val count = result.getInt(0);
        result.close();
        db.close();
        return count == 0;
    }

    fun addTime(time: Time): Long {
        val db = this.writableDatabase;
        val values = ContentValues();
        values.put(TIMES_ROUTE_ID, time.routeId);
        values.put(TIMES_TIME, time.time);
        values.put(TIMES_DATE, (System.currentTimeMillis() / 1000).toInt());

        val result = db.insert(TIMES_TABLE, null, values);
        db.close();
        return result;
    }

    fun getLastTimeFor(routeId: Int): IntArray {
        val array = intArrayOf(0, 0);
        val db = this.readableDatabase;
        val query = "select $TIMES_TIME, $TIMES_DATE from $TIMES_TABLE where $routeId = $TIMES_ROUTE_ID order by $TIMES_ID desc limit 1";
        val result = db.rawQuery(query, null);
        if(result.moveToFirst()) {
            array[0] = result.getInt(result.getColumnIndex(TIMES_TIME));
            array[1] = result.getInt(result.getColumnIndex(TIMES_DATE));
        }
        result.close();
        db.close();
        return array;
    }

    fun getTimeFor(routeId: Int, func: String): IntArray {
        val array = intArrayOf(0, 0);
        val db = this.readableDatabase;
        val query = "select $TIMES_TIME, $TIMES_DATE from $TIMES_TABLE where $routeId = $TIMES_ROUTE_ID and $TIMES_TIME = " +
                "(select $func($TIMES_TIME) from $TIMES_TABLE where $routeId = $TIMES_ROUTE_ID)";
        val result = db.rawQuery(query, null);
        if(result.moveToFirst()) {
            array[0] = result.getInt(result.getColumnIndex(TIMES_TIME));
            array[1] = result.getInt(result.getColumnIndex(TIMES_DATE));
        }
        result.close();
        db.close();
        return array;
    }

    fun getAverageTimeFor(routeId: Int): Int {
        val db = this.readableDatabase;
        val query = "select avg($TIMES_TIME) from $TIMES_TABLE where $routeId = $TIMES_ROUTE_ID";
        val result = db.rawQuery(query, null);
        var time = 0;
        if(result.moveToFirst()) {
            time = result.getInt(0);
        }
        result.close();
        db.close();
        return time;
    }
}