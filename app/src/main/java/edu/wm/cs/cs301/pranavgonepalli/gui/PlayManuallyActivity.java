package edu.wm.cs.cs301.pranavgonepalli.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import edu.wm.cs.cs301.pranavgonepalli.gui.AMazeActivity;
import edu.wm.cs.cs301.pranavgonepalli.gui.GeneratingActivity;
import edu.wm.cs.cs301.pranavgonepalli.R;
import edu.wm.cs.cs301.pranavgonepalli.generation.Maze;

public class PlayManuallyActivity extends AppCompatActivity {
    private static final String TAG = "PlayManuallyActivity";
    private String driver;
    private int pathLength = 0;
    private int shortestPath;
    private Maze maze;
    private StatePlaying statePlaying;
    private int zoomLevel = 5;
    private MediaPlayer chompsound;
    private MediaPlayer chompsound2;

    /**
     * Create the 3 switches that allow the user to toggle map, solution, and walls.
     * Also make a seekbar on the right side to allow the user to zoom in and out of the map.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);

        chompsound = MediaPlayer.create(this, R.raw.chomp);
        chompsound2 = MediaPlayer.create(this, R.raw.chomp);

        Intent intent = getIntent();
        driver = intent.getStringExtra("driver");
        Log.v(TAG, "Driver " + driver);

        Switch show_map_switch = (Switch) findViewById(R.id.show_map_switch);
        show_map_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Log.v(TAG, "Show Map: ON");
                    //Toast.makeText(getApplicationContext(), "Map ON", Toast.LENGTH_SHORT).show();
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 0);
                }
                else{
                    Log.v(TAG, "Show Map: OFF");
                    //Toast.makeText(getApplicationContext(), "Map OFF", Toast.LENGTH_SHORT).show();
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 0);
                }
            }
        });
        Switch show_solution_switch = (Switch) findViewById(R.id.show_solution_switch);
        show_solution_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Log.v(TAG, "Show Solution: ON");
                    //Toast.makeText(getApplicationContext(), "Solution ON", Toast.LENGTH_SHORT).show();
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 0);
                }
                else{
                    Log.v(TAG, "Show Solution: OFF");
                    //Toast.makeText(getApplicationContext(), "Solution OFF", Toast.LENGTH_SHORT).show();
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 0);
                }
            }
        });
        Switch show_walls_switch = (Switch) findViewById(R.id.show_walls_switch);
        show_walls_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Log.v(TAG, "Show Walls: ON");
                    //Toast.makeText(getApplicationContext(), "Walls ON", Toast.LENGTH_SHORT).show();
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 0);
                }
                else{
                    Log.v(TAG, "Show Walls: OFF");
                    //Toast.makeText(getApplicationContext(), "Walls OFF", Toast.LENGTH_SHORT).show();
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 0);
                }
            }
        });

        SeekBar zoom_seekbar = (SeekBar) findViewById(R.id.zoom_seekbar);
        zoom_seekbar.setProgress(5);
        zoom_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.v(TAG, "Zoom level: " + i);
                if(i > zoomLevel){
                    for(int level = 0; level < 10; level++){
                        statePlaying.handleUserInput(Constants.UserInput.ZOOMIN, 0);
                    }
                }
                else{
                    for(int level = 0; level < 10; level++){
                        statePlaying.handleUserInput(Constants.UserInput.ZOOMOUT, 0);
                    }
                }
                zoomLevel = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        MazePanel panel = findViewById(R.id.maze_view);
        maze = GeneratingActivity.getMaze();
        statePlaying = new StatePlaying(this);
        statePlaying.setMaze(maze);
        statePlaying.start(panel);
        int[] currentPosition = statePlaying.getCurrentPosition();
        shortestPath = maze.getDistanceToExit(currentPosition[0], currentPosition[1]);
    }

    /**
     * Move forward.
     * Increment pathLength.
     * @param v
     */
    public void move(View v){
        if(chompsound.isPlaying()){
            chompsound2.start();
        }
        else{
            chompsound.start();
        }
        Log.v(TAG, "Forward button was clicked.");
        pathLength++;
        Log.v(TAG, "Path length is " + pathLength);
        //Toast.makeText(getApplicationContext(), "Moved forward", Toast.LENGTH_SHORT).show();
        statePlaying.handleUserInput(Constants.UserInput.UP, 0);
        int[] currentPosition = statePlaying.getCurrentPosition();
        if(statePlaying.isOutside(currentPosition[0], currentPosition[1])){
            switchToWinningManual();
        }
    }

    /**
     * Rotate left.
     * @param v
     */
    public void rotateLeft(View v){
        Log.v(TAG, "Rotate left button was clicked.");
        //Toast.makeText(getApplicationContext(), "Rotated left", Toast.LENGTH_SHORT).show();
        statePlaying.handleUserInput(Constants.UserInput.LEFT, 0);
    }

    /**
     * Rotate right.
     * @param v
     */
    public void rotateRight(View v){
        Log.v(TAG, "Rotate right button was clicked.");
        //Toast.makeText(getApplicationContext(), "Rotated right", Toast.LENGTH_SHORT).show();
        statePlaying.handleUserInput(Constants.UserInput.RIGHT, 0);
    }

    /**
     * Jump over wall.
     * Increment pathLength.
     * @param v
     */
    public void jump(View v){
        if(chompsound.isPlaying()){
            chompsound2.start();
        }
        else{
            chompsound.start();
        }
        Log.v(TAG, "Jump button was clicked.");
        pathLength++;
        Log.v(TAG, "Path length is " + pathLength);
        //Toast.makeText(getApplicationContext(), "Jumped", Toast.LENGTH_SHORT).show();
        statePlaying.handleUserInput(Constants.UserInput.JUMP, 0);
    }

    /**
     * Switches from PlayManuallyActivity to WinningActivity.
     */
    public void switchToWinningManual(){
        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("path_length", pathLength);
        intent.putExtra("shortest_path", shortestPath);
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