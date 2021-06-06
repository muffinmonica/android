package misterhz.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val fragment = supportFragmentManager.findFragmentById(R.id.route_fragment) as RouteFragment;
        val routeId = intent.extras?.get("id");
        fragment.setRoute(routeId as Int);
    }
}