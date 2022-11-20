package edu.wm.cs.cs301.pranavgonepalli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

public class PlayManuallyActivity extends AppCompatActivity {
    private static final String TAG = "PlayManuallyActivity";
    private int skill;
    private String builder;
    private boolean rooms;
    private String driver;
    private int pathLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);

        Intent intent = getIntent();
        skill = intent.getIntExtra("skill", 0);
        builder = intent.getStringExtra("builder");
        rooms = intent.getBooleanExtra("rooms", true);
        driver = intent.getStringExtra("driver");
        Log.v(TAG, "Parameters: Skill level " + skill + ", Builder " + builder + ", Rooms " + rooms + ", Driver " + driver);

        Switch show_map_switch = (Switch) findViewById(R.id.show_map_switch);
        show_map_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Log.v(TAG, "Show Map: ON");
                }
                else{
                    Log.v(TAG, "Show Map: OFF");
                }
            }
        });
        Switch show_solution_switch = (Switch) findViewById(R.id.show_solution_switch);
        show_solution_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Log.v(TAG, "Show Solution: ON");
                }
                else{
                    Log.v(TAG, "Show Solution: OFF");
                }
            }
        });
        Switch show_walls_switch = (Switch) findViewById(R.id.show_walls_switch);
        show_walls_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Log.v(TAG, "Show Walls: ON");
                }
                else{
                    Log.v(TAG, "Show Walls: OFF");
                }
            }
        });

        SeekBar zoom_seekbar = (SeekBar) findViewById(R.id.zoom_seekbar);
        zoom_seekbar.setProgress(5);
        zoom_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.v(TAG, "Zoom level: " + i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * Move forward.
     */
    public void move(View v){
        Log.v(TAG, "Forward button was clicked.");
        pathLength++;
        Log.v(TAG, "Path length is " + pathLength);
    }

    /**
     * Rotate left.
     */
    public void rotateLeft(View v){
        Log.v(TAG, "Rotate left button was clicked.");
    }

    /**
     * Rotate right.
     */
    public void rotateRight(View v){
        Log.v(TAG, "Rotate right button was clicked.");
    }

    /**
     * Jump over wall.
     */
    public void jump(View v){
        Log.v(TAG, "Jump button was clicked.");
        pathLength++;
        Log.v(TAG, "Path length is " + pathLength);
    }
}