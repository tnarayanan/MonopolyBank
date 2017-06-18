package com.apps.tejasnarayanan.monopolybank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.apps.tejasnarayanan.monopolybank.ChooseNameActivity.players;

public class BankerActivity extends AppCompatActivity {

    TextView statusLabel;
    TextView moneyLabel;
    TextView worthLabel;
    TextView bankingLabel;
    TextView ownedPropertyLabel;

    ListView listView;

    Button moneyButton;
    Button propertyButton;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference().child(JoinGameActivity.code).child(ChooseNameActivity.name);
    DatabaseReference gameReference = database.getReference().child(JoinGameActivity.code);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banker);

        statusLabel = (TextView) findViewById(R.id.statusLabelBanker);
        moneyLabel = (TextView) findViewById(R.id.moneyLabelBanker);
        worthLabel = (TextView) findViewById(R.id.worthLabelBanker);
        bankingLabel = (TextView) findViewById(R.id.bankingLabel);
        ownedPropertyLabel = (TextView) findViewById(R.id.ownedPropertyLabelBanker);

        listView = (ListView) findViewById(R.id.listViewBanker);

        moneyButton = (Button) findViewById(R.id.moneyButton);
        propertyButton = (Button) findViewById(R.id.propertyButton);


        statusLabel.setText("Code: " + JoinGameActivity.code + "        " + ChooseNameActivity.name);

        reference.child("Money").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                moneyLabel.setText("$" + String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        gameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String newPlayers = "";
                int numNewPlayers = 0;

                for (DataSnapshot playerSnapshot : dataSnapshot.getChildren()) {
                    if (!players.contains(playerSnapshot.getKey())) {
                        if (!newPlayers.equals("")) {
                            newPlayers += ", ";
                        }
                        players.add(playerSnapshot.getKey());
                        newPlayers += playerSnapshot.getKey();
                        numNewPlayers++;
                    }

                }

                if (numNewPlayers > 0) {
                    String finalString = "";
                    if (numNewPlayers == 1) {
                        finalString = "New player: " + newPlayers;
                    } else {
                        finalString = "New players: " + newPlayers;
                    }

                    Toast.makeText(getApplicationContext(), finalString, Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        moneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MoneyActivity.class);
                startActivity(i);
            }
        });

        propertyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PropertyActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {}


}
