package misterhz.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import misterhz.fragments.db.DBHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RouteListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RouteListFragment : ListFragment() {

    lateinit var db: DBHelper;

    lateinit var listener: Listener;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val superView = super.onCreateView(inflater, container, savedInstanceState);
        db = DBHelper(this.context!!);

        Thread() {
            run {
                if(db.hasNoRoutes()) {
                    seed();
                }
                Route.routes = db.getRouteList();
            }
            activity?.runOnUiThread {
                val names = Array<String>(Route.routes.size) {""};
                for(i in Route.routes.indices) {
                    names[i] = Route.routes[i].getTitle();
                }
                val adapter = ArrayAdapter<String>(inflater.context, android.R.layout.simple_list_item_1, names);
                listAdapter = adapter;
            }
        }.start();

        return superView;

    }

    override fun onAttach(context: Context) {
        super.onAttach(context);
        listener = context as Listener;
    }

    override fun onListItemClick(listView: ListView, itemView: View, position: Int, id: Long) {
        listener.itemClicked(id.toInt());
    }

    fun seed() {
        if(db.hasNoRoutes()) {
            db.addRoute(Route(0, "Berlin", "Warsaw", "In one run"));
            db.addRoute(Route(1, "Seoul", "Pyongyang", "For kimchi lovers"));
            db.addRoute(Route(2, "Beijing", "Moscow", "Aliexpress delivery"));
        }
    }
}