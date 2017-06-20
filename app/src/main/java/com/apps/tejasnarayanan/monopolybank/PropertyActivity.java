package com.apps.tejasnarayanan.monopolybank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PropertyActivity extends AppCompatActivity {

    TextView titleLabel;
    TextView playerLabel;
    TextView propertyLabel;
    TextView costLabel;

    Spinner playerSpinner;
    Spinner propertySpinner;

    Button doneButton;

    String selectedProperty;

    String selectedPlayer;

    int selectedCost;


    ArrayList<String> availableNames = new ArrayList<>();

    static Map<String, Integer> prices;



    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference().child(JoinGameActivity.code);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        initializePrices();

        titleLabel = (TextView) findViewById(R.id.propertyTitleLabel);
        playerLabel = (TextView) findViewById(R.id.propertyPlayerLabel);
        propertyLabel = (TextView) findViewById(R.id.propertyLabel);
        costLabel = (TextView) findViewById(R.id.propertyCostLabel);

        playerSpinner = (Spinner) findViewById(R.id.propertyPlayerSpinner);
        propertySpinner = (Spinner) findViewById(R.id.propertySpinner1);

        doneButton = (Button) findViewById(R.id.propertyDoneButton);

        ArrayList<String> playersNoBank = (ArrayList<String>) ChooseNameActivity.players.clone();
        playersNoBank.remove("Bank");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, playersNoBank);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerSpinner.setAdapter(spinnerAdapter);

        reference.child("Bank").child("Property").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    availableNames.add(childSnapshot.getKey());
                }

                ArrayAdapter<String> propertyAdapter = new ArrayAdapter<>(PropertyActivity.this, android.R.layout.simple_spinner_item, availableNames);
                propertyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                propertySpinner.setAdapter(propertyAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        playerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPlayer = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        propertySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //propertySpinner.setSelection(position);
                selectedProperty = (String) parent.getItemAtPosition(position);
                selectedCost = prices.get(selectedProperty);
                costLabel.setText("Cost: $" + String.valueOf(selectedCost));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                reference.child(selectedPlayer).child("Property").child(selectedProperty).setValue(0);

                reference.child("Bank").child("Property").child(selectedProperty).removeValue();

                reference.child(selectedPlayer).child("Money").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().setValue(dataSnapshot.getValue(Integer.class) - selectedCost);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent i = new Intent(getApplicationContext(), BankerActivity.class);
                startActivity(i);
            }
        });




    }

    private static void initializePrices() {
        prices = new HashMap<>();
        prices.put("Mediterranean Avenue", 60);
        prices.put("Baltic Avenue", 60);
        prices.put("Oriental Avenue", 100);
        prices.put("Vermont Avenue", 100);
        prices.put("Connecticut Avenue", 120);
        prices.put("St Charles Place", 140);
        prices.put("States Avenue", 140);
        prices.put("Virginia Avenue", 160);
        prices.put("St James Place", 180);
        prices.put("Tennessee Avenue", 180);
        prices.put("New York Avenue", 200);
        prices.put("Kentucky Avenue", 220);
        prices.put("Indiana Avenue", 220);
        prices.put("Illinois Avenue", 240);
        prices.put("Atlantic Avenue", 260);
        prices.put("Ventnor Avenue", 260);
        prices.put("Marvin Gardens", 280);
        prices.put("Pacific Avenue", 300);
        prices.put("North Carolina Avenue", 300);
        prices.put("Pennsylvania Avenue", 320);
        prices.put("Park Place", 350);
        prices.put("Boardwalk", 400);
        prices.put("Reading Railroad", 200);
        prices.put("Pennsylvania RR", 200);
        prices.put("B & O Railroad", 200);
        prices.put("Short Line RR", 200);
        prices.put("Electric Company", 150);
        prices.put("Water Works", 150);
    }
}
