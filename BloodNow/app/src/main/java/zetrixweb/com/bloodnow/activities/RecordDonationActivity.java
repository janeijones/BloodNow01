package zetrixweb.com.bloodnow.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import cn.jeesoft.widget.pickerview.CharacterPickerWindow;
import zetrixweb.com.bloodnow.R;
import zetrixweb.com.bloodnow.extra.CommonUtils;
import zetrixweb.com.bloodnow.extra.OptionsWindowHelper;
import zetrixweb.com.bloodnow.pojo.RecordDonationPojo;
import zetrixweb.com.bloodnow.pojo.SignupPojo;

import static zetrixweb.com.bloodnow.extra.OptionsWindowHelper.options3Items;

public class RecordDonationActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth auth;

    private RadioGroup raRgGender;
    private RadioButton raRbMale;
    private RadioButton raRbFemale;
    private RadioButton raRbOther;
    private LinearLayout llBottom;
    private TextView rdaRecord;
    private TextView rdaReset;
    private TextView rdaCancel;
    RadioGroup rgGender;
    private EditText etPlace, etCity, etDate;
    RadioButton genderCheck;

    CharacterPickerWindow window;
    public String selectedDate;
    private RelativeLayout rlMain;

    SignupPojo signupPojo;

    String key;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_donation);

        auth = FirebaseAuth.getInstance();
//        DatabaseReference.setAndroidContext(MainActivity.this);

        ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://blood-now.firebaseio.com/path/to/data");

        rlMain = (RelativeLayout) findViewById(R.id.rlrmainn);

        signupPojo = CommonUtils.convertJsonToCalGoal(CommonUtils.getStringPref(RecordDonationActivity.this,CommonUtils.SF_LOGIN_DATA2,""));

        raRgGender = (RadioGroup) findViewById(R.id.ra_rgGender);
        raRbMale = (RadioButton) findViewById(R.id.ra_rbMale);
        raRbFemale = (RadioButton) findViewById(R.id.ra_rbFemale);
        raRbOther = (RadioButton) findViewById(R.id.ra_rbOther);
        llBottom = (LinearLayout) findViewById(R.id.llBottom);
        rdaRecord = (TextView) findViewById(R.id.rda_record);
        rdaReset = (TextView) findViewById(R.id.rda_reset);
        rdaCancel = (TextView) findViewById(R.id.rda_cancel);
        etCity = (EditText) findViewById(R.id.input_city);
        etPlace = (EditText) findViewById(R.id.input_place);
        etDate = (EditText) findViewById(R.id.input_date);
        rgGender = (RadioGroup) findViewById(R.id.typeRadio);

        rdaRecord.setOnClickListener(this);
        rdaReset.setOnClickListener(this);
        rdaCancel.setOnClickListener(this);
        etDate.setOnClickListener(this);

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(RadioGroup group, int checkedId)
                                                {
                                                    genderCheck = (RadioButton) findViewById(checkedId);
                                                    Toast.makeText(getBaseContext(), genderCheck.getText(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.rda_record:

                if(isValid()){

                    String date = etDate.getText().toString();
                    String place = etPlace.getText().toString();
                    String city = etCity.getText().toString();
                    String type = genderCheck.getText().toString();

                    //Creating Person object
                    RecordDonationPojo recordDonationPojo = new RecordDonationPojo();
                    recordDonationPojo.setDate(date);
                    recordDonationPojo.setPlace(place);
                    recordDonationPojo.setCity(city);
                    recordDonationPojo.setType(type);
                    //Adding values

                    //Storing values to firebase
                    ref.child("RecordDonation").child(date).setValue(recordDonationPojo);
                    //Value event listener for realtime data update
                    ref.child("RecordDonation").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Log.d("data",snapshot.toString());

                            GenericTypeIndicator<Map<String, RecordDonationPojo>> genericTypeIndicator = new GenericTypeIndicator<Map<String, RecordDonationPojo>>() {};
                            Map<String, RecordDonationPojo> map = snapshot.getValue(genericTypeIndicator);

                            RecordDonationPojo recordDonationPojo1= map.get(key);
//                                                dataSnapshot.getChildren().iterator().next().getKey();
                            CommonUtils.setStringSharedPref(RecordDonationActivity.this,CommonUtils.SF_LOGIN_DATA1, CommonUtils.convertDonationToJson(recordDonationPojo1));
//                                                getPlayerResponsePojos = CommonUtils.convertJsonToCalGoal(CommonUtils.getStringPref(MainActivity.this,CommonUtils.SF_LOGIN_DATA1,null));

                            signupPojo.setCounter(signupPojo.getCounter()+3);

                            String key = signupPojo.getEmail().replaceAll("@","_");
                            key = key.replaceAll("\\.","_");

                            ref.child("SignupPojo").child(key).setValue(signupPojo);

                            //Value event listener for realtime data update
                            ref.child("SignupPojo").child(key).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot snapshot) {

                                    SignupPojo person = snapshot.getValue(SignupPojo.class);
//                                snapshot.getRef().child("counter").setValue(signupPojo.getCounter()+3);
                                    CommonUtils.setStringSharedPref(RecordDonationActivity.this,CommonUtils.SF_LOGIN_DATA2,CommonUtils.convertCalGoalToJson(person));
                                    setResult(RESULT_OK);
                                    finish();
                                }

                                @Override
                                public void onCancelled(DatabaseError firebaseError) {
                                    System.out.println("The read failed: " + firebaseError.getMessage());

                                }

                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError firebaseError) {
                            System.out.println("The read failed: " + firebaseError.getMessage());
                        }

                    });


                }

                break;

            case R.id.rda_reset:
                etCity.setText("");
                etPlace.setText("");
                etDate.setText("");
                break;

            case R.id.rda_cancel:
                finish();
                break;

            case R.id.input_date:
                showWindow();
                break;
        }
    }

    //Date Picker
    private void showWindow() {

        window = OptionsWindowHelper.builder(RecordDonationActivity.this, new OptionsWindowHelper.OnOptionsSelectListener() {
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
                        etDate.setError(null);
                        etDate.setText(selectedDate);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        window.showAtLocation(rlMain, Gravity.BOTTOM, 0, 0);
        window.setSelectOptions(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-1,Calendar.getInstance().get(Calendar.MONTH),options3Items.size()-15);
    }

    private boolean isValid(){

        boolean isValid = true;

        if (TextUtils.isEmpty(etDate.getText().toString())) {
            etDate.setError("Patient's Name cannot be empty");
            isValid = false;
        }

        if (TextUtils.isEmpty(etCity.getText().toString())){
            etCity.setError("phone number cannot be empty");
            isValid=false;

        }
        if (TextUtils.isEmpty(etPlace.getText().toString())){
            etPlace.setError("phone number cannot be empty");
            isValid=false;

        }
        return isValid;
    }

}
