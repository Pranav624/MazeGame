package edu.wm.cs.cs301.pranavgonepalli;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;
import android.widget.Toast;
import android.widget.AdapterView;

public class AMazeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String[] algorithms = {"Manual", "DFS", "Prim", "Boruvka"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);
        Spinner s = findViewById(R.id.maze_generator);
        s.setOnItemSelectedListener(this);

        ArrayAdapter a = new ArrayAdapter(this, android.R.layout.simple_spinner_item, algorithms);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s.setAdapter(a);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id){
        Toast.makeText(getApplicationContext(), algorithms[position], Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0){

    }
}