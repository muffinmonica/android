package misterhz.random.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DBHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_NAME = "users.db";
        private val DATABASE_VERSION = 1;

        private val TABLE_NAME = "users"
        private val ID_COLUMN = "id"
        private val USERNAME_COLUMN = "user"
        private val PASSWORD_COLUMN = "pass"
        private val SCORE_COLUMN = "score"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("create table $TABLE_NAME ($ID_COLUMN integer primary key autoincrement, " +
                "$USERNAME_COLUMN text unique, $PASSWORD_COLUMN text, $SCORE_COLUMN integer)");
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("drop table if exists $TABLE_NAME");
        onCreate(db);
    }

    fun userExists(nick: String, pass: String): Boolean {
        val regex = Regex("^\\s*$")
        if(regex matches nick || regex matches pass) {
            return false;
        }
        val db = this.readableDatabase;
        val query = "select * from users where $USERNAME_COLUMN = \"$nick\" and $PASSWORD_COLUMN = \"$pass\"";
        val result = db.rawQuery(query, null);
        val exists = result.moveToFirst();
        result.close();
        db.close();
        return exists;
    }

    fun addUser(nick: String, pass: String): Long {
        val regex = Regex("^\\s*$")
        if(regex matches nick || regex matches pass) {
            return -1L
        }
        val db = this.writableDatabase;
        val values = ContentValues();
        values.put(USERNAME_COLUMN, nick);
        values.put(PASSWORD_COLUMN, pass);
        values.put(SCORE_COLUMN, 0);

        val result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    fun getPoints(nick: String): Int {
        val db = this.readableDatabase;
        val query = "select $SCORE_COLUMN from $TABLE_NAME where $USERNAME_COLUMN = \"$nick\""
        val result = db.rawQuery(query, null);
        val exists = result.moveToFirst();
        if(!exists) {
            return -1;
        }
        val scoreColumn = result.getColumnIndex(SCORE_COLUMN);
        val score = result.getInt(scoreColumn);
        result.close();
        db.close();
        return score;
    }

    fun setPoints(nick: String, points: Int): Int {
        val db = this.writableDatabase;
        val contentValues = ContentValues();
        contentValues.put(USERNAME_COLUMN, nick);
        contentValues.put(SCORE_COLUMN, points);
        val res = db.update(TABLE_NAME, contentValues, "$USERNAME_COLUMN = \"$nick\"", null);
        db.close();
        return res;
    }

    fun getList(): List<String> {
        val list: MutableList<String> = ArrayList()
        val db = this.readableDatabase
        val query = "select * from $TABLE_NAME order by $SCORE_COLUMN desc limit 10"
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()) {
            do {
                var s = "";
                s += result.getString(result.getColumnIndex(USERNAME_COLUMN)) + " "
                s += result.getString(result.getColumnIndex(SCORE_COLUMN))
                list.add(s)
            } while(result.moveToNext())
        }
        result.close();
        db.close()
        return list;
    }
}