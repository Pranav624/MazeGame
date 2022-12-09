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
import android.widget.TextView;

import edu.wm.cs.cs301.pranavgonepalli.gui.AMazeActivity;
import edu.wm.cs.cs301.pranavgonepalli.gui.GeneratingActivity;
import edu.wm.cs.cs301.pranavgonepalli.gui.LosingActivity;
import edu.wm.cs.cs301.pranavgonepalli.R;
import edu.wm.cs.cs301.pranavgonepalli.generation.Maze;

public class PlayAnimationActivity extends AppCompatActivity {
    private static final String TAG = "PlayAnimationActivity";
    private String driver_string;
    private RobotDriver driver;
    private String robot_configuration_string;
    private Robot robot_configuration;
    private int zoomLevel = 5;
    private DistanceSensor forward;
    private DistanceSensor backward;
    private DistanceSensor left;
    private DistanceSensor right;
    private int shortestPath;
    private float energy = 3500;
    private int speed = 300;
    private Maze maze;
    private StatePlaying statePlaying;
    private boolean play = false;
    private Button forward_sensor_button;
    private Button left_sensor_button;
    private Button right_sensor_button;
    private Button backward_sensor_button;
    private SeekBar energy_bar;
    private TextView energy_text;
    private MediaPlayer chompsound;
    private boolean started = false;
    AnimationThread thread = new AnimationThread();
    Thread myThread = new Thread(thread);

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

        chompsound = MediaPlayer.create(this, R.raw.chomp);

        Intent intent = getIntent();
        driver_string = intent.getStringExtra("driver");
        robot_configuration_string = intent.getStringExtra("robot_configuration");
        Log.v(TAG, "Driver " + driver_string + ", Robot configuration " + robot_configuration_string);

        forward_sensor_button = findViewById(R.id.forward_sensor_button);
        left_sensor_button = findViewById(R.id.left_sensor_button);
        right_sensor_button = findViewById(R.id.right_sensor_button);
        backward_sensor_button = findViewById(R.id.backward_sensor_button);
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

        SeekBar speed_seekbar = (SeekBar) findViewById(R.id.speed_seekbar);
        speed_seekbar.setProgress(1);
        speed_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView speed_text = findViewById(R.id.speed_text);
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i == 0){
                    speed_text.setText("Slow");
                    speed = 600;
                    Log.v(TAG, "Speed: Slow");
                }
                else if(i == 1){
                    speed_text.setText("Medium");
                    speed = 300;
                    Log.v(TAG, "Speed: Medium");
                }
                else{
                    speed_text.setText("Fast");
                    speed = 100;
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

        energy_bar = (SeekBar) findViewById(R.id.progress_bar);
        energy_bar.setProgress((int)energy);
        energy_text = findViewById(R.id.energy_text);

        MazePanel panel = findViewById(R.id.maze_view);
        maze = GeneratingActivity.getMaze();
        statePlaying = new StatePlaying(this);
        statePlaying.setMaze(maze);
        statePlaying.start(panel);
        int[] currentPosition = statePlaying.getCurrentPosition();
        shortestPath = maze.getDistanceToExit(currentPosition[0], currentPosition[1]);
        if(driver_string.equals("Wizard")){driver = new Wizard();}
        else if(driver_string.equals("Smart Wizard")){driver = new SmartWizard();}
        else if(driver_string.equals("WallFollower")){driver = new WallFollower();}
        configureRobot(robot_configuration_string);
        driver.setRobot(robot_configuration);
        driver.setMaze(maze);
        myThread.start();
    }

    /**
     * Creates the robot and adds its sensors based on the configuration string.
     * @param configuration
     */
    public void configureRobot(String configuration){
        if(configuration.equals("Premium")){
            robot_configuration = new ReliableRobot();
            forward = new ReliableSensor();
            backward = new ReliableSensor();
            left = new ReliableSensor();
            right = new ReliableSensor();
        }
        else if(configuration.equals("Mediocre")){
            robot_configuration = new UnreliableRobot();
            forward = new ReliableSensor();
            backward = new ReliableSensor();
            left = new UnreliableSensor();
            right = new UnreliableSensor();
        }
        else if(configuration.equals("Soso")){
            robot_configuration = new UnreliableRobot();
            forward = new UnreliableSensor();
            backward = new UnreliableSensor();
            left = new ReliableSensor();
            right = new ReliableSensor();
        }
        else if(configuration.equals("Shaky")){
            robot_configuration = new UnreliableRobot();
            forward = new UnreliableSensor();
            backward = new UnreliableSensor();
            left = new UnreliableSensor();
            right = new UnreliableSensor();
        }
        robot_configuration.setStatePlaying(statePlaying);
        forward.setMaze(maze);
        backward.setMaze(maze);
        left.setMaze(maze);
        right.setMaze(maze);
        robot_configuration.addDistanceSensor(forward, Robot.Direction.FORWARD);
        robot_configuration.addDistanceSensor(backward, Robot.Direction.BACKWARD);
        robot_configuration.addDistanceSensor(left, Robot.Direction.LEFT);
        robot_configuration.addDistanceSensor(right, Robot.Direction.RIGHT);
    }

    /**
     * Starts the failure and repair process for each of the sensors of Robot r.
     * @param r
     */
    public void startSensors(Robot r){
        try{
            r.startFailureAndRepairProcess(Robot.Direction.FORWARD, 4000, 2000);
            Thread.sleep(1300);
            r.startFailureAndRepairProcess(Robot.Direction.BACKWARD, 4000, 2000);
            Thread.sleep(1300);
            r.startFailureAndRepairProcess(Robot.Direction.LEFT, 4000, 2000);
            Thread.sleep(1300);
            r.startFailureAndRepairProcess(Robot.Direction.RIGHT, 4000, 2000);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Updates the sensor buttons on the screen, showing whether the sensors are operational or not.
     */
    public void updateSensors(){
        if(forward.getIsOperational()){forward_sensor_button.setBackgroundColor(0xff34eb43);}
        else{forward_sensor_button.setBackgroundColor(0xffff0000);}
        if(left.getIsOperational()){left_sensor_button.setBackgroundColor(0xff34eb43);}
        else{left_sensor_button.setBackgroundColor(0xffff0000);}
        if(right.getIsOperational()){right_sensor_button.setBackgroundColor(0xff34eb43);}
        else{right_sensor_button.setBackgroundColor(0xffff0000);}
        if(backward.getIsOperational()){backward_sensor_button.setBackgroundColor(0xff34eb43);}
        else{backward_sensor_button.setBackgroundColor(0xffff0000);}
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
            chompsound.pause();
            Log.v(TAG, "Robot has stopped.");
            play_pause.setText("START");
            //Toast.makeText(getApplicationContext(), "Robot has stopped", Toast.LENGTH_SHORT).show();
        }
        else{
            if(started == true){
                chompsound.start();
                chompsound.setLooping(true);
            }
            Log.v(TAG, "Robot is moving.");
            play_pause.setText("PAUSE");
            //Toast.makeText(getApplicationContext(), "Robot is moving", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Switches from PlayAnimationActivity to WinningActivity.
     */
    public void switchToWinning(){
        chompsound.stop();
        myThread.interrupt();
        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("path_length", robot_configuration.getOdometerReading());
        intent.putExtra("shortest_path", shortestPath);
        intent.putExtra("energy_consumed", (int)driver.getEnergyConsumption());
        intent.putExtra("driver", driver_string);
        startActivity(intent);
    }

    /**
     * Switches from PlayAnimationActivity to LosingActivity.
     */
    public void switchToLosing(){
        chompsound.stop();
        myThread.interrupt();
        Intent intent = new Intent(this, LosingActivity.class);
        intent.putExtra("path_length", robot_configuration.getOdometerReading());
        intent.putExtra("energy_consumed", (int)driver.getEnergyConsumption());
        intent.putExtra("driver", driver_string);
        intent.putExtra("reason", "The robot broke!");
        startActivity(intent);
    }

    /**
     * Back button returns to title screen.
     */
    @Override
    public  void onBackPressed(){
        chompsound.stop();
        myThread.interrupt();
        Intent intent = new Intent(this, AMazeActivity.class);
        startActivity(intent);
    }

    /**
     * This is the background thread that plays the game and updates the animation.
     */
    class AnimationThread implements Runnable{
        @Override
        public void run(){
            started = true;
            startSensors(robot_configuration);
            if(play) {
                chompsound.start();
                chompsound.setLooping(true);
            }
            while(!(robot_configuration.isAtExit() &&
                    robot_configuration.canSeeThroughTheExitIntoEternity(Robot.Direction.FORWARD))){
                while(!play){}
                try{
                    driver.drive1Step2Exit();
                } catch (Exception e){
                    switchToLosing();
                }
                PlayAnimationActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateSensors();
                        energy = 3500 - driver.getEnergyConsumption();
                        energy_bar.setProgress((int)energy);
                        energy_text.setText("Energy Remaining: " + (int)energy);
                    }
                });
                try{
                    Thread.sleep(speed);
                } catch (Exception e){
                    return;
                }
            }
            if(robot_configuration.getBatteryLevel() >= robot_configuration.getEnergyForStepForward()){
                Log.v(TAG, "test1");
                robot_configuration.move(1);
                switchToWinning();
            }
            else{
                switchToLosing();
            }
        }
    }
}