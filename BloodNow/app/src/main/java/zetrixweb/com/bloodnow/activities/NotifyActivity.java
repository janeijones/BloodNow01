package zetrixweb.com.bloodnow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import zetrixweb.com.bloodnow.R;
import zetrixweb.com.bloodnow.extra.CommonUtils;
import zetrixweb.com.bloodnow.pojo.SignupPojo;

public class NotifyActivity extends AppCompatActivity implements View.OnClickListener{

    SignupPojo signupPojo;
    Intent intent;
    String bloodg,city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        intent = getIntent();
        bloodg = intent.getStringExtra("bg");
        city = intent.getStringExtra("city");
        signupPojo = CommonUtils.convertJsonToCalGoal(CommonUtils.getStringPref(NotifyActivity.this,CommonUtils.SF_LOGIN_DATA2,""));

        findViewById(R.id.na_btnInerested).setOnClickListener(this);
        findViewById(R.id.na_btnNotInerested).setOnClickListener(this);
        findViewById(R.id.na_btnNotEligible).setOnClickListener(this);
        findViewById(R.id.na_btnClose).setOnClickListener(this);

        ((TextView)findViewById(R.id.head)).setText("A Patient in the " + city + " city needs "+ bloodg + " Blood. If you are interested please click interested below.");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.na_btnInerested:

                findViewById(R.id.llDetail).setVisibility(View.VISIBLE);

                break;

            case R.id.na_btnNotInerested:
                finish();

                break;

            case R.id.na_btnNotEligible:
                finish();

                break;

            case R.id.na_btnClose:
                Intent loginIntent = new Intent(NotifyActivity.this, MainActivity.class);
                startActivity(loginIntent);
                finish();

                break;
        }
    }
}
