package com.android.gaoyun.mfpit_fight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

public class LauncherActivity extends AppCompatActivity {

    Button newGameButton;
    public static UUID client_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        setTitle(R.string.app_title);

        newGameButton = (Button)findViewById(R.id.buttonNewGame);

    }

    public void startNewGame(View view) {

        try(Socket serverSocket= new Socket(InetAddress.getLocalHost(), 50000)) {

            System.out.println("Connection to server");

            client_id = UUID.randomUUID();
            System.out.println("Client ID: "+ client_id);

            OutputStream outputStream = serverSocket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            System.out.println("Output stream initialized.");

            dataOutputStream.writeUTF(client_id.toString());
            System.out.println("Client ID send.");

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

        Intent roundIntent = new Intent(LauncherActivity.this, RoundActivity.class);
        startActivity(roundIntent);

    }
}
