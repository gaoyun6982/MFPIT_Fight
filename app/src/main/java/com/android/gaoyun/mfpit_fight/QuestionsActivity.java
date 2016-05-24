package com.android.gaoyun.mfpit_fight;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {

    TextView questionText;
    RadioButton ans1;
    RadioButton ans2;
    RadioButton ans3;
    RadioButton ans4;

    ArrayList<String> questionCombination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        questionText = (TextView)findViewById(R.id.questionText);
        ans1 = (RadioButton)findViewById(R.id.ans1Text);
        ans2 = (RadioButton)findViewById(R.id.ans2Text);
        ans3 = (RadioButton)findViewById(R.id.ans3Text);
        ans4 = (RadioButton)findViewById(R.id.ans4Text);

        giveQuestion();

    }

    private void giveQuestion(){

        try(Socket serverSocket= new Socket(InetAddress.getLocalHost(), 50000)) {

            System.out.println("Connection to server");

            InputStream inputStream = serverSocket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            System.out.println("Input stream initialized.");

            questionCombination.add(0, dataInputStream.readUTF());
            questionCombination.add(1, dataInputStream.readUTF());
            questionCombination.add(2, dataInputStream.readUTF());
            questionCombination.add(3, dataInputStream.readUTF());
            questionCombination.add(4, dataInputStream.readUTF());

            dataInputStream.close();
            inputStream.close();


        } catch (UnknownHostException e) {
            System.out.println("Host exception.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO exception.");
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("Another exception");
            e.printStackTrace();
        };

    }

    private void setQuestionPosition(ArrayList<String> questions){

        int[] positions = new int[4];
        for(int i=0; i<4; i++) positions[i]=i+1;



    }

    public void answerQuestion(View view) {

        try(Socket serverSocket= new Socket(InetAddress.getLocalHost(), 50000)) {


            OutputStream answerStream = serverSocket.getOutputStream();
            DataOutputStream answerDataStream = new DataOutputStream(answerStream);

            RadioGroup ansGroup = (RadioGroup) findViewById(R.id.answers);
            RadioButton ans = (RadioButton) findViewById(ansGroup.getCheckedRadioButtonId());
            String answer = ans.getText().toString();

            if (answer.equals(questionCombination.get(1))) answerDataStream.writeInt(1);
            else answerDataStream.writeInt(0);

            answerDataStream.close();
            answerStream.close();

        } catch (UnknownHostException e) {
            System.out.println("Host exception.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO exception.");
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("Another exception");
            e.printStackTrace();
        };

    }
}
