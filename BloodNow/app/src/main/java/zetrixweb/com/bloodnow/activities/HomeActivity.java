package zetrixweb.com.bloodnow.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import zetrixweb.com.bloodnow.R;
import zetrixweb.com.bloodnow.api.ApiInterface;
import zetrixweb.com.bloodnow.api.Message;
import zetrixweb.com.bloodnow.api.NotifyData;
import zetrixweb.com.bloodnow.extra.CommonUtils;
import zetrixweb.com.bloodnow.pojo.SignupPojo;

public class HomeActivity extends AppCompatActivity {

    EditText etName, etPhone;
    AppCompatSpinner spBlood;
    TextView tvSend;

    ArrayList<String> BloodGroupList;

    DatabaseReference ref;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;
    OkHttpClient mClient;
    JSONArray jsonArray;
    SignupPojo signupPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        signupPojo = CommonUtils.convertJsonToCalGoal(CommonUtils.getStringPref(HomeActivity.this,CommonUtils.SF_LOGIN_DATA2,""));
        ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://blood-now.firebaseio.com/path/to/data");;

        //DatabaseReference.setAndroidContext(this);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseInstance.getReference();

        //mFirebaseInstance.setPersistenceEnabled(true);

        auth = FirebaseAuth.getInstance();

        mClient = new OkHttpClient();

        String refreshedToken = "";//add your user refresh tokens who are logged in with firebase.

        jsonArray = new JSONArray();
        jsonArray.put(refreshedToken);

        etName = (EditText) findViewById(R.id.ha_etName);
        etPhone = (EditText) findViewById(R.id.ha_etPhone);
        spBlood = (AppCompatSpinner) findViewById(R.id.ha_spBloodGroup);
        tvSend = (TextView) findViewById(R.id.ha_send);

        BloodGroupList = new ArrayList<>();
        BloodGroupList.add("Select Blood Group");
        BloodGroupList.add("A+");
        BloodGroupList.add("A-");
        BloodGroupList.add("B+");
        BloodGroupList.add("B-");
        BloodGroupList.add("AB+");
        BloodGroupList.add("AB-");
        BloodGroupList.add("O+");
        BloodGroupList.add("O-");

        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                BloodGroupList);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBlood.setAdapter(bloodAdapter);

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isValid()){
                    //String deviceId = Settings.Secure.getString(HomeActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                    ref.child("UserTokens").orderByChild("city").equalTo(signupPojo.getCity()).addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            ArrayList<String> strings = new ArrayList<>();

                            try {
                                Iterator<DataSnapshot> dataSnapshotIterator = dataSnapshot.getChildren().iterator();
                                while (dataSnapshotIterator.hasNext()) {
                                    HashMap value = (HashMap) dataSnapshotIterator.next().getValue();
                                    String token = value.get("token").toString();
                                    String email = value.get("email").toString();
                                    if (!signupPojo.getEmail().equals(email))
                                        strings.add(token);
                                }
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }

                            if(!strings.isEmpty()){
                                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                                logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                                httpClient.addInterceptor(new Interceptor() {
                                    @Override
                                    public okhttp3.Response intercept(Chain chain) throws IOException {

                                        Request original = chain.request();

                                        // Request customization: add request headers
                                        Request.Builder requestBuilder = original.newBuilder()
                                                .addHeader("Authorization", "key=AAAADbfPh4o:APA91bH6TYoN678vJM97ZsOD1893-tMw2Fdnr7NFSh7nyRmG-vxMJqJ1Jv4Tkavbdi_97Df63gbQpFB-3iCqR71Gx3e10Jp_C8yb8WQAoL7jPSCaKQmBMKHRuPPU_YKfn_M61yGlXe_r");
                                        Request request = requestBuilder.build();
                                        return chain.proceed(request);
                                    }
                                });

                                httpClient.addInterceptor(logging);
                                OkHttpClient client = httpClient.build();

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl("https://fcm.googleapis.com")//url of FCM message server
                                        .client(client)
                                        .addConverterFactory(GsonConverterFactory.create())//use for convert JSON file into object
                                        .build();

                                // prepare call in Retrofit 2.0
                                ApiInterface firebaseAPI = retrofit.create(ApiInterface.class);

                                //for messaging server
                                NotifyData notifydata = new NotifyData(etName.getText().toString(), "Phone No: " + etPhone.getText().toString() + "\nBlood Group: " + spBlood.getSelectedItem().toString() );

//                Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)
                                for (int i = 0; i < strings.size(); i++) {
                                    Call<Message> call2 = firebaseAPI.sendMessage(new Message(strings.get(i), notifydata));

                                    call2.enqueue(new Callback<Message>() {

                                        @Override
                                        public void onResponse(Call<Message> call, retrofit2.Response<Message> response) {
                                            Log.d("Response ", "onResponse");
                                            Toast.makeText(HomeActivity.this, "Notification sent", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<Message> call, Throwable t) {
                                            Log.d("Response ", "onFailure");
                                            Toast.makeText(HomeActivity.this, "Notification failure", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                            else {
                                Toast.makeText(HomeActivity.this, "No User in same city", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError firebaseError) {
                            Log.d("error", "");
                        }
                    });
                }
            }
        });
    }

    private boolean isValid(){

        boolean isValid = true;

        if (TextUtils.isEmpty(etName.getText().toString())) {
            etName.setError("Patient's Name cannot be empty");
            isValid = false;
        }

        if (TextUtils.isEmpty(etPhone.getText().toString())){
            etPhone.setError("phone number cannot be empty");
            isValid=false;

        }
        return isValid;
    }
}
