package com.apps.tejasnarayanan.monopolybank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class JoinGameActivity extends AppCompatActivity {

    public static String code;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    TextView gameCodeLabel;
    EditText codeField;
    Button goButton;
    Button createGameButton;

    public static boolean isBanker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        gameCodeLabel = (TextView) findViewById(R.id.gameCodeLabel);
        codeField = (EditText) findViewById(R.id.codeField);
        goButton = (Button) findViewById(R.id.goButton);
        createGameButton = (Button) findViewById(R.id.createGameButton);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isBanker = false;

                code = codeField.getText().toString();

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(code)) {
                            Intent i = new Intent(getApplicationContext(), ChooseNameActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "Code does not exist", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isBanker = true;

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        code = generateRandomCode();
                        while (dataSnapshot.hasChild(code)) {
                            code = generateRandomCode();
                        }

                        Intent i = new Intent(getApplicationContext(), ChooseNameActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private static String generateRandomCode() {
        String availableChars = "abcdefghijklmnopqrstuvwxyz0123456789";
        String tempCode = "";
        for (int i = 0; i < 6; i++) {
            tempCode += availableChars.toCharArray()[(int)(Math.random() * 35)];
        }

        return tempCode;
    }
}
