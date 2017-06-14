package com.apps.tejasnarayanan.monopolybank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MoneyActivity extends AppCompatActivity {

    TextView moneyLabel;
    TextView senderLabel;
    TextView receiverLabel;
    TextView amountLabel;

    Spinner senderSpinner;
    Spinner receiverSpinner;

    EditText amountField;

    Button doneButton;

    String sender;
    String receiver;

    int senderValue;
    int receiverValue;

    int amountChange;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference().child(JoinGameActivity.code);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        moneyLabel = (TextView) findViewById(R.id.moneyTitleLabel);
        senderLabel = (TextView) findViewById(R.id.senderLabel);
        receiverLabel = (TextView) findViewById(R.id.receiverLabel);
        amountLabel = (TextView) findViewById(R.id.amountLabel);

        senderSpinner = (Spinner) findViewById(R.id.senderSpinner);
        receiverSpinner = (Spinner) findViewById(R.id.receiverSpinner);

        amountField = (EditText) findViewById(R.id.amountField);

        doneButton = (Button) findViewById(R.id.doneButtonMoney);


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ChooseNameActivity.players);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        senderSpinner.setAdapter(spinnerAdapter);
        receiverSpinner.setAdapter(spinnerAdapter);

        senderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sender = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        receiverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                receiver = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amountChange = Integer.parseInt(amountField.getText().toString());

                final DatabaseReference senderReference = reference.child(sender);
                final DatabaseReference receiverReference = reference.child(receiver);


                if (!sender.equals("Bank")) {
                    senderReference.child("Money").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            senderValue = dataSnapshot.getValue(Integer.class);
                            System.out.println("Sender: " + senderValue);
                            dataSnapshot.getRef().setValue(senderValue - amountChange);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //senderReference.child("Money").setValue(senderValue - amountChange);
                    System.out.println(senderValue + " - " + amountChange + " = " + (senderValue - amountChange));
                }

                if (!receiver.equals("Bank")) {

                    receiverReference.child("Money").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            receiverValue = dataSnapshot.getValue(Integer.class);
                            System.out.println("Receiver: " + receiverValue);
                            dataSnapshot.getRef().setValue(receiverValue + amountChange);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //receiverReference.child("Money").setValue(receiverValue + amountChange);
                    System.out.println(receiverValue + " + " + amountChange + " = " + (receiverValue + amountChange));
                }

                Intent i = null;
                if(JoinGameActivity.isBanker) {
                    i = new Intent(getApplicationContext(), BankerActivity.class);
                } else {
                    //i = new Intent(getApplicationContext(), PlayerActivity.class);
                }

                startActivity(i);
            }
        });


    }
}
