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
import java.util.Arrays;

public class ChooseNameActivity extends AppCompatActivity {

    public static ArrayList<String> players = new ArrayList<>();
    public static String name;

    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference reference = database.getReference().child(JoinGameActivity.code);

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
                reference.child(name).child("Property").setValue(new ArrayList<Property>());

                reference.child("Bank").child("Money").setValue(999999);

                writeProperty();

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

    private static void writeProperty() {
        reference.child("Bank").child("Property").child("Mediterranean Avenue").setValue(0);
        reference.child("Bank").child("Property").child("Baltic Avenue").setValue(0);
        reference.child("Bank").child("Property").child("Oriental Avenue").setValue(0);
        reference.child("Bank").child("Property").child("Vermont Avenue").setValue(0);
        reference.child("Bank").child("Property").child("Connecticut Avenue").setValue(0);
        reference.child("Bank").child("Property").child("St Charles Place").setValue(0);
        reference.child("Bank").child("Property").child("States Avenue").setValue(0);
        reference.child("Bank").child("Property").child("Virginia Avenue").setValue(0);
        reference.child("Bank").child("Property").child("St James Place").setValue(0);
        reference.child("Bank").child("Property").child("Tennessee Avenue").setValue(0);
        reference.child("Bank").child("Property").child("New York Avenue").setValue(0);
        reference.child("Bank").child("Property").child("Kentucky Avenue").setValue(0);
        reference.child("Bank").child("Property").child("Indiana Avenue").setValue(0);
        reference.child("Bank").child("Property").child("Illinois Avenue").setValue(0);
        reference.child("Bank").child("Property").child("Atlantic Avenue").setValue(0);
        reference.child("Bank").child("Property").child("Ventnor Avenue").setValue(0);
        reference.child("Bank").child("Property").child("Marvin Gardens").setValue(0);
        reference.child("Bank").child("Property").child("Pacific Avenue").setValue(0);
        reference.child("Bank").child("Property").child("North Carolina Avenue").setValue(0);
        reference.child("Bank").child("Property").child("Pennsylvania Avenue").setValue(0);
        reference.child("Bank").child("Property").child("Park Place").setValue(0);
        reference.child("Bank").child("Property").child("Boardwalk").setValue(0);
        reference.child("Bank").child("Property").child("Reading Railroad").setValue(0);
        reference.child("Bank").child("Property").child("Pennsylvania RR").setValue(0);
        reference.child("Bank").child("Property").child("B & O Railroad").setValue(0);
        reference.child("Bank").child("Property").child("Short Line RR").setValue(0);
        reference.child("Bank").child("Property").child("Electric Company").setValue(0);
        reference.child("Bank").child("Property").child("Water Works").setValue(0);
    }
}
