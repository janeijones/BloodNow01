package zetrixweb.com.bloodnow.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jeesoft.widget.pickerview.CharacterPickerWindow;
import zetrixweb.com.bloodnow.R;
import zetrixweb.com.bloodnow.extra.CommonUtils;
import zetrixweb.com.bloodnow.extra.OptionsWindowHelper;
import zetrixweb.com.bloodnow.pojo.SignupPojo;

import static zetrixweb.com.bloodnow.extra.OptionsWindowHelper.options3Items;

public class UpdateInfoActivity extends AppCompatActivity {

    DatabaseReference ref;
    private FirebaseAuth auth;

    ArrayList<String> heightList;
    ArrayList<String> weightList;
    ArrayList<String> BloodGroupList;

//    private AppCompatSpinner uaSpBloodGroup;
    private EditText etFname,etCountry,etState,etCity,etZip,etPhone;
    private EditText etLname;
    private EditText etMname;
    private EditText etEmail;
    private EditText etDob;
    private RadioGroup uaRgSmoke;
    private RadioButton uaRbSmokeYes;
    private RadioButton uaRbSmokeNo;
    private RadioGroup uaRgDrink;
    private RadioButton uaRbDrinkYes;
    private RadioButton uaRbDrinkNo;
    private RadioGroup uaRgHealth;
    private RadioButton uaRbHealthYes;
    private RadioButton uaRbHealthNo;
    private RadioGroup uaRgGender;
    private RadioButton uaRbMale;
    private RadioButton uaRbFemale;
    private RadioButton uaRbOther;
    private AppCompatSpinner uaSpHeight;
    private AppCompatSpinner uaSpWeight;
    private TextView update;

    CharacterPickerWindow window;
    public String selectedDate;
    private RelativeLayout rlMain;

    RadioButton smokeCheck, drinkCheck, genderCheck, healthCheck;

    SignupPojo signupPojo;

    ArrayList<String> keyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);

        ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-now.firebaseio.com/path/to/data");

        rlMain = (RelativeLayout) findViewById(R.id.rlrmain);

        weightList = new ArrayList<>();
        heightList = new ArrayList<>();
        BloodGroupList = new ArrayList<>();

        etFname = (EditText) findViewById(R.id.ua_input_fname);
        etLname = (EditText) findViewById(R.id.ua_input_lname);
        etEmail = (EditText) findViewById(R.id.ua_input_email);
        etDob = (EditText) findViewById(R.id.ua_input_dob);
        etCountry = (EditText) findViewById(R.id.ua_input_country);
        etState = (EditText) findViewById(R.id.ua_input_state);
        etCity = (EditText) findViewById(R.id.ua_input_city);
        etPhone = (EditText) findViewById(R.id.ua_input_phone);
        uaRgSmoke = (RadioGroup) findViewById(R.id.ua_rgSmoke);
        uaRbSmokeYes = (RadioButton) findViewById(R.id.ua_rbSmokeYes);
        uaRbSmokeNo = (RadioButton) findViewById(R.id.ua_rbSmokeNo);
        uaRgDrink = (RadioGroup) findViewById(R.id.ua_rgDrink);
        uaRbDrinkYes = (RadioButton) findViewById(R.id.ua_rbDrinkYes);
        uaRbDrinkNo = (RadioButton) findViewById(R.id.ua_rbDrinkNo);
        uaRgHealth = (RadioGroup) findViewById(R.id.ua_rgHealth);
        uaRbHealthYes = (RadioButton) findViewById(R.id.ua_rbHealthYes);
        uaRbHealthNo = (RadioButton) findViewById(R.id.ua_rbHealthNo);
        uaRgGender = (RadioGroup) findViewById(R.id.ua_rgGender);
        uaRbMale = (RadioButton) findViewById(R.id.ua_rbMale);
        uaRbFemale = (RadioButton) findViewById(R.id.ua_rbFemale);
        uaRbOther = (RadioButton) findViewById(R.id.ua_rbOther);
        uaSpHeight = (AppCompatSpinner) findViewById(R.id.ua_spHeight);
        uaSpWeight = (AppCompatSpinner) findViewById(R.id.ua_spWeight);
        update = (TextView) findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    storeData();
                }
            }
        });

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWindow();
            }
        });

        for (int i = 42; i <= 200; i++) {
            weightList.add(" " + i + " Kg");
        }

        for (int i = 4; i <= 7; i++) {

            for(int j = 1; j<=12; j++){
                heightList.add(i + "'" + j + " inch");
            }
        }

        BloodGroupList.add("Select Blood Group");
        BloodGroupList.add("A+");
        BloodGroupList.add("A-");
        BloodGroupList.add("B+");
        BloodGroupList.add("B-");
        BloodGroupList.add("AB+");
        BloodGroupList.add("AB-");
        BloodGroupList.add("O+");
        BloodGroupList.add("O-");
        BloodGroupList.add("HH/Bombay");

        ArrayAdapter<String> heightAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                heightList);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uaSpHeight.setAdapter(heightAdapter);

        ArrayAdapter<String> weightAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                weightList);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uaSpWeight.setAdapter(weightAdapter);

//        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item,
//                BloodGroupList);
//        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        uaSpBloodGroup.setAdapter(bloodAdapter);

        uaRgSmoke.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(RadioGroup group, int checkedId)
                                               {
                                                   smokeCheck = (RadioButton) findViewById(checkedId);
                                                   Toast.makeText(getBaseContext(), smokeCheck.getText(), Toast.LENGTH_SHORT).show();
                                               }
                                           }
        );

        uaRgDrink.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(RadioGroup group, int checkedId)
                                               {
                                                   drinkCheck = (RadioButton) findViewById(checkedId);
                                                   Toast.makeText(getBaseContext(), drinkCheck.getText(), Toast.LENGTH_SHORT).show();
                                               }
                                           }
        );

        uaRgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(RadioGroup group, int checkedId)
                                                {
                                                    genderCheck = (RadioButton) findViewById(checkedId);
                                                    Toast.makeText(getBaseContext(), genderCheck.getText(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
        );

        uaRgHealth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(RadioGroup group, int checkedId)
                                                {
                                                    healthCheck = (RadioButton) findViewById(checkedId);
                                                    Toast.makeText(getBaseContext(), healthCheck.getText(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
        );

        ref.child("SignupPojo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, SignupPojo>> genericTypeIndicator = new GenericTypeIndicator<Map<String, SignupPojo>>() {};
                Map<String, SignupPojo> map = dataSnapshot.getValue(genericTypeIndicator );
//                keyList = new ArrayList<>(map.keySet());

                signupPojo = CommonUtils.convertJsonToCalGoal(CommonUtils.getStringPref(UpdateInfoActivity.this,CommonUtils.SF_LOGIN_DATA2,""));

                etFname.setText(signupPojo.getFname());
//                etMname.setText(signupPojo.getMname());
                etLname.setText(signupPojo.getLname());
                etEmail.setText(signupPojo.getEmail());
                etDob.setText(signupPojo.getDob());
                uaSpHeight.setSelection(BloodGroupList.indexOf(signupPojo.getHeight()));
                uaSpWeight.setSelection(BloodGroupList.indexOf(signupPojo.getWeight()));
                etCountry.setText(signupPojo.getCountry());
                etState.setText(signupPojo.getState());
                etCity.setText(signupPojo.getCity());
//                etZip.setText(signupPojo.getZipcode());
                etPhone.setText(signupPojo.getPhone());

                if(signupPojo.getGender().toString().equals("Female")) {
                    uaRbFemale.setChecked(true);
                }
                else if(signupPojo.getGender().toString().equals("Male")){
                    uaRbMale.setChecked(true);
                }
                else if(signupPojo.getGender().toString().equals("Other")){
                    uaRbOther.setChecked(true);
                }

                if(signupPojo.getSmoke().toString().equals("Yes")) {
                    uaRbSmokeYes.setChecked(true);
                }
                else if(signupPojo.getSmoke().toString().equals("No")){
                    uaRbSmokeNo.setChecked(true);
                }

                if(signupPojo.getDrink().toString().equals("Yes")) {
                    uaRbDrinkYes.setChecked(true);
                }
                else if(signupPojo.getDrink().toString().equals("No")){
                    uaRbDrinkNo.setChecked(true);
                }

                if(signupPojo.getHealth().toString().equals("Yes")) {
                    uaRbHealthYes.setChecked(true);
                }
                else if(signupPojo.getHealth().toString().equals("No")){
                    uaRbHealthNo.setChecked(true);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void storeData(){
        //Getting values to store
        String fname,mname,lname,email,password,dob,country,city,state,zip,phone,
                smoke,drink,health,gender,height,weight;

        fname = etFname.getText().toString();
//        mname = etMname.getText().toString();
        lname = etLname.getText().toString();
        email = etEmail.getText().toString();
        dob = etDob.getText().toString();
        country = etCountry.getText().toString();
        state = etState.getText().toString();
        city = etCity.getText().toString();
//        zip = etZip.getText().toString();
        phone = etPhone.getText().toString();
        smoke = smokeCheck.getText().toString();
        drink = drinkCheck.getText().toString();
        health = healthCheck.getText().toString();
        gender = genderCheck.getText().toString();
        height = uaSpHeight.getSelectedItem().toString();
        weight = uaSpWeight.getSelectedItem().toString();

        //Creating Person object
        SignupPojo signupPojo = new SignupPojo();

        //Adding values
        signupPojo.setFname(fname);
        signupPojo.setLname(lname);
        signupPojo.setEmail(email);
        signupPojo.setDob(dob);
        signupPojo.setCountry(country);
        signupPojo.setState(state);
        signupPojo.setCity(city);
//        signupPojo.setZipcode(zip);
        signupPojo.setPhone(phone);
        signupPojo.setSmoke(smoke);
        signupPojo.setDrink(drink);
        signupPojo.setHealth(health);
        signupPojo.setGender(gender);
        signupPojo.setHeight(height);
        signupPojo.setWeight(weight);

        //Storing values to firebase
        String key = etEmail.getText().toString().replaceAll("@", "_");
        key = key.replaceAll("\\.", "_");
        ref.child("SignupPojo").child(key).setValue(signupPojo);

        //Value event listener for realtime data update
        ref.child("SignupPojo").child(key).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                SignupPojo person = snapshot.getValue(SignupPojo.class);

//                //Adding it to a string
//                //Adding it to a string
//                String string = person.getFname();
//                String name = person.getLname();
//
//                //Displaying it on textview
//                etFname.setText(string);
////                            tvUsername.setText(name);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());

            }

        });
    }

    //Date Picker
    private void showWindow() {

        window = OptionsWindowHelper.builder(UpdateInfoActivity.this, new OptionsWindowHelper.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(String province, String city, String area) {
                Log.e("main", province + "," + city + "," + area);
                try {
                    selectedDate =province+" "+city+" "+area;
                    Date currentDate = Calendar.getInstance().getTime();
                    Date selectedD = new SimpleDateFormat("dd MMMM yyyy").parse(selectedDate);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(selectedD);
                    calendar.set(Calendar.YEAR,Calendar.getInstance().get(Calendar.YEAR)-13);
                    Date validDate = calendar.getTime();
                    if (selectedD.before(currentDate) && selectedD.before(validDate)) {
                        etDob.setError(null);
                        etDob.setText(selectedDate);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        window.showAtLocation(rlMain, Gravity.BOTTOM, 0, 0);
        window.setSelectOptions(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-1,Calendar.getInstance().get(Calendar.MONTH),options3Items.size()-15);
    }

    public boolean isValid(){
        boolean isValid = true;

        if (TextUtils.isEmpty(etFname.getText().toString())){
            etFname.setError("First name cannot be empty");
            isValid=false;
        }

        if (TextUtils.isEmpty(etLname.getText().toString())){
            etLname.setError("Last Name cannot be empty");
            isValid=false;
        }

        if (TextUtils.isEmpty(etEmail.getText().toString())){
            etEmail.setError("Email cannot be empty");
            isValid=false;
        }

        if (TextUtils.isEmpty(etEmail.getText().toString())){
            etEmail.setError("Email cannot be empty");
            isValid=false;

        }else if(!isValidEmail(etEmail.getText().toString())){
            etEmail.setError("Enter Valid Email Address");
            isValid=false;
        }

        if (TextUtils.isEmpty(etPhone.getText().toString())){
            etPhone.setError("Please enter phone number");
            isValid=false;
        }

        if (TextUtils.isEmpty(etDob.getText().toString())){
            etDob.setError("Select your date of birth");
            isValid=false;
        }

        if (TextUtils.isEmpty(etCountry.getText().toString())){
            etCountry.setError("Please select Country");
            isValid=false;
        }

        if (TextUtils.isEmpty(etCity.getText().toString())){
            etCity.setError("City Name cannot be empty");
            isValid=false;
        }

        return isValid;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
