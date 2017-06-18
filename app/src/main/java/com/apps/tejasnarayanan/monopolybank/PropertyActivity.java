package com.apps.tejasnarayanan.monopolybank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class PropertyActivity extends AppCompatActivity {

    TextView titleLabel;
    TextView playerLabel;
    TextView colorLabel;
    TextView propertyLabel;

    Spinner playerSpinner;
    Spinner colorSpinner;
    Spinner propertySpinner;

    Button doneButton;

    String selectedColor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        titleLabel = (TextView) findViewById(R.id.propertyTitleLabel);
        playerLabel = (TextView) findViewById(R.id.propertyPlayerLabel);
        colorLabel = (TextView) findViewById(R.id.propertyColorLabel);
        propertyLabel = (TextView) findViewById(R.id.propertyLabel);

        playerSpinner = (Spinner) findViewById(R.id.propertyPlayerSpinner);
        colorSpinner = (Spinner) findViewById(R.id.propertyColorSpinner);
        propertySpinner = (Spinner) findViewById(R.id.propertySpinner);

        doneButton = (Button) findViewById(R.id.propertyDoneButton);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ChooseNameActivity.players);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerSpinner.setAdapter(spinnerAdapter);

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedColor = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        propertySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedColor.equals("")) {
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, ChooseNameActivity.players);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    propertySpinner.setAdapter(spinnerAdapter);
                }
            }
        });

    }
}
