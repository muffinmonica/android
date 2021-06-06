package misterhz.fragments

import misterhz.fragments.db.DBHelper

class Route(var id: Int, var start: String, var finish: String, var desc: String) {

    companion object {
        lateinit var routes: List<Route>;
        fun seed() {

        }
    }

    fun getTitle(): String {
        return "$start - $finish";
    }
}