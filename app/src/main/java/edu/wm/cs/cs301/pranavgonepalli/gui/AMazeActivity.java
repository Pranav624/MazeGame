package edu.wm.cs.cs301.pranavgonepalli.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import java.sql.Array;

import edu.wm.cs.cs301.pranavgonepalli.R;

public class AMazeActivity extends AppCompatActivity {
    private String[] algorithms = {"DFS", "Prim", "Boruvka"};
    private String[] rooms = {"Yes", "No"};
    private SeekBar skill_level;
    private Spinner generator_spinner;
    private Spinner rooms_spinner;

    /**
     * Create the Spinner that contains the generating algorithms, and ArrayAdapter.
     * The default generator is DFS.
     * Also, creates a Spinner for a yes or no choice for rooms, with the default being yes.
     * Also changes the TextView containing the maze difficulty based on the change of
     * the SeekBar.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);

        generator_spinner = (Spinner) findViewById(R.id.maze_generator);
        ArrayAdapter generator = new ArrayAdapter(this, android.R.layout.simple_spinner_item, algorithms);
        generator.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        generator_spinner.setAdapter(generator);

        rooms_spinner = (Spinner) findViewById(R.id.maze_rooms);
        ArrayAdapter room = new ArrayAdapter(this, android.R.layout.simple_spinner_item, rooms);
        room.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rooms_spinner.setAdapter(room);

        skill_level = (SeekBar) findViewById(R.id.maze_size);
        skill_level.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * When the user changes the progress of the SeekBar, it updates the
             * skill  level shown in the skill level TextView.
             * @param seekBar
             * @param i
             * @param b
             */
             @Override
             public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                 TextView maze_level = findViewById(R.id.maze_size_level);
                 maze_level.setText("" + i);
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
     * Switch from title state to generating state.
     * @param v
     */
    public void switchToGenerating(View v){
        int skill = skill_level.getProgress();
        String builder = generator_spinner.getSelectedItem().toString();
        String rooms_choice = rooms_spinner.getSelectedItem().toString();
        boolean rooms_choice_bool = true;
        if(rooms_choice.equals("No")){
            rooms_choice_bool = false;
        }
        Intent intent = new Intent(this, GeneratingActivity.class);
        intent.putExtra("skill", skill);
        intent.putExtra("builder", builder);
        intent.putExtra("rooms", rooms_choice_bool);
        startActivity(intent);
    }
}