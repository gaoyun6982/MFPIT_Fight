package com.android.gaoyun.mfpit_fight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;

public class RoundActivity extends AppCompatActivity {

    boolean transition = false;
    public static Socket soc = new Socket();

    public static UUID client_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        setTitle(R.string.app_title);

        Thread listenThread = new Thread(null, doBackgroundListening,"BackgroundListen");

        listenThread.start();

    }


    public void staffClickRound(View view) {

        System.out.println("Staff Only!");
        Intent gameIntent = new Intent(RoundActivity.this, GameActivity.class);
        startActivity(gameIntent);

    }

    private Runnable doBackgroundListening = new Runnable()
    {

        @Override
        public void run()
        {
            try{

                soc.bind(null);
                soc.setSoTimeout(60000);
                soc.connect(new InetSocketAddress(LauncherActivity.ipAddr, LauncherActivity.port), 60000);
                soc.setKeepAlive(true);
                System.out.println("Connection to server");

                InputStream inputStream = soc.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);
                System.out.println("Input stream initialized.");

                OutputStream outputStream = soc.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                System.out.println("Output stream initialized.");

                transition = dataInputStream.readBoolean();
                System.out.println("Transition agreed.");

                client_id = UUID.randomUUID();
                System.out.println("Client ID: " + client_id);
                dataOutputStream.writeUTF(client_id.toString());
                System.out.println("ID send.");

                if(transition){
                    System.out.println("Server answered.");
                    Intent gameIntent = new Intent(RoundActivity.this, GameActivity.class);
                    startActivity(gameIntent);
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Что-то пошло не так :(", Toast.LENGTH_SHORT);
                toast.show();
            }

            finish();
        }
    };

}
