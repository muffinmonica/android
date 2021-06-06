package misterhz.random

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import misterhz.random.db.DBHelper

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toast.makeText(applicationContext, "Papers, please!", Toast.LENGTH_SHORT).show();

        val dbHelper = DBHelper(this);

        val usernameField = findViewById<EditText>(R.id.username)
        val passwordField = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login)
        val registerButton = findViewById<Button>(R.id.register)

        loginButton.setOnClickListener {
            Thread() {
                run {
                    Thread.sleep(100)
                }
                runOnUiThread() {
                    // TODO check nick/pass
                    val nick = usernameField.text.toString();
                    val pass = passwordField.text.toString();

                    val regex = Regex("^\\s*$")
                    if(regex matches nick || regex matches pass) {
                        Toast.makeText(applicationContext, "Where is your passport?", Toast.LENGTH_SHORT).show();
                        return@runOnUiThread;
                    }

                    if(!dbHelper.userExists(nick, pass)) {
                        Toast.makeText(applicationContext, "Your names don't match!", Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(this, MainActivity::class.java);
                        intent.putExtra("user", nick);
                        startActivity(intent);
                        this.onPause();
                    }
                }
            }.start();
        }

        registerButton.setOnClickListener {
            Thread() {
                run {
                    Thread.sleep(100)
                }
                runOnUiThread() {
                    val nick = usernameField.text.toString();
                    val pass = passwordField.text.toString();
                    val regex = Regex("^\\s*$")
                    if(regex matches nick || regex matches pass) {
                        Toast.makeText(applicationContext, "Where is your passport?", Toast.LENGTH_SHORT).show();
                        return@runOnUiThread;
                    }
                    if(pass.length < 6) {
                        Toast.makeText(applicationContext, "Short passwords are not allowed!", Toast.LENGTH_SHORT).show();
                        return@runOnUiThread;
                    }
                    val result = dbHelper.addUser(nick, pass);

                    if(result < 1L) {
                        Toast.makeText(applicationContext, "Wait here!", Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(this, MainActivity::class.java);
                        intent.putExtra("user", nick);
                        startActivity(intent);
                        this.onPause();
                    }
                }
            }.start();
        }
    }

    fun nicknameExists(nick: String, pass: String): Boolean {
        return true;
    }

    fun passwordCorrect(nick: String, pass: String): Boolean {
        return true;
    }
}