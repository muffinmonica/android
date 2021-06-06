package misterhz.random

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class OptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        val loginButton = findViewById<Button>(R.id.login_button);
        val scoreboardButton = findViewById<Button>(R.id.scoreboard_button);

        loginButton.setOnClickListener {
            Thread() {
                run {
                    Thread.sleep(100)
                }
                runOnUiThread() {
                    val intent = Intent(this, LoginActivity::class.java);
                    startActivity(intent);
                    this.onPause();
                }
            }.start();
        }

        scoreboardButton.setOnClickListener {
            Thread() {
                run {
                    Thread.sleep(100)
                }
                runOnUiThread() {
                    val intent = Intent(this, ScoreboardActivity::class.java);
                    startActivity(intent);
                    this.onPause();
                }
            }.start();
        }
    }
}