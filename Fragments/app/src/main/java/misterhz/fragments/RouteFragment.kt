package misterhz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import misterhz.fragments.db.DBHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RouteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RouteFragment : Fragment() {
    private var routeId: Int = 0;
    private lateinit var stoper: StoperFragment;

    fun setRoute(id: Int) {
        routeId = id;
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            stoper = StoperFragment();
            stoper.routeId = routeId;
            val ft = childFragmentManager.beginTransaction()
            ft.add(R.id.stoper_container2, stoper)
            ft.addToBackStack(null)
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            ft.commit()
        } else {
            routeId = savedInstanceState.getInt("id");
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_route, container, false)
    }

    override fun onStart() {
        super.onStart();
        val title = view?.findViewById<TextView>(R.id.route_title);
        val route = Route.routes[routeId];
        title?.text = route.getTitle();
        val description = view?.findViewById<TextView>(R.id.route_description);
        description?.text = route.desc;
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("id", routeId);
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RouteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RouteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}