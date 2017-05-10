package zetrixweb.com.bloodnow.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import zetrixweb.com.bloodnow.R;
import zetrixweb.com.bloodnow.extra.CommonUtils;
import zetrixweb.com.bloodnow.pojo.RecordDonationPojo;
import zetrixweb.com.bloodnow.pojo.SignupPojo;

public class UserPageActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView usrMenu, usrLogout;
    private TextView usrNotification, tvCity, tvPhone;
    private TextView usrWelcome;
    private TextView saveLife;
    private LinearLayout usrStatus;
    private TextView usrVerify;
    private TextView usrSite;
    private TextView usrFindSite;
    private TextView usrRecord;
    private TextView usrHistory;
    private TextView usrUpdate;
    private TextView usrTvStatus;
    private TextView usrEmergency;

    SignupPojo signupPojo;
    RecordDonationPojo recordDonationPojo;
    private FirebaseAuth auth;
    Calendar c;

    private String[] myString;
    private static final Random rgenerator = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        c = Calendar.getInstance();
        auth = FirebaseAuth.getInstance();

        usrLogout = (ImageView) findViewById(R.id.logout);
        usrNotification = (TextView) findViewById(R.id.usr_notification);
        usrWelcome = (TextView) findViewById(R.id.usr_welcome);
        saveLife = (TextView) findViewById(R.id.saveLife);
        usrStatus = (LinearLayout) findViewById(R.id.usr_status);
        usrTvStatus = (TextView) findViewById(R.id.usr_tvStatus);
        usrEmergency = (TextView) findViewById(R.id.usr_emergency);
        usrFindSite = (TextView) findViewById(R.id.usr_findSite);
        usrRecord = (TextView) findViewById(R.id.usr_record);
        usrHistory = (TextView) findViewById(R.id.usr_history);
        usrUpdate = (TextView) findViewById(R.id.usr_update);
        tvCity = (TextView) findViewById(R.id.usr_tvCity);
        tvPhone = (TextView) findViewById(R.id.usr_tvphone);

        Resources res = getResources();

        myString = res.getStringArray(R.array.quotesArray);

        String q = myString[rgenerator.nextInt(myString.length)];

        ((TextView)findViewById(R.id.quotes)).setText(q);

        usrNotification.setOnClickListener(this);
        usrEmergency.setOnClickListener(this);
        usrFindSite.setOnClickListener(this);
        usrRecord.setOnClickListener(this);
        usrHistory.setOnClickListener(this);
        usrUpdate.setOnClickListener(this);
        usrLogout.setOnClickListener(this);

        signupPojo = CommonUtils.convertJsonToCalGoal(CommonUtils.getStringPref(UserPageActivity.this,CommonUtils.SF_LOGIN_DATA2,""));
        if (!CommonUtils.getStringPref(UserPageActivity.this,CommonUtils.SF_LOGIN_DATA1,"").equals("")) {
            recordDonationPojo = CommonUtils.convertJsonToDonation(CommonUtils.getStringPref(UserPageActivity.this, CommonUtils.SF_LOGIN_DATA1, ""));
        }

        usrWelcome.setText("Welcome " + signupPojo.getFname() + ",");
        tvCity.setText("City : " + signupPojo.getCity());
        tvPhone.setText("Phone : " + signupPojo.getPhone());


        if(signupPojo.getCounter()>0 && recordDonationPojo!= null){
            c.add(Calendar.DATE,-56);
            Date date = c.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String newDate = formatter.format(date);

            try {
                Date currentDate = formatter.parse(newDate);
                Date donationDate = formatter.parse(recordDonationPojo.getDate());
                if(donationDate.before(currentDate)){
                    usrTvStatus.setTextColor(getResources().getColor(R.color.red));
                    usrTvStatus.setText("Your Status is : RED");
                }

                c= Calendar.getInstance();
                c.add(Calendar.DATE,-2);
                newDate = formatter.format(c.getTime());
                currentDate = formatter.parse(newDate);
                donationDate = formatter.parse(recordDonationPojo.getDate());
                if(recordDonationPojo.getType().toString().equals("plasma") && donationDate.before(currentDate)){
                    usrTvStatus.setTextColor(getResources().getColor(R.color.red));
                    usrTvStatus.setText("Your Status is : RED");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        if(signupPojo.getGender().equals("Male")){
            if(signupPojo.getHeight().equals("4'10") && Integer.parseInt(signupPojo.getWeight().toString()) < 50){
                usrTvStatus.setTextColor(getResources().getColor(R.color.amber));
                usrTvStatus.setText("Your Status is : Amber");
            }
            else {
                usrTvStatus.setTextColor(getResources().getColor(R.color.green));
                usrTvStatus.setText("Your Status is : Green");
            }

        }else if(signupPojo.getGender().equals("Female")){
            if(signupPojo.getHeight().equals("4'10") && Integer.parseInt(signupPojo.getWeight().toString()) < 60){
                usrTvStatus.setTextColor(getResources().getColor(R.color.amber));
                usrTvStatus.setText("Your Status is : Amber");
            }
            else if(signupPojo.getHeight().equals("4'11") && Integer.parseInt(signupPojo.getWeight().toString()) < 58){
                usrTvStatus.setTextColor(getResources().getColor(R.color.amber));
                usrTvStatus.setText("Your Status is : Amber");
            }
            else if(signupPojo.getHeight().equals("5'") && Integer.parseInt(signupPojo.getWeight().toString()) < 56){
                usrTvStatus.setTextColor(getResources().getColor(R.color.amber));
                usrTvStatus.setText("Your Status is : Amber");
            }
            else if(signupPojo.getHeight().equals("5'1") && Integer.parseInt(signupPojo.getWeight().toString()) < 54){
                usrTvStatus.setTextColor(getResources().getColor(R.color.amber));
                usrTvStatus.setText("Your Status is : Amber");
            }
            else if(signupPojo.getHeight().equals("5'2") && Integer.parseInt(signupPojo.getWeight().toString()) < 52){
                usrTvStatus.setTextColor(getResources().getColor(R.color.amber));
                usrTvStatus.setText("Your Status is : Amber");
            }
            else if(signupPojo.getHeight().equals("5'3") && Integer.parseInt(signupPojo.getWeight().toString()) < 50){
                usrTvStatus.setTextColor(getResources().getColor(R.color.amber));
                usrTvStatus.setText("Your Status is : Amber");
            }
            else if(signupPojo.getHeight().equals("5'4") && Integer.parseInt(signupPojo.getWeight().toString()) < 48){
                usrTvStatus.setTextColor(getResources().getColor(R.color.amber));
                usrTvStatus.setText("Your Status is : Amber");
            }
            else if(signupPojo.getHeight().equals("5'5") && Integer.parseInt(signupPojo.getWeight().toString()) < 46){
                usrTvStatus.setTextColor(getResources().getColor(R.color.amber));
                usrTvStatus.setText("Your Status is : Amber");
            }
            else {
                usrTvStatus.setTextColor(getResources().getColor(R.color.green));
                usrTvStatus.setText("Your Status is : Green");
            }
        }
        else{
            usrTvStatus.setTextColor(getResources().getColor(R.color.red));
            usrTvStatus.setText("Your Status is : Red");
        }

        if(signupPojo.getHealth().equals("Yes")){
            usrTvStatus.setTextColor(getResources().getColor(R.color.amber));
            usrTvStatus.setText("Your Status is : Amber");
        }else {
            usrTvStatus.setTextColor(getResources().getColor(R.color.green));
            usrTvStatus.setText("Your Status is : Green");
        }

        if(signupPojo.getSmoke().equals("Yes") || signupPojo.getDrink().equals("Yes")){
            usrTvStatus.setTextColor(getResources().getColor(R.color.amber));
            usrTvStatus.setText("Your Status is : Amber");
        }else {
            usrTvStatus.setTextColor(getResources().getColor(R.color.green));
            usrTvStatus.setText("Your Status is : Green");
        }

        usrTvStatus.setOnClickListener(this);
        saveLife.setText("You have potentially saved " + signupPojo.getCounter() + " lives!!");
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case R.id.action_donation_site:
//                Intent siteIntent = new Intent(UserPageActivity.this, DonationSiteActivity.class);
//                startActivity(siteIntent);
//                return(true);
//
//            case R.id.action_record_donation:
//                Intent recordIntent = new Intent(UserPageActivity.this, RecordDonationActivity.class);
//                startActivity(recordIntent);
//                return(true);
//
//            case R.id.action_donation_history:
//                Intent historyIntent = new Intent(UserPageActivity.this, DonationHistoryActivity.class);
//                startActivity(historyIntent);
//                return(true);
//
//            case R.id.action_verify:
//                Intent verifyIntent = new Intent(UserPageActivity.this, VerifyActivity.class);
//                startActivity(verifyIntent);
//                return(true);
//
//            case R.id.action_update_info:
//                Intent updateIntent = new Intent(UserPageActivity.this, UpdateInfoActivity.class);
//                startActivity(updateIntent);
//                return(true);
//
//            case R.id.action_send_notification:
//                Intent notificationIntent = new Intent(UserPageActivity.this, HomeActivity.class);
//                startActivity(notificationIntent);
//                return(true);
//
//        }
//        return(super.onOptionsItemSelected(item));
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.usr_notification:
                Intent notificationIntent = new Intent(UserPageActivity.this, HomeActivity.class);
                startActivity(notificationIntent);
                break;

            case R.id.usr_emergency:
                Intent emergencyIntent = new Intent(UserPageActivity.this, EmergencyActivity.class);
                startActivity(emergencyIntent);
                break;

            case R.id.usr_findSite:
                Intent findSiteIntent = new Intent(UserPageActivity.this, DonationSiteActivity.class);
                startActivity(findSiteIntent);
                break;

            case R.id.usr_update:
                Intent updateIntent = new Intent(UserPageActivity.this, UpdateInfoActivity.class);
                startActivity(updateIntent);
                break;

            case R.id.usr_history:
                Intent historyIntent = new Intent(UserPageActivity.this, DonationHistoryActivity.class);
                startActivity(historyIntent);
                break;

            case R.id.usr_record:
                Intent recordIntent = new Intent(UserPageActivity.this, RecordDonationActivity.class);
                startActivityForResult(recordIntent,1);
                break;

            case R.id.usr_tvStatus:
                Dialog dialog= new Dialog(UserPageActivity.this);
                dialog.setTitle(null);
                dialog.setContentView(R.layout.status_dialog_layout);
                TextView tvStatus = (TextView) dialog.findViewById(R.id.statusMsg);

                if(usrTvStatus.getText().toString().equals("Your Status is : Amber")){
                    tvStatus.setText("Height-Weight requirement not met");

                }
                if(usrTvStatus.getText().toString().equals("Your Status is : Red")){
                    tvStatus.setText("Last Donated 2 days ago");

                }
                if(usrTvStatus.getText().toString().equals("Your Status is : Green")){
                    tvStatus.setText("You are all set!");

                }
                dialog.show();
                break;

            case R.id.logout:
                auth.signOut();
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                signupPojo= CommonUtils.convertJsonToCalGoal(CommonUtils.getStringPref(UserPageActivity.this,CommonUtils.SF_LOGIN_DATA2,""));
                saveLife.setText("You have potentially saved " + signupPojo.getCounter() + " lives!!");
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
