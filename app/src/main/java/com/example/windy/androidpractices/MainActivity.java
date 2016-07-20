package com.example.windy.androidpractices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SketchView sketchView = (SketchView) findViewById(R.id.sketch_view);
        sketchView.startAnimation();
    }
}
