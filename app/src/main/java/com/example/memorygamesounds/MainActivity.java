package com.example.memorygamesounds;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{


    SharedPreferences prefs;
    final String DATANAME = "MyData";
    final String INTNAME = "MyInt";
    int defaultInt = 0;
    public static int hisScore;


    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(DATANAME, MODE_PRIVATE);

        hisScore = prefs.getInt(INTNAME, defaultInt);

        TextView textHiScore = (TextView) findViewById(R.id.textHiScore);
        textHiScore.setText("Highscore: \n" + hisScore);


        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

    }
}
