package edu.wm.cs.cs301.pranavgonepalli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import edu.wm.cs.cs301.pranavgonepalli.generation.DefaultOrder;
import edu.wm.cs.cs301.pranavgonepalli.generation.Maze;
import edu.wm.cs.cs301.pranavgonepalli.generation.MazeFactory;
import edu.wm.cs.cs301.pranavgonepalli.generation.Order;

public class GeneratingActivity extends AppCompatActivity {
    private static final String TAG = "GeneratingActivity";

    private String[] drivers = {"Select", "Manual", "Wizard", "Smart Wizard", "WallFollower"};
    private String[] robot_configurations = {"Premium", "Mediocre", "Soso", "Shaky"};
    private Spinner driver_spinner;
    private Spinner robot_configuration_spinner;
    private ProgressBar progress_bar;
    private int skill;
    private String builder_string;
    private Order.Builder builder;
    private boolean rooms;
    private boolean loading = true;
    private MazeFactory mazeFactory;
    private DefaultOrder order;
    private static Maze maze;
    BackgroundThread thread = new BackgroundThread();
    Thread myThread = new Thread(thread);

    /**
     * Create the spinner that contains the drivers the user can choose from.
     * If the user chooses a driver besides Manual, lets the user choose robot configuration
     * by making the text and spinner visible.
     * Also, start the background thread in this method.
     * After the user chooses a driver, if the background thread hasn't completed, it'll tell
     * the user to please wait until it's done.
     * If it is completed, it'll switch to the respective statePlaying.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        driver_spinner = (Spinner) findViewById(R.id.maze_driver);
        ArrayAdapter driver = new ArrayAdapter(this, android.R.layout.simple_spinner_item, drivers);
        driver.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driver_spinner.setAdapter(driver);

        robot_configuration_spinner = (Spinner) findViewById(R.id.robot_configuration);
        ArrayAdapter robot_configuration = new ArrayAdapter(this, android.R.layout.simple_spinner_item, robot_configurations);
        robot_configuration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        robot_configuration_spinner.setAdapter(robot_configuration);

        driver_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView robot_text = findViewById(R.id.robot_configuration_text);
                TextView waiting = findViewById(R.id.waiting_text);
                if(adapterView.getItemAtPosition(i).equals("Wizard") ||
                   adapterView.getItemAtPosition(i).equals("Smart Wizard") ||
                   adapterView.getItemAtPosition(i).equals("WallFollower")){
                    robot_text.setVisibility(TextView.VISIBLE);
                    robot_configuration_spinner.setVisibility(TextView.VISIBLE);
                }
                else{
                    robot_text.setVisibility(TextView.GONE);
                    robot_configuration_spinner.setVisibility(TextView.GONE);
                }
                if(!adapterView.getItemAtPosition(i).equals("Select")){
                    if(loading == true) {
                        waiting.setText("Maze generation will be completed soon. Please wait");
                        //Toast.makeText(getApplicationContext(), "Maze generation will be completed soon. Please wait", Toast.LENGTH_LONG).show();
                    }
                    else{
                        if(adapterView.getItemAtPosition(i).equals("Manual")){
                            switchToPlayManually();
                        }
                        else switchToPlayAnimation();
                    }
                }
                else waiting.setText("Maze generating...");
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Intent intent = getIntent();
        skill = intent.getIntExtra("skill", 0);
        builder_string = intent.getStringExtra("builder");
        if(builder_string.equals("DFS")){
            builder = Order.Builder.DFS;
        }
        else if(builder_string.equals("Prim")){
            builder = Order.Builder.Prim;
        }
        else{
            builder = Order.Builder.Boruvka;
        }
        rooms = intent.getBooleanExtra("rooms", true);
        Log.v(TAG, "Parameters Selected in Title Screen: Skill level " + skill + ", Builder " + builder_string + ", Rooms " + rooms);
        myThread.start();
    }

    /**
     * Returns the maze that was generated.
     * @return
     */
    public static Maze getMaze(){
        return maze;
    }

    /**
     * Switch from GeneratingActivity to PlayManuallyActivity.
     */
    public void switchToPlayManually(){
        Intent intent = new Intent(this, PlayManuallyActivity.class);
        intent.putExtra("driver", "Manual");
        startActivity(intent);
    }

    /**
     * Switch from GeneratingActivity to PlayAnimationActivity.
     */
    public void switchToPlayAnimation(){
        Intent intent = new Intent(this, PlayAnimationActivity.class);
        String chosen_driver = driver_spinner.getSelectedItem().toString();
        String chosen_robot_configuration = robot_configuration_spinner.getSelectedItem().toString();
        intent.putExtra("driver", chosen_driver);
        intent.putExtra("robot_configuration", chosen_robot_configuration);
        startActivity(intent);
    }

    /**
     * Back button returns to title screen.
     */
    @Override
    public  void onBackPressed(){
        myThread.interrupt();
        Intent intent = new Intent(this, AMazeActivity.class);
        startActivity(intent);
    }

    /**
     * This is the background thread that imitates the generation of a maze.
     * It sleeps for the number of seconds specified and then tells the user to select
     * a driver if one isn't chosen yet.
     * During this time, it also updates the ProgressBar.
     * Otherwise, it'll switch to the respective statePlaying.
     */
    class BackgroundThread implements Runnable{
        @Override
        public void run(){
            mazeFactory = new MazeFactory();
            order = new DefaultOrder(skill);
            order.setBuilder(builder);
            order.setPerfect(!rooms);
            mazeFactory.order(order);
            try{
                while(order.getProgress() < 100){
                    GeneratingActivity.this.runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            progress_bar.setProgress(order.getProgress());
                        }
                    });
                    Thread.sleep(10);
                }
                loading = false;
                mazeFactory.waitTillDelivered();
                maze = order.getMaze();
                GeneratingActivity.this.runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        progress_bar.setProgress(100);
                        String chosen_driver = driver_spinner.getSelectedItem().toString();
                        if(!chosen_driver.equals("Select")){
                            if(chosen_driver.equals("Manual")){
                                Log.v(TAG, "Switching from generating to manual activity.");
                                switchToPlayManually();
                            }
                            else{
                                Log.v(TAG, "Switching from generating to animation activity.");
                                switchToPlayAnimation();
                            }
                        }
                        else{
                            TextView waiting = findViewById(R.id.waiting_text);
                            waiting.setText("Maze generation completed. Please select a driver");
                        }
                    }
                });
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}