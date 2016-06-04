package com.android.gaoyun.mfpit_fight;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FinishActivity extends AppCompatActivity {

    String winner = "";

    TextView headText;
    TextView myNum;
    TextView oppNum;

    int myScore;
    int oppScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        headText = (TextView)findViewById(R.id.scoreText);
        myNum = (TextView)findViewById(R.id.myScoreNumText);
        oppNum = (TextView)findViewById(R.id.opponentScoreNumText);

        Thread listenThread = new Thread(null, doBackgroundListening,"BackgroundListen");

        listenThread.start();



    }

    private Runnable doBackgroundListening = new Runnable()
    {

        @Override
        public void run() {

            try{

                InputStream inputStream = RoundActivity.soc.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);
                System.out.println("Finish input stream initialized.");

                winner = dataInputStream.readUTF();
                System.out.println("Give winnerID: " + winner);

                String id = RoundActivity.client_id.toString();

                if((winner.equals(id))||(winner.equals(""))){

                    myScore = dataInputStream.readInt();
                    oppScore = dataInputStream.readInt();

                    System.out.println("Get scores (win or draw)");

                }else{

                    oppScore = dataInputStream.readInt();
                    myScore = dataInputStream.readInt();

                    System.out.println("Get scores (lose)");

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String newHeader = headText.getText().toString();

                        if (myScore>oppScore) {

                            headText.setText(newHeader += "\nВы победили!");

                        } else if (myScore==oppScore) {

                            headText.setText(newHeader += "\nНичья!");

                        } else {

                            headText.setText(newHeader += "\nВы проиграли!");

                        }

                        TextView myNum = (TextView)findViewById(R.id.myScoreNumText);
                        TextView oppNum = (TextView)findViewById(R.id.opponentScoreNumText);

                        String scoreMy = myScore+"";
                        String scroOpp = oppScore+"";

                        myNum.setText(scoreMy);
                        oppNum.setText(scroOppФвв);

                    }

                });

                try {
                    RoundActivity.soc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    public void finishClick(View view) {

        Toast toast = Toast.makeText(getApplicationContext(),
                "Пока-пока ;)", Toast.LENGTH_SHORT);
        toast.show();

        finish();

    }
}
