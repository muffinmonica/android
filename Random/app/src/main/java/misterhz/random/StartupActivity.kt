package misterhz.random

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class StartupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        Toast.makeText(applicationContext, "Glory to Arstotzka!", Toast.LENGTH_SHORT).show();
        val thread = Thread() {
            run {
                Thread.sleep(3000)
            }
            runOnUiThread() {
                val intent = Intent(this, OptionActivity::class.java);
                startActivity(intent);
                finish();
            }
        }
        thread.start();
    }
}