package com.android.gaoyun.mfpit_fight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class RoundActivity extends AppCompatActivity {

    boolean transition = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        setTitle(R.string.app_title);

        try(Socket serverSocket= new Socket(InetAddress.getLocalHost(), 50000)) {

            System.out.println("Connection to server");

            InputStream inputStream = serverSocket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            System.out.println("Input stream initialized.");

            transition = dataInputStream.readBoolean();

            dataInputStream.close();
            inputStream.close();

            if(transition){
                System.out.println("Server answered for join sec. player");
                Intent gameIntent = new Intent(RoundActivity.this, GameActivity.class);
                startActivity(gameIntent);
                finishActivity(0);
            }

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


    public void staffClickRound(View view) {

        System.out.println("Staff Only!");
        Intent gameIntent = new Intent(RoundActivity.this, GameActivity.class);
        startActivity(gameIntent);
        finishActivity(1);

    }
}
