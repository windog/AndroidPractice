package com.example.windy.androidpractices;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

/**
 * Created by windog on 2016/7/22.
 */
public class SecondActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button button2 = (Button) findViewById(R.id.btn2);
    }
}
