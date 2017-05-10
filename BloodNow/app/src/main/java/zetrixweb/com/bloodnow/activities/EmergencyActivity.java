package zetrixweb.com.bloodnow.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import zetrixweb.com.bloodnow.R;

public class EmergencyActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etRequestor, etPhone, etHospital, etCity, etPName, etPGender, etPAge, etBAmt, etIncident;
    TextView tvRegister, tvReset, tvCancel,tvRegister2, tvReset2, tvCancel2;
    AppCompatSpinner spBloodGroup,spAge;

    ArrayList<String> bloodArraylist;
    ArrayList<String> ageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        etRequestor = (EditText) findViewById(R.id.input_RequestorNm);
        etPhone = (EditText) findViewById(R.id.input_phoneNo);
        etCity = (EditText) findViewById(R.id.input_city);
        spBloodGroup = (AppCompatSpinner) findViewById(R.id.ea_spBloodGroup);
        spAge = (AppCompatSpinner) findViewById(R.id.pda_spAge);
//        etHospital = (EditText) findViewById(R.id.input_hospitalNm);
//        etWard = (EditText) findViewById(R.id.input_ward);
        etPName = (EditText) findViewById(R.id.pda_input_patientNm);
//        etPGender = (EditText) findViewById(R.id.input_patientSex);
//        etPAge = (EditText) findViewById(R.id.input_patientAge);
//        etBAmt = (EditText) findViewById(R.id.input_bloodAmt);
        etIncident = (EditText) findViewById(R.id.pda_input_incident);

        tvRegister = (TextView) findViewById(R.id.ea_tvrequest);
        tvReset = (TextView) findViewById(R.id.reset);
        tvCancel = (TextView) findViewById(R.id.cancel);
        tvRegister2 = (TextView) findViewById(R.id.pda_tvrequest);
        tvReset2 = (TextView) findViewById(R.id.pda_reset);
        tvCancel2 = (TextView) findViewById(R.id.pda_cancel);

        bloodArraylist = new ArrayList<>();
        bloodArraylist.add("Select Blood Group");
        bloodArraylist.add("A+");
        bloodArraylist.add("A-");
        bloodArraylist.add("B+");
        bloodArraylist.add("B-");
        bloodArraylist.add("O+");
        bloodArraylist.add("O-");
        bloodArraylist.add("AB+");
        bloodArraylist.add("AB-");

        ageList = new ArrayList<>();
        ageList.add("Select Your Age");
        for (int i = 18; i < 80; i++) {
            ageList.add(String.valueOf(i));
        }

        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                bloodArraylist);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBloodGroup.setAdapter(bloodAdapter);

        ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                ageList);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAge.setAdapter(ageAdapter);


        tvRegister.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvRegister2.setOnClickListener(this);
        tvReset2.setOnClickListener(this);
        tvCancel2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ea_tvrequest:
                if(isValid()) {
                    Intent intent = new Intent(EmergencyActivity.this, NotifyActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.reset:
                etRequestor.setText("");
                etPhone.setText("");
                etHospital.setText("");
                etCity.setText("");
                etPName.setText("");
                etPGender.setText("");
                etPAge.setText("");
                etBAmt.setText("");
                etIncident.setText("");
                break;

            case R.id.cancel:
                finish();
                break;

            case R.id.pda_tvrequest:

                AlertDialog.Builder alert = new AlertDialog.Builder(EmergencyActivity.this);
                alert.setTitle("Confirm Request ?");
                alert.setCancelable(false);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(isValid()){
                            Intent intent2 = new Intent(EmergencyActivity.this,NotifyActivity.class);
                            intent2.putExtra("bg", spBloodGroup.getSelectedItem().toString());
                            intent2.putExtra("city", etCity.getText().toString());
                            startActivity(intent2);
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       dialog.cancel();
                    }
                });
                alert.show();

                break;

            case R.id.pda_reset:
                etRequestor.setText("");
                etPhone.setText("");
                etCity.setText("");
                etPName.setText("");
                etIncident.setText("");
                break;

            case R.id.pda_cancel:
                finish();
                break;
        }
    }

    public boolean isValid() {
        boolean isValid = true;

        if (TextUtils.isEmpty(etRequestor.getText().toString())) {
            etRequestor.setError("Requestor's name cannot be empty");
            isValid = false;
        }

        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            etPhone.setError("Phone number cannot be empty");
            isValid = false;
        }

//        if (TextUtils.isEmpty(etHospital.getText().toString())) {
//            etHospital.setError("Hospital name cannot be empty");
//            isValid = false;
//        }

        if (TextUtils.isEmpty(etCity.getText().toString())) {
            etCity.setError("city must be required");
            isValid = false;
        }

        if (TextUtils.isEmpty(etPName.getText().toString())) {
            etPName.setError("Patient's name cannot be empty");
            isValid = false;
        }

        if (TextUtils.isEmpty(etIncident.getText().toString())) {
            etIncident.setError("Incident mest be required");
            isValid = false;
        }

    return isValid;
    }
}
