package misterhz.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity(), Listener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun itemClicked(id: Int) {
        val fragmentContainer = findViewById<View>(R.id.fragment_container);
        if(fragmentContainer != null) {
            val details = RouteFragment();
            val transaction = supportFragmentManager.beginTransaction();
            details.setRoute(id);
            transaction.replace(R.id.fragment_container, details);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            val intent = Intent(this, DetailsActivity::class.java);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }
}