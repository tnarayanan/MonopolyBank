package com.apps.tejasnarayanan.monopolybank;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    ArrayList<String> ownedProperties = new ArrayList<>();
    final ArrayList<String> nonHouseProperties = new ArrayList<>(Arrays.asList(
            "Reading Railroad",
            "Pennsylvania RR",
            "B & O Railroad",
            "Short Line RR",
            "Electric Company",
            "Water Works"));

    static Map<String, Integer> housePrices;

    String currProperty;
    int currCost = 0;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference().child(JoinGameActivity.code).child(ChooseNameActivity.name);
    DatabaseReference gameReference = database.getReference().child(JoinGameActivity.code);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banker);

        initializePrices();

        statusLabel = (TextView) findViewById(R.id.statusLabelBanker);
        moneyLabel = (TextView) findViewById(R.id.moneyLabelBanker);
        worthLabel = (TextView) findViewById(R.id.worthLabelBanker);
        bankingLabel = (TextView) findViewById(R.id.bankingLabel);
        ownedPropertyLabel = (TextView) findViewById(R.id.ownedPropertyLabelBanker);

        listView = (ListView) findViewById(R.id.listViewBanker);

        moneyButton = (Button) findViewById(R.id.moneyButton);
        propertyButton = (Button) findViewById(R.id.propertyButton);


        statusLabel.setText("Code: " + JoinGameActivity.code + "        " + ChooseNameActivity.name);

        reference.child("Property").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ownedProperties.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    ownedProperties.add(childSnapshot.getKey() + " (" + childSnapshot.getValue() + ")");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(BankerActivity.this, android.R.layout.simple_list_item_1, ownedProperties);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currProperty = ((String) parent.getItemAtPosition(position));
                final int currNumOfHouses = Integer.parseInt(String.valueOf(currProperty.charAt(currProperty.length() - 2)));
                currProperty = currProperty.substring(0, currProperty.length() - 4);

                if (!nonHouseProperties.contains(currProperty)) {

                    LayoutInflater inflater = getLayoutInflater();
                    View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);

                    final TextView housesCostLabel = (TextView) alertLayout.findViewById(R.id.housesCostLabel);
                    final NumberPicker numberPicker = (NumberPicker) alertLayout.findViewById(R.id.numberPicker);

                    numberPicker.setMinValue(0);
                    numberPicker.setMaxValue(5 - currNumOfHouses);
                    numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                            currCost = newVal * housePrices.get(currProperty);
                            housesCostLabel.setText("Cost: $" + String.valueOf(currCost));
                        }
                    });


                    AlertDialog.Builder alert = new AlertDialog.Builder(BankerActivity.this);
                    alert.setTitle("Buy Houses on " + currProperty);
                    // this is set the view from XML inside AlertDialog
                    alert.setView(alertLayout);
                    // disallow cancel of AlertDialog on click of back button and outside touch
                    alert.setCancelable(false);
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    alert.setPositiveButton("Buy", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reference.child("Property").child(currProperty).setValue(currNumOfHouses + numberPicker.getValue());

                            reference.child("Money").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().setValue(dataSnapshot.getValue(Integer.class) - currCost);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                }


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

    private static void initializePrices() {
        housePrices = new HashMap<>();
        housePrices.put("Mediterranean Avenue", 50);
        housePrices.put("Baltic Avenue", 50);
        housePrices.put("Oriental Avenue", 50);
        housePrices.put("Vermont Avenue", 50);
        housePrices.put("Connecticut Avenue", 50);
        housePrices.put("St Charles Place", 100);
        housePrices.put("States Avenue", 100);
        housePrices.put("Virginia Avenue", 100);
        housePrices.put("St James Place", 100);
        housePrices.put("Tennessee Avenue", 100);
        housePrices.put("New York Avenue", 100);
        housePrices.put("Kentucky Avenue", 150);
        housePrices.put("Indiana Avenue", 150);
        housePrices.put("Illinois Avenue", 150);
        housePrices.put("Atlantic Avenue", 150);
        housePrices.put("Ventnor Avenue", 150);
        housePrices.put("Marvin Gardens", 150);
        housePrices.put("Pacific Avenue", 200);
        housePrices.put("North Carolina Avenue", 200);
        housePrices.put("Pennsylvania Avenue", 200);
        housePrices.put("Park Place", 200);
        housePrices.put("Boardwalk", 200);
        housePrices.put("Reading Railroad", 0);
        housePrices.put("Pennsylvania RR", 0);
        housePrices.put("B & O Railroad", 0);
        housePrices.put("Short Line RR", 0);
        housePrices.put("Electric Company", 0);
        housePrices.put("Water Works", 0);
    }


}
