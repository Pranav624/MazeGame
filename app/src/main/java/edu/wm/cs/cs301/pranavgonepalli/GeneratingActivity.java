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

public class GeneratingActivity extends AppCompatActivity {
    private static final String TAG = "GeneratingActivity";

    private String[] drivers = {"Select", "Manual", "Wizard", "Smart Wizard", "WallFollower"};
    private String[] robot_configurations = {"Premium", "Mediocre", "Soso", "Shaky"};
    private Spinner driver_spinner;
    private Spinner robot_configuration_spinner;
    private ProgressBar progress_bar;
    private int progress = 0;
    private int skill;
    private String builder;
    private boolean rooms;
    private boolean loading = true;

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
        builder = intent.getStringExtra("builder");
        rooms = intent.getBooleanExtra("rooms", true);
        BackgroundThread thread = new BackgroundThread(10);
        new Thread(thread).start();
    }

    /**
     * Switch from GeneratingActivity to PlayManuallyActivity.
     */
    public void switchToPlayManually(){
        Intent intent = new Intent(this, PlayManuallyActivity.class);
        intent.putExtra("skill", skill);
        intent.putExtra("builder", builder);
        intent.putExtra("rooms", rooms);
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
        intent.putExtra("skill", skill);
        intent.putExtra("builder", builder);
        intent.putExtra("rooms", rooms);
        intent.putExtra("driver", chosen_driver);
        intent.putExtra("robot_configuration", chosen_robot_configuration);
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
        int seconds;

        BackgroundThread(int seconds){
            this.seconds = seconds;
        }
        @Override
        public void run(){
            try{
                //Thread.sleep(seconds*1000);
                while(progress < 100){
                    progress+=1;
                    GeneratingActivity.this.runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            progress_bar.setProgress(progress);
                        }
                    });
                    Thread.sleep(seconds*10);
                }
                loading = false;
                String chosen_driver = driver_spinner.getSelectedItem().toString();
                if(!chosen_driver.equals("Select")){
                    if(chosen_driver.equals("Manual")){
                        switchToPlayManually();
                    }
                    else{
                        switchToPlayAnimation();
                    }
                }
                else{
                    GeneratingActivity.this.runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            TextView waiting = findViewById(R.id.waiting_text);
                            waiting.setText("Maze generation completed. Please select a driver");
                        }
                    });
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}