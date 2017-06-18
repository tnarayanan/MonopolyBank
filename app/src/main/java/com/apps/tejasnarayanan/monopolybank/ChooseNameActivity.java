package com.apps.tejasnarayanan.monopolybank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseNameActivity extends AppCompatActivity {

    public static ArrayList<String> players = new ArrayList<>();
    public static String name;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference().child(JoinGameActivity.code);

    TextView chooseNameLabel;
    EditText nameField;
    Button nameButton;
    TextView codeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_name);

        chooseNameLabel = (TextView) findViewById(R.id.chooseNameLabel);
        nameField = (EditText) findViewById(R.id.nameField);
        nameButton = (Button) findViewById(R.id.nameButton);
        codeLabel = (TextView) findViewById(R.id.codeLabel);

        codeLabel.setText("Code: " + JoinGameActivity.code);

        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameField.getText().toString();

                reference.child(name).child("Money").setValue(1500);

                reference.child("Bank").setValue(999999);

                System.out.println("Added " + name);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {



                        for (DataSnapshot playerSnapshot : dataSnapshot.getChildren()) {
                            if (!players.contains(playerSnapshot.getKey())) {
                                players.add(playerSnapshot.getKey());
                            }

                        }

                        Log.e("---------", players.toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent i;
                if(JoinGameActivity.isBanker) {
                    i = new Intent(getApplicationContext(), BankerActivity.class);
                } else {
                    i = new Intent(getApplicationContext(), PlayerActivity.class);
                }

                startActivity(i);
            }
        });


    }
}
