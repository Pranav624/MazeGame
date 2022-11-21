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
import android.widget.Toast;

public class PlayManuallyActivity extends AppCompatActivity {
    private static final String TAG = "PlayManuallyActivity";
    private int skill;
    private String builder;
    private boolean rooms;
    private String driver;
    private int pathLength = 0;

    /**
     * Create the 3 switches that allow the user to toggle map, solution, and walls.
     * Also make a seekbar on the right side to allow the user to zoom in and out of the map.
     * @param savedInstanceState
     */
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
                    Toast.makeText(getApplicationContext(), "Map ON", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.v(TAG, "Show Map: OFF");
                    Toast.makeText(getApplicationContext(), "Map OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Switch show_solution_switch = (Switch) findViewById(R.id.show_solution_switch);
        show_solution_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Log.v(TAG, "Show Solution: ON");
                    Toast.makeText(getApplicationContext(), "Solution ON", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.v(TAG, "Show Solution: OFF");
                    Toast.makeText(getApplicationContext(), "Solution OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Switch show_walls_switch = (Switch) findViewById(R.id.show_walls_switch);
        show_walls_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Log.v(TAG, "Show Walls: ON");
                    Toast.makeText(getApplicationContext(), "Walls ON", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.v(TAG, "Show Walls: OFF");
                    Toast.makeText(getApplicationContext(), "Walls OFF", Toast.LENGTH_SHORT).show();
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
     * Increment pathLength.
     * @param v
     */
    public void move(View v){
        Log.v(TAG, "Forward button was clicked.");
        pathLength++;
        Log.v(TAG, "Path length is " + pathLength);
        Toast.makeText(getApplicationContext(), "Moved forward", Toast.LENGTH_SHORT).show();
    }

    /**
     * Rotate left.
     * @param v
     */
    public void rotateLeft(View v){
        Log.v(TAG, "Rotate left button was clicked.");
        Toast.makeText(getApplicationContext(), "Rotated left", Toast.LENGTH_SHORT).show();
    }

    /**
     * Rotate right.
     * @param v
     */
    public void rotateRight(View v){
        Log.v(TAG, "Rotate right button was clicked.");
        Toast.makeText(getApplicationContext(), "Rotated right", Toast.LENGTH_SHORT).show();
    }

    /**
     * Jump over wall.
     * Increment pathLength.
     * @param v
     */
    public void jump(View v){
        Log.v(TAG, "Jump button was clicked.");
        pathLength++;
        Log.v(TAG, "Path length is " + pathLength);
        Toast.makeText(getApplicationContext(), "Jumped", Toast.LENGTH_SHORT).show();
    }

    /**
     * Switches from PlayManuallyActivity to WinningActivity.
     * @param v
     */
    public void switchToWinningManual(View v){
        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("path_length", pathLength);
        intent.putExtra("driver", driver);
        startActivity(intent);
    }

    /**
     * Back button returns to title screen.
     */
    @Override
    public  void onBackPressed(){
        Intent intent = new Intent(this, AMazeActivity.class);
        startActivity(intent);
    }
}