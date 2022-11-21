package edu.wm.cs.cs301.pranavgonepalli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class LosingActivity extends AppCompatActivity {
    private static final String TAG = "LosingActivity";
    private int energy_consumed = -1;
    private int path_length;
    private String driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);

        Intent intent = getIntent();
        path_length = intent.getIntExtra("path_length", 0);
        driver = intent.getStringExtra("driver");
        energy_consumed = 3500 - intent.getIntExtra("energy_remaining", 0);
        Log.v(TAG, "Path Length: " + path_length + ", Driver: " + driver + ", Energy Consumed: " + energy_consumed);

        TextView reason_text = findViewById(R.id.reason_text);
        reason_text.setText(intent.getStringExtra("reason"));
    }

    /**
     * Switch to the Title screen.
     * @param v
     */
    public void switchToTitle(View v){
        Intent intent = new Intent(this, AMazeActivity.class);
        startActivity(intent);
    }
}