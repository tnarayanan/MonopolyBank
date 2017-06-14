package com.apps.tejasnarayanan.monopolybank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                moneyLabel.setText(String.valueOf(dataSnapshot.getValue()));
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
                /*Intent i = new Intent(getApplicationContext(), PropertyActivity.class);
                startActivity(i);*/
            }
        });

    }


}
