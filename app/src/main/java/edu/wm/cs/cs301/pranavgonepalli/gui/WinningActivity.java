package edu.wm.cs.cs301.pranavgonepalli.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import edu.wm.cs.cs301.pranavgonepalli.gui.AMazeActivity;
import edu.wm.cs.cs301.pranavgonepalli.R;

public class WinningActivity extends AppCompatActivity {
    private static final String TAG = "WinningActivity";
    private int energy_consumed = -1;
    private int path_length;
    private int shortest_path;
    private String driver;
    private MediaPlayer intro;

    /**
     * Displays that the user has won.
     * Also shows the path length, and energy consumed if it was a robot driver.
     * There's a play again button for the user to go back to the title screen.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);

        intro = MediaPlayer.create(this, R.raw.intro);
        intro.start();
        intro.setLooping(true);
        Intent intent = getIntent();
        path_length = intent.getIntExtra("path_length", 0);
        shortest_path = intent.getIntExtra("shortest_path", 0);
        driver = intent.getStringExtra("driver");
        if(!driver.equals("Manual")){
            energy_consumed = intent.getIntExtra("energy_consumed", 0);
        }
        Log.v(TAG, "Path Length: " + path_length + ", Driver: " + driver + ", Energy Consumed: " + energy_consumed);

        TextView path_length_text = findViewById(R.id.path_length_text);
        path_length_text.setText("Path Length: " + path_length);
        TextView shortest_path_text = findViewById(R.id.shortest_path_text);
        shortest_path_text.setText("Shortest Path: " + shortest_path);
        if(energy_consumed != -1){
            TextView energy_consumed_text = findViewById(R.id.energy_consumed_text);
            energy_consumed_text.setText("Energy Consumed: " + energy_consumed);
            energy_consumed_text.setVisibility(TextView.VISIBLE);
        }
    }

    /**
     * Switch to the Title screen.
     * @param v
     */
    public void switchToTitle(View v){
        intro.stop();
        Intent intent = new Intent(this, AMazeActivity.class);
        startActivity(intent);
    }

    /**
     * Back button returns to title screen.
     */
    @Override
    public  void onBackPressed(){
        intro.stop();
        Intent intent = new Intent(this, AMazeActivity.class);
        startActivity(intent);
    }
}