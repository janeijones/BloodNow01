package zetrixweb.com.bloodnow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zetrixweb.com.bloodnow.R;
import zetrixweb.com.bloodnow.extra.CommonUtils;
import zetrixweb.com.bloodnow.pojo.DeviceId;
import zetrixweb.com.bloodnow.pojo.RecordDonationPojo;
import zetrixweb.com.bloodnow.pojo.SignupPojo;

public class MainActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView header;
    private EditText etEmail;
    private EditText etPassword;
    private TextView login;
    private TextView emergency,register,forgot;

    private FirebaseAuth auth;

    ArrayList<SignupPojo> getPlayerResponsePojos;

    DatabaseReference ref;
    ArrayList<String> keyList;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
//        DatabaseReference.setAndroidContext(MainActivity.this);

        ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://blood-now.firebaseio.com/path/to/data");;

        logo = (ImageView) findViewById(R.id.logo);
        header = (TextView) findViewById(R.id.header);
        etEmail = (EditText) findViewById(R.id.input_username);
        etPassword = (EditText) findViewById(R.id.input_password);
        login = (TextView) findViewById(R.id.login);
        emergency = (TextView) findViewById(R.id.emergency);
        register = (TextView) findViewById(R.id.ma_tvRegister);
        forgot = (TextView) findViewById(R.id.forgot_pass);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean flag = true;
                if(flag){
                    if(isValid()){
                        //authenticate user
                        auth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // If sign in fails, display a message to the user.
                                        if (!task.isSuccessful()) {
                                            // there was an error
                                            if (etPassword.getText().toString().length() < 6) {
                                                etPassword.setError(getString(R.string.minimum_password));
                                            } else {
                                                Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                            }
                                        } else {

                                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                            key = etEmail.getText().toString().replaceAll("@", "_");
                                            key = key.replaceAll("\\.", "_");
                                            ref.child("SignupPojo").orderByKey().equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    GenericTypeIndicator<Map<String, SignupPojo>> genericTypeIndicator = new GenericTypeIndicator<Map<String, SignupPojo>>() {};
                                                    Map<String, SignupPojo> map = dataSnapshot.getValue(genericTypeIndicator);

                                                    final SignupPojo signupPojo= map.get(key);
//                                                dataSnapshot.getChildren().iterator().next().getKey();
                                                    CommonUtils.setStringSharedPref(MainActivity.this,CommonUtils.SF_LOGIN_DATA2, CommonUtils.convertCalGoalToJson(signupPojo));
//                                                getPlayerResponsePojos = CommonUtils.convertJsonToCalGoal(CommonUtils.getStringPref(MainActivity.this,CommonUtils.SF_LOGIN_DATA1,null));

                                                    String deviceId = Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                                                    String email = etEmail.getText().toString();
                                                    String token = FirebaseInstanceId.getInstance().getToken();

                                                    //Creating Person object
                                                    DeviceId deviceIdpojo = new DeviceId();
                                                    deviceIdpojo.setEmail(email);
                                                    deviceIdpojo.setToken(token);
                                                    deviceIdpojo.setCity(signupPojo.getCity());
                                                    //Adding values

                                                    //Storing values to firebase
                                                    ref.child("UserTokens").child(deviceId).setValue(deviceIdpojo);

                                                    //Value event listener for realtime data update
                                                    ref.child("UserTokens").addListenerForSingleValueEvent(new ValueEventListener() {

                                                        @Override
                                                        public void onDataChange(DataSnapshot snapshot) {
                                                /*for (DataSnapshot postSnapshot : snapshot.getChildren()) {*/
                                                            //UserTokenPojo person = snapshot.getValue(UserTokenPojo.class);
                                                            Log.d("data",snapshot.toString());

                                                            if(signupPojo.getCounter() > 0){
                                                                //Value event listener for realtime data update
                                                                ref.child("RecordDonation").addListenerForSingleValueEvent(new ValueEventListener() {

                                                                    @Override
                                                                    public void onDataChange(DataSnapshot snapshot) {
                                                                        Log.d("data",snapshot.toString());

                                                                        GenericTypeIndicator<Map<String, RecordDonationPojo>> genericTypeIndicator = new GenericTypeIndicator<Map<String, RecordDonationPojo>>() {};
                                                                        Map<String, RecordDonationPojo> map = snapshot.getValue(genericTypeIndicator);

                                                                        ArrayList<String> strings = new ArrayList<String>(map.keySet());
                                                                        Collections.sort(strings);
                                                                        RecordDonationPojo recordDonationPojo1= map.get(strings.get(strings.size()-1));

//                                                dataSnapshot.getChildren().iterator().next().getKey();
                                                                        CommonUtils.setStringSharedPref(MainActivity.this,CommonUtils.SF_LOGIN_DATA1, CommonUtils.convertDonationToJson(recordDonationPojo1));
//
                                                                        Intent intent = new Intent(MainActivity.this, UserPageActivity.class);
                                                                        startActivity(intent);

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError firebaseError) {
                                                                        System.out.println("The read failed: " + firebaseError.getMessage());
                                                                        Intent intent = new Intent(MainActivity.this, UserPageActivity.class);
                                                                        startActivity(intent);
                                                                    }

                                                                });

                                                            }
                                                            else {
                                                                Intent intent = new Intent(MainActivity.this, UserPageActivity.class);
                                                                startActivity(intent);
                                                            }


                                                /*}*/
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError firebaseError) {
                                                            System.out.println("The read failed: " + firebaseError.getMessage());
                                                        }

                                                    });

                                                    etEmail.setText("");
                                                    etPassword.setText("");

//                                              Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
//                                              startActivity(intent);
//                                              finish();

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }

                                    }
                                });
                    }
                    login.setEnabled(false);

                }
                login.setEnabled(true);
                flag = false;


                if(isValid())
                {

                }


            }
        });

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmergencyActivity.class);
                startActivity(intent);
            }
        });

//        ref.child("SignupPojo").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                GenericTypeIndicator<Map<String, SignupPojo>> genericTypeIndicator = new GenericTypeIndicator<Map<String, SignupPojo>>() {};
//                Map<String, SignupPojo> map = dataSnapshot.getValue(genericTypeIndicator );
//                keyList = new ArrayList<>(map.keySet());
//                //SignupPojo mapp = map.get(0);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    private boolean isValid(){

        boolean isValid = true;

        if (TextUtils.isEmpty(etEmail.getText().toString())){
            etEmail.setError("Email cannot be empty");
            isValid=false;

        }else if(!isValidEmail(etEmail.getText().toString())){
            etEmail.setError("Enter Valid Email");
            isValid=false;

        }

        if (TextUtils.isEmpty(etPassword.getText().toString())){
            etPassword.setError("Password cannot be empty");
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
