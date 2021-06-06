package misterhz.fragments

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import misterhz.fragments.db.DBHelper
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class StoperFragment : Fragment(), View.OnClickListener {
    var seconds = 0;
    var running = false;
    var wasRunning = false;
    var routeId = 0;
    lateinit var db: DBHelper;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
            routeId = savedInstanceState.getInt("routeId");
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_stoper, container, false);
        db = DBHelper(this.context!!);

        runStoper(layout);
        updateTimes();

        val startButton = layout.findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener(this)
        val stopButton = layout.findViewById<Button>(R.id.stop_button)
        stopButton.setOnClickListener(this)
        val resetButton = layout.findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener(this)
        val saveTimeButton = layout.findViewById<Button>(R.id.save_time_button);
        saveTimeButton.setOnClickListener(this);

        return layout;
    }

    override fun onPause() {
        super.onPause()
        wasRunning = running;
        running = false;
    }

    override fun onResume() {
        super.onResume()
        if(wasRunning) {
            running = true;
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putInt("seconds", seconds)
        savedInstanceState.putBoolean("running", running)
        savedInstanceState.putBoolean("wasRunning", wasRunning)
        savedInstanceState.putInt("routeId", routeId);
    }

    private fun onClickStart() {
        running = true
    }

    private fun onClickStop() {
        running = false
    }

    private fun onClickReset() {
        running = false
        seconds = 0
    }

    private fun onClickSaveTime() {
        val time = Time(routeId, seconds);
        db.addTime(time);
//        Toast.makeText(this.context, time.toString(), Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTimes() {
        val timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

        val minTimeLabel = view?.findViewById<TextView>(R.id.min_time);
        val maxTimeLabel = view?.findViewById<TextView>(R.id.max_time);
        val avgTimeLabel = view?.findViewById<TextView>(R.id.avg_time);
        val lastTimeLabel = view?.findViewById<TextView>(R.id.last_time);

        val minTime = db.getTimeFor(routeId, "min");
        val maxTime = db.getTimeFor(routeId, "max");
        val lastTime = db.getLastTimeFor(routeId);
        val avgTime = db.getAverageTimeFor(routeId);

        val tsAsDateMin = DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochSecond(minTime[1].toLong()));
        val tsAsDateMax = DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochSecond(maxTime[1].toLong()));
        val tsAsDateLast = DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochSecond(lastTime[1].toLong()));

        val dateMin = LocalDate.parse(tsAsDateMin, timeFormat);
        val dateMax = LocalDate.parse(tsAsDateMax, timeFormat);
        val dateLast = LocalDate.parse(tsAsDateLast, timeFormat);

        minTimeLabel?.text = getString(R.string.min_time_s, minTime[0] / 3600, minTime[0] % 3600 / 60, minTime[0] % 60) +
                ", " + dateMin.year + "-" + dateMin.monthValue + "-" + dateMin.dayOfMonth;
        maxTimeLabel?.text = getString(R.string.max_time_s, maxTime[0] / 3600, maxTime[0] % 3600 / 60, maxTime[0] % 60) +
                ", " + dateMax.year + "-" + dateMax.monthValue + "-" + dateMax.dayOfMonth;
        lastTimeLabel?.text = getString(R.string.last_time_s, lastTime[0] / 3600, lastTime[0] % 3600 / 60, lastTime[0] % 60) +
                ", " + dateLast.year + "-" + dateLast.monthValue + "-" + dateLast.dayOfMonth;
        avgTimeLabel?.text = getString(R.string.avg_time_s, avgTime / 3600, avgTime % 3600 / 60, avgTime % 60);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun runStoper(view: View) {
        val timeView = view.findViewById<View>(R.id.time_view) as TextView
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                updateTimes()
                val hours = seconds / 3600
                val minutes = seconds % 3600 / 60
                val secs = seconds % 60
                val time = String.format("%d:%02d:%02d", hours, minutes, secs)
                timeView.text = time
                if (running) {
                    seconds++
                }
                handler.postDelayed(this, 1000)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.start_button -> onClickStart();
            R.id.stop_button -> onClickStop();
            R.id.reset_button -> onClickReset();
            R.id.save_time_button -> onClickSaveTime();
        }
    }
}