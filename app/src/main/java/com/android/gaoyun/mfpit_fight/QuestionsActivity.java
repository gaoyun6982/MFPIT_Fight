package com.android.gaoyun.mfpit_fight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {

    TextView questionText;
    RadioButton ans1;
    RadioButton ans2;
    RadioButton ans3;
    RadioButton ans4;

    int categories = 1;
    int score = 0;
    int questionStringNumber = 0;

    ArrayList<String> questionCombination1 = new ArrayList<>();


    Thread listenThread;
    Thread answerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        questionText = (TextView)findViewById(R.id.questionText);
        ans1 = (RadioButton)findViewById(R.id.ans1Text);
        ans2 = (RadioButton)findViewById(R.id.ans2Text);
        ans3 = (RadioButton)findViewById(R.id.ans3Text);
        ans4 = (RadioButton)findViewById(R.id.ans4Text);

        listenThread = new Thread(null, doBackgroundListening,"BackgroundListen");
        listenThread.start();

    }

    private Runnable doBackgroundListening = new Runnable() {

        @Override
        public void run() {

            getQuestion();

        }

        public void getQuestion(){
            try {

                System.out.println("Connection to server");

                InputStream inputStream = RoundActivity.soc.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);
                System.out.println("Input stream initialized.");

                String serverLoad = dataInputStream.readUTF();
                questionCombination1.add(0, serverLoad);

                serverLoad = dataInputStream.readUTF();
                questionCombination1.add(1, serverLoad);

                serverLoad = dataInputStream.readUTF();
                questionCombination1.add(2, serverLoad);

                serverLoad = dataInputStream.readUTF();
                questionCombination1.add(3, serverLoad);

                serverLoad = dataInputStream.readUTF();
                questionCombination1.add(4, serverLoad);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        int[] positions = new int[4];
                        for (int i = 0; i < 4; i++) positions[i] = i + 1;

                        questionText.setText(questionCombination1.get(0));

                        ans1 = (RadioButton) findViewById(R.id.ans1Text);
                        ans2 = (RadioButton) findViewById(R.id.ans2Text);
                        ans3 = (RadioButton) findViewById(R.id.ans3Text);
                        ans4 = (RadioButton) findViewById(R.id.ans4Text);

                        char[] ans = questionCombination1.get(1).toCharArray();
                        System.out.println(ans);
                        ans1.setText(ans, 0, ans.length);

                        ans = questionCombination1.get(2).toCharArray();
                        ans2.setText(ans, 0, ans.length);

                        ans = questionCombination1.get(3).toCharArray();
                        ans3.setText(ans, 0, ans.length);

                        ans = questionCombination1.get(4).toCharArray();
                        ans4.setText(ans, 0, ans.length);

                        System.out.println("Wait for answer.");


                    }
                });

            } catch (UnknownHostException e) {
                System.out.println("Host exception.");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("IO exception.");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Another exception");
                e.printStackTrace();
            }
            ;
        }



    };

    private Runnable answerOnButton = new Runnable() {
        @Override
        public void run() {
            try{


                OutputStream answerStream = RoundActivity.soc.getOutputStream();
                DataOutputStream answerDataStream = new DataOutputStream(answerStream);

                System.out.println("Sended score: "+score+"\nScore nulled");
                answerDataStream.writeInt(score);
                score = 0;


            } catch (UnknownHostException e) {
                System.out.println("Host exception.");
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Что-то пошло не так :(", Toast.LENGTH_SHORT);
                toast.show();
                finish();
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("IO exception.");
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Что-то пошло не так :(", Toast.LENGTH_SHORT);
                toast.show();
                finish();
                e.printStackTrace();
            } catch (Exception e){
                System.out.println("Another exception");
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Что-то пошло не так :(", Toast.LENGTH_SHORT);
                toast.show();
                e.printStackTrace();
                finish();
            };
        }
    };

    public void answerQuestion(View view) {

        answerThread = new Thread(null, answerOnButton,"AnswerThread");

        RadioGroup ansGroup = (RadioGroup) findViewById(R.id.answers);
        RadioButton ans = (RadioButton) findViewById(ansGroup.getCheckedRadioButtonId());
        String answer = ans.getText().toString();


        if (answer.equals(questionCombination1.get(1))) score++;

        System.out.println("Score " + score);

        ansGroup.clearCheck();

        if((questionStringNumber<5)&&(categories>=0)) {
            System.out.println("Setting new question num."+questionStringNumber);
            listenThread = new Thread(null, doBackgroundListening,"BackgroundListen");
            listenThread.start();
            questionStringNumber++;
        }else if(categories>0){
            categories--;
            questionStringNumber = 1;
            if(categories>=0){
                listenThread = new Thread(null, doBackgroundListening,"BackgroundListen");
                listenThread.start();
                questionStringNumber++;
            } else{

                System.out.println("Variant 1");

                answerThread.start();

                Intent intent = new Intent(QuestionsActivity.this, FinishActivity.class);
                startActivity(intent);

                finish();

            }
        }
        else{

            System.out.println("Variant 2");

            answerThread.start();

            Intent intent = new Intent(QuestionsActivity.this, FinishActivity.class);
            startActivity(intent);

            finish();

        }

    }
}
