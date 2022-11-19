package edu.wm.cs.cs301.pranavgonepalli;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;
import android.widget.Toast;
import android.widget.AdapterView;

import java.sql.Array;

public class AMazeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String[] algorithms = {"DFS", "Prim", "Boruvka"};
    String[] rooms = {"Yes", "No"};

    /**
     * Create the Spinner that contains the generating algorithms, and ArrayAdapter.
     * The default generator is DFS.
     * Also, creates a Spinner for a yes or no choice for rooms, with the default being yes.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);

        Spinner generator_spinner = findViewById(R.id.maze_generator);
        generator_spinner.setOnItemSelectedListener(this);
        ArrayAdapter generator = new ArrayAdapter(this, android.R.layout.simple_spinner_item, algorithms);
        generator.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        generator_spinner.setAdapter(generator);

        Spinner rooms_spinner = findViewById(R.id.maze_rooms);
        rooms_spinner.setOnItemSelectedListener(this);
        ArrayAdapter room = new ArrayAdapter(this, android.R.layout.simple_spinner_item, rooms);
        room.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rooms_spinner.setAdapter(room);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id){

    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0){

    }
}