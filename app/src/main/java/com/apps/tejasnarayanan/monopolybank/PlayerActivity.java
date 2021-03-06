package com.apps.tejasnarayanan.monopolybank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    TextView statusLabel;
    TextView moneyLabel;
    TextView worthLabel;
    TextView ownedPropertyLabel;

    ListView listView;

    ArrayList<String> ownedProperties = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference().child(JoinGameActivity.code).child(ChooseNameActivity.name);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        statusLabel = (TextView) findViewById(R.id.statusLabel);
        moneyLabel = (TextView) findViewById(R.id.moneyLabel);
        worthLabel = (TextView) findViewById(R.id.worthLabel);
        ownedPropertyLabel = (TextView) findViewById(R.id.ownedPropertyLabel);

        listView = (ListView) findViewById(R.id.listView);

        statusLabel.setText("Code: " + JoinGameActivity.code + "        " + ChooseNameActivity.name);

        reference.child("Property").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ownedProperties.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    ownedProperties.add(childSnapshot.getKey() + " (" + childSnapshot.getValue() + ")");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(PlayerActivity.this, android.R.layout.simple_list_item_1, ownedProperties);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reference.child("Money").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                moneyLabel.setText("$" + String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {}
}
