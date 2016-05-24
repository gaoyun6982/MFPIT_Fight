package com.android.gaoyun.mfpit_fight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    TextView myScore = (TextView)findViewById(R.id.myScoreNumText);
    TextView oppScore = (TextView)findViewById(R.id.opponentScoreNumText);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }


    public void startRound(View view) {

        Intent intent = new Intent(GameActivity.this,QuestionsActivity.class);

    }
}
