package com.example.deltatask2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void singleplayer(View view) {
        GameView gameView=new GameView(this);
        setContentView(gameView);
    }

    public void friend(View view) {
        GameView2 gameview2 = new GameView2(this);
        setContentView(gameview2);
    }

    public void computer(View view) {
        GameView3 gameview3 = new GameView3(this);
        setContentView(gameview3);
    }
}


