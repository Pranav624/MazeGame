package edu.wm.cs.cs301.pranavgonepalli.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import edu.wm.cs.cs301.pranavgonepalli.gui.AMazeActivity;
import edu.wm.cs.cs301.pranavgonepalli.R;

public class LosingActivity extends AppCompatActivity {
    private static final String TAG = "LosingActivity";
    private int energy_consumed = -1;
    private int path_length;
    private String driver;
    private String reason;

    /**
     * Displays that the robot lost and the reason.
     * Shows the path length and the energy consumed.
     * There's a play again button for the user to go back to the title screen.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);

        Intent intent = getIntent();
        path_length = intent.getIntExtra("path_length", 0);
        driver = intent.getStringExtra("driver");
        energy_consumed = intent.getIntExtra("energy_consumed", 0);
        reason = intent.getStringExtra("reason");
        Log.v(TAG, "Path Length: " + path_length + ", Driver: " + driver + ", Energy Consumed: " + energy_consumed + ", Reason " + reason);

        TextView path_length_text = findViewById(R.id.path_length_text);
        path_length_text.setText("Path Length: " + path_length);
        TextView energy_consumed_text = findViewById(R.id.energy_consumed_text);
        energy_consumed_text.setText("Energy Consumed: " + energy_consumed);
        TextView reason_text = findViewById(R.id.reason_text);
        reason_text.setText(reason);
    }

    /**
     * Switch to the Title screen.
     * @param v
     */
    public void switchToTitle(View v){
        Intent intent = new Intent(this, AMazeActivity.class);
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