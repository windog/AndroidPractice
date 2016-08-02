package com.example.windy.androidpractices.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.windy.androidpractices.R;
import com.example.windy.androidpractices.Second.SecondActivity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button2 = (Button) findViewById(R.id.btn_toActivity2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });

        SketchView sketchView = (SketchView) findViewById(R.id.sketch_view);
        sketchView.startAnimation();

        // 单例模式饿汉式
        System.out.printf(Singleton.getInstance().getClass().getSimpleName());
        // 单例模式懒汉式
        System.out.printf(Singleton1.getInstance().getClass().getSimpleName());

        // 调用默认构造函数，不带参数的
        ArrayList<String> arraylist = new ArrayList<String>();
        arraylist.add("0");
        arraylist.add("1");
        arraylist.add("2");
        arraylist.add("3");

        for (String s:arraylist) {
            Logger.d(LOG_TAG,s);
        }

        Log.d(LOG_TAG,arraylist.get(1));
    }
}
