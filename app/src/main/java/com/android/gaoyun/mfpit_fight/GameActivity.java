package com.android.gaoyun.mfpit_fight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class GameActivity extends AppCompatActivity {

    RadioButton cat1;
    RadioButton cat2;
    RadioButton cat3;
    RadioButton cat4;

    TextView myScoreText;
    TextView oppScoreText;
    TextView headText;

    RadioGroup categoryGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        myScoreText = (TextView)findViewById(R.id.myScoreNumText);
        oppScoreText = (TextView)findViewById(R.id.opponentScoreNumText);
        headText = (TextView)findViewById(R.id.scoreText);

        cat1 = (RadioButton)findViewById(R.id.radioCategory1);
        cat2 = (RadioButton)findViewById(R.id.radioCategory2);
        cat3 = (RadioButton)findViewById(R.id.radioCategory3);
        cat4 = (RadioButton)findViewById(R.id.radioCategory4);

        categoryGroup = (RadioGroup)findViewById(R.id.radioGroupCategory);

    }

    public void startRound(View view) {

        Thread listenThread = new Thread(null, doBackgroundListening,"BackgroundListen");

        listenThread.start();

    }

    private Runnable doBackgroundListening = new Runnable()
    {

        @Override
        public void run()
        {
            try{

                System.out.println("Connection to server");

                OutputStream outputStream = RoundActivity.soc.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                System.out.println("Output stream initialized.");

                int id1 = cat1.getId();
                int id2 = cat2.getId();
                int id3 = cat3.getId();
                int id4 = cat4.getId();
                int checked = categoryGroup.getCheckedRadioButtonId();

                if(checked==id1) dataOutputStream.writeInt(1);
                else if(checked==id2) dataOutputStream.writeInt(2);
                else if(checked==id3) dataOutputStream.writeInt(3);
                else if(checked==id4) dataOutputStream.writeInt(4);

                System.out.println(categoryGroup.getCheckedRadioButtonId());

                Intent intent = new Intent(GameActivity.this,QuestionsActivity.class);
                startActivity(intent);

                finish();

            } catch (IOException e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Что-то пошло не так :(", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }

        }
    };
}
