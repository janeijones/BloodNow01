package zetrixweb.com.bloodnow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jeesoft.widget.pickerview.CharacterPickerWindow;
import okhttp3.OkHttpClient;
import zetrixweb.com.bloodnow.R;
import zetrixweb.com.bloodnow.extra.OptionsWindowHelper;
import zetrixweb.com.bloodnow.extra.OptionsWindowHelperr;
import zetrixweb.com.bloodnow.pojo.SignupPojo;

import static zetrixweb.com.bloodnow.extra.OptionsWindowHelper.options3Items;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{

    private AppCompatSpinner spHeight,spWeight,spBGroup;
    private TextView register;

    Calendar myCalendar;
    private SimpleDateFormat dateFormatter;

    ArrayList<String> heightList;
    ArrayList<String> weightList;
    ArrayList<String> BloodGroupList;

    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;
    OkHttpClient mClient;
    DatabaseReference ref;
    JSONArray jsonArray;

    CharacterPickerWindow window;
    public String selectedDate;
    private RelativeLayout rlMain;

    RadioButton smokeCheck, drinkCheck, genderCheck, healthCheck;

    RadioGroup rgSmoke,rgDrink,rgHealth,rgGender;

    private EditText etFname,etLname,etEmail,etPassword,etCpassword,etDob,etBloodGroup,etState,
            etCountry,etCity,etZip,etPhone,etDesc,etMname;

    private FirebaseAuth auth;
    ArrayList<String> keyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();

        ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://blood-now.firebaseio.com/path/to/data");;

        mDatabase = mFirebaseInstance.getReference();

        weightList = new ArrayList<>();
        heightList = new ArrayList<>();
        BloodGroupList = new ArrayList<>();

        rlMain = (RelativeLayout) findViewById(R.id.rlMain);

        rgDrink = (RadioGroup) findViewById(R.id.ra_rgDrink);
        rgSmoke = (RadioGroup) findViewById(R.id.ra_rgSmoke);
        rgHealth = (RadioGroup) findViewById(R.id.ra_rgHealth);
        rgGender = (RadioGroup) findViewById(R.id.ra_rgGender);

        spHeight = (AppCompatSpinner) findViewById(R.id.ra_spHeight);
        spWeight = (AppCompatSpinner) findViewById(R.id.ra_spWeight);
        spBGroup = (AppCompatSpinner) findViewById(R.id.ra_spBloodGroup);

        register = (TextView) findViewById(R.id.register);

//        etMname = (EditText) findViewById(R.id.input_mname);
        etFname = (EditText) findViewById(R.id.input_fname);
        etLname = (EditText) findViewById(R.id.input_lname);
        etEmail = (EditText) findViewById(R.id.input_email);
        etPassword = (EditText) findViewById(R.id.input_password);
//        etCpassword = (EditText) findViewById(R.id.input_cPassword);
        etDob = (EditText) findViewById(R.id.input_dob);
        spBGroup = (AppCompatSpinner) findViewById(R.id.ra_spBloodGroup);
        etState = (EditText) findViewById(R.id.input_state);
        etCountry = (EditText) findViewById(R.id.input_country);
        etCity = (EditText) findViewById(R.id.input_city);
//        etZip = (EditText) findViewById(R.id.input_zipcode);
        etPhone = (EditText) findViewById(R.id.input_phone);
        etDesc = (EditText) findViewById(R.id.desc);

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
        spHeight.setAdapter(heightAdapter);

        ArrayAdapter<String> weightAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                weightList);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeight.setAdapter(weightAdapter);

        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                BloodGroupList);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBGroup.setAdapter(bloodAdapter);

        etDob.setOnClickListener(this);
        register.setOnClickListener(this);

        mClient = new OkHttpClient();

        String refreshedToken = "";//add your user refresh tokens who are logged in with firebase.

        jsonArray = new JSONArray();
        jsonArray.put(refreshedToken);

        rgSmoke.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                  @Override
                                                  public void onCheckedChanged(RadioGroup group, int checkedId)
                                                  {
                                                      smokeCheck = (RadioButton) findViewById(checkedId);
                                                      Toast.makeText(getBaseContext(), smokeCheck.getText(), Toast.LENGTH_SHORT).show();
                                                  }
                                              }
        );

        rgDrink.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(RadioGroup group, int checkedId)
                                               {
                                                   drinkCheck = (RadioButton) findViewById(checkedId);
                                                   Toast.makeText(getBaseContext(), drinkCheck.getText(), Toast.LENGTH_SHORT).show();
                                               }
                                           }
        );

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(RadioGroup group, int checkedId)
                                               {
                                                   genderCheck = (RadioButton) findViewById(checkedId);
                                                   Toast.makeText(getBaseContext(), genderCheck.getText(), Toast.LENGTH_SHORT).show();
                                               }
                                           }
        );

        rgHealth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(RadioGroup group, int checkedId)
                                               {
                                                   healthCheck = (RadioButton) findViewById(checkedId);
                                                   Toast.makeText(getBaseContext(), healthCheck.getText(), Toast.LENGTH_SHORT).show();
                                               }
                                           }
        );

        etCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountryWindow();
            }
        });

        ref.child("SignupPojo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, SignupPojo>> genericTypeIndicator = new GenericTypeIndicator<Map<String, SignupPojo>>() {};
                Map<String, SignupPojo> map = dataSnapshot.getValue(genericTypeIndicator );
                keyList = new ArrayList<>(map.keySet());
                //SignupPojo mapp = map.get(0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.input_dob:

                showWindow();

                break;

            case R.id.register:

                if(isValid() && !keyList.contains(etPhone.getText().toString())){
                    //SignUp
                    auth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(RegistrationActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                    // If sign in fails, display a message to the user.

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegistrationActivity.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(RegistrationActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                            storeData();
                }
                else
                {
                    Toast.makeText(this, "Phone number is already registered", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void storeData(){
        //Getting values to store
        String fname,counter,lname,email,password,dob,country,city,bloodGroup,phone,
                smoke,drink,health,gender,height,weight;

        fname = etFname.getText().toString();
//        mname = etMname.getText().toString();
        lname = etLname.getText().toString();
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        counter = "0";
        dob = etDob.getText().toString();
        country = etCountry.getText().toString();
        city = etCity.getText().toString();
//        zip = etZip.getText().toString();
        bloodGroup = spBGroup.getSelectedItem().toString();
        phone = etPhone.getText().toString();
        smoke = smokeCheck.getText().toString();
        drink = drinkCheck.getText().toString();
        health = healthCheck.getText().toString();
        gender = genderCheck.getText().toString();
        height = spHeight.getSelectedItem().toString();
        weight = spWeight.getSelectedItem().toString();

        //Creating Person object
        SignupPojo signupPojo = new SignupPojo();

        //Adding values
        signupPojo.setFname(fname);
        signupPojo.setLname(lname);
        signupPojo.setEmail(email);
        signupPojo.setPassword(password);
        signupPojo.setDob(dob);
        signupPojo.setCountry(country);
        signupPojo.setCity(city);
//        signupPojo.setZipcode(zip);
        signupPojo.setPhone(phone);
        signupPojo.setBloodGroup(bloodGroup);
        signupPojo.setSmoke(smoke);
        signupPojo.setDrink(drink);
        signupPojo.setHealth(health);
        signupPojo.setGender(gender);
        signupPojo.setHeight(height);
        signupPojo.setWeight(weight);

        String key = email.replaceAll("@","_");
        key = key.replaceAll("\\.","_");
        //Storing values to firebase
        ref.child("SignupPojo").child(key).setValue(signupPojo);

        //Value event listener for realtime data update
        ref.child("SignupPojo").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                SignupPojo person = snapshot.getValue(SignupPojo.class);

                //Adding it to a string
                //Adding it to a string
                String string = person.getFname();
                String name = person.getLname();

                //Displaying it on textview
                etFname.setText(string);
//                            tvUsername.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());

            }
        });
    }

    //Date Picker
    private void showWindow() {

        window = OptionsWindowHelper.builder(RegistrationActivity.this, new OptionsWindowHelper.OnOptionsSelectListener() {
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

    //Country Picker
    private void showCountryWindow() {

        window = OptionsWindowHelperr.builder(RegistrationActivity.this, new OptionsWindowHelperr.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(String province,String country) {
                etCountry.setText(country);

            }
        });
        window.showAtLocation(rlMain, Gravity.BOTTOM, 0, 0);
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

        if (TextUtils.isEmpty(etPassword.getText().toString())){
            etPassword.setError("Password cannot be empty");
            isValid=false;
        }

//        if (rgDrink.getCheckedRadioButtonId() == -1)
//        {
//            drinkCheck.setError("Please Select anyone of them");
//            isValid=false;
//        }
//
//        if (rgSmoke.getCheckedRadioButtonId() == -1)
//        {
//            smokeCheck.setError("Please Select anyone of them");
//            isValid=false;
//        }
//        if (rgHealth.getCheckedRadioButtonId() == -1)
//        {
//            healthCheck.setError("Please Select anyone of them");
//            isValid=false;
//        }
//        if (rgGender.getCheckedRadioButtonId() == -1)
//        {
//            genderCheck.setError("Please Select anyone of them");
//            isValid=false;
//        }

        return isValid;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
