package misterhz.random

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import misterhz.random.db.DBHelper
import java.lang.NumberFormatException
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var attempts = 0;
    var points = 0;
    var numberToGuess = 0;
    var nick = "";

    val db = DBHelper(this);

    lateinit var guessField: EditText;
    lateinit var guessButton: Button;
    lateinit var resetGameButton: Button;
    lateinit var scoreboardButton: Button;
    lateinit var attemptsLabel: TextView;
    lateinit var scoreLabel: TextView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nick = intent.getStringExtra("user").toString();
        points = db.getPoints(nick);

        Toast.makeText(applicationContext, "Cause no trouble, $nick!", Toast.LENGTH_SHORT).show();
        guessField = findViewById<EditText>(R.id.guess_field);
        guessButton = findViewById<Button>(R.id.guess_button);
        attemptsLabel = findViewById<TextView>(R.id.attempts_label);
        scoreLabel = findViewById<TextView>(R.id.score_label);
        resetGameButton = findViewById<Button>(R.id.reset_game_button);
        scoreboardButton = findViewById<Button>(R.id.scoreboard_button2);

        resetGameButton.setOnClickListener {
            resetGame();
            Toast.makeText(applicationContext, "Attempts reset", Toast.LENGTH_SHORT).show();
        }
        guessButton.setOnClickListener {
            guess(guessField.text.toString());
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

        scoreLabel.text = "Your score: $points";
    }

    fun resetPoints() {
        points = 0;
        scoreLabel.text = "Your score: $points";
        db.setPoints(nick, 0)
        Toast.makeText(applicationContext, "Points reset", Toast.LENGTH_SHORT).show();
    }

    fun resetAttempts() {
        attempts = 0;
        attemptsLabel.text = "Your attempts: $attempts";
    }

    fun incrementAttempts() {
        ++attempts;
        attemptsLabel.text = "Your attempts: $attempts";
    }

    fun resetGame() {
        resetPoints();
        resetAttempts();
    }

    fun newRound() {
        numberToGuess = Random.nextInt(0, 20);
        resetAttempts();
    }

    fun getPoints(attempts: Int): Int {
        return when(attempts) {
            1 -> 5;
            in 2..4 -> 3;
            in 5..6 -> 2;
            in 7..10 -> 1;
            else -> 0;
        }
    }

    fun guess(str: String) {
        if(str == "") return;
        guessField.setText("");
        var num = 0;
        try {
            num = str.toInt();
        } catch (ex: NumberFormatException) {
            Toast.makeText(applicationContext, "Numbers only", Toast.LENGTH_SHORT).show();
            return;
        }
        if(num !in 0..20) {
            Toast.makeText(applicationContext, "Numbers from 0 to 20 only", Toast.LENGTH_SHORT).show();
        } else {
            incrementAttempts();
            when {
                num > numberToGuess -> {
                    Toast.makeText(applicationContext, "Less", Toast.LENGTH_SHORT).show();
                }
                num < numberToGuess -> {
                    Toast.makeText(applicationContext, "More", Toast.LENGTH_SHORT).show();
                }
                else -> {
                    val builder = AlertDialog.Builder(this@MainActivity);
                    builder.setTitle("Win");
                    builder.setMessage("Exactly!");
                    builder.setPositiveButton("OK") { dialogInterface: DialogInterface?, which: Int -> }
                    val dialog: AlertDialog = builder.create();
                    dialog.show();
                    points += getPoints(attempts)
                    scoreLabel.text = "Your score: $points";

                    db.setPoints(nick, points)

                    newRound();
                }
            }
        }
        if(attempts >= 10) {
            newRound();
            val builder = AlertDialog.Builder(this@MainActivity);
            builder.setTitle("Loss");
            builder.setMessage("Too many attempts");
            builder.setPositiveButton("OK") { dialogInterface: DialogInterface?, which: Int -> }
            val dialog: AlertDialog = builder.create();
            dialog.show();
        }
    }
}