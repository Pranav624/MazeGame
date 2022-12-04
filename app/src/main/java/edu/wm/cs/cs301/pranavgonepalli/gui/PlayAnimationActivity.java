package edu.wm.cs.cs301.pranavgonepalli.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import edu.wm.cs.cs301.pranavgonepalli.gui.AMazeActivity;
import edu.wm.cs.cs301.pranavgonepalli.gui.GeneratingActivity;
import edu.wm.cs.cs301.pranavgonepalli.gui.LosingActivity;
import edu.wm.cs.cs301.pranavgonepalli.R;
import edu.wm.cs.cs301.pranavgonepalli.generation.Maze;

public class PlayAnimationActivity extends AppCompatActivity {
    private static final String TAG = "PlayAnimationActivity";
    private String driver;
    private String robot_configuration;
    private int pathLength = 0;
    private int shortestPath;
    private int energy = 3500;
    private Maze maze;
    private StatePlaying statePlaying;
    private boolean play = false;

    /**
     * Make the three switches that allow the user to toggle map, solution, and walls.
     * Also make the seekbar that allows the user to zoom in and out of the map.
     * Make another seekbar on the bottom that lets the user change the speed of the animation.
     * Add a progress bar that changes based on how much energy the robot has remaining.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        Intent intent = getIntent();
        driver = intent.getStringExtra("driver");
        robot_configuration = intent.getStringExtra("robot_configuration");
        Log.v(TAG, "Driver " + driver + ", Robot configuration " + robot_configuration);

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

        SeekBar speed_seekbar = (SeekBar) findViewById(R.id.speed_seekbar);
        speed_seekbar.setProgress(1);
        speed_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView speed_text = findViewById(R.id.speed_text);
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i == 0){
                    speed_text.setText("Slow");
                    Log.v(TAG, "Speed: Slow");
                }
                else if(i == 1){
                    speed_text.setText("Medium");
                    Log.v(TAG, "Speed: Medium");
                }
                else{
                    speed_text.setText("Fast");
                    Log.v(TAG, "Speed: Fast");
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ProgressBar energy_bar = (ProgressBar) findViewById(R.id.progress_bar);
        energy_bar.setProgress(energy);

        MazePanel panel = findViewById(R.id.maze_view);
        maze = GeneratingActivity.getMaze();
        statePlaying = new StatePlaying(this);
        statePlaying.setMaze(maze);
        statePlaying.start(panel);
        int[] currentPosition = statePlaying.getCurrentPosition();
        shortestPath = maze.getDistanceToExit(currentPosition[0], currentPosition[1]);
    }

    /**
     * Pauses or starts the robot.
     * If the robot is moving, it stops and if it is stopped, it starts when this function is called.
     * @param v
     */
    public void playOrPause(View v){
        Button play_pause = (Button) findViewById(R.id.playpause_button);
        play = !play;
        if(play == false){
            Log.v(TAG, "Robot has stopped.");
            play_pause.setText("START");
            Toast.makeText(getApplicationContext(), "Robot has stopped", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.v(TAG, "Robot is moving.");
            play_pause.setText("PAUSE");
            Toast.makeText(getApplicationContext(), "Robot is moving", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Switches from PlayAnimationActivity to WinningActivity.
     * @param v
     */
    public void switchToWinning(View v){
        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("path_length", pathLength);
        intent.putExtra("shortest_path", shortestPath);
        intent.putExtra("energy_remaining", energy);
        intent.putExtra("driver", driver);
        startActivity(intent);
    }

    /**
     * Switches from PlayAnimationActivity to LosingActivity.
     * @param v
     */
    public void switchToLosing(View v){
        Intent intent = new Intent(this, LosingActivity.class);
        intent.putExtra("path_length", pathLength);
        intent.putExtra("energy_remaining", energy);
        intent.putExtra("driver", driver);
        intent.putExtra("reason", "The robot broke!");
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