package zetrixweb.com.bloodnow.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import zetrixweb.com.bloodnow.R;
import zetrixweb.com.bloodnow.adapters.HistoryAdapter;
import zetrixweb.com.bloodnow.extra.CommonUtils;
import zetrixweb.com.bloodnow.pojo.RecordDonationPojo;

public class DonationHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;

    RecordDonationPojo recordDonationPojo;

    DatabaseReference ref;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_history);

        ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://blood-now.firebaseio.com/");

        recyclerView = (RecyclerView) findViewById(R.id.history_recycle);

        recordDonationPojo = CommonUtils.convertJsonToDonation(CommonUtils.getStringPref(DonationHistoryActivity.this,CommonUtils.SF_LOGIN_DATA1,""));

        recyclerView.setLayoutManager(new LinearLayoutManager(DonationHistoryActivity.this, LinearLayoutManager.VERTICAL, false));
//        historyAdapter = new HistoryAdapter(DonationHistoryActivity.this);

//        FirebaseRecyclerAdapter<RecordDonationPojo,donationViewHolder> adapter = new FirebaseRecyclerAdapter<RecordDonationPojo, donationViewHolder>(
//                RecordDonationPojo.class,
//                R.layout.history_item_layout,
//                donationViewHolder.class,
//                //referencing the node where we want the database to store the data from our Object
//                ref.child("path").child("to").child("data").child("RecordDonation")) {
//            @Override
//            protected void populateViewHolder(donationViewHolder viewHolder, RecordDonationPojo model, int position) {
//
//                viewHolder.tvDate.setText(model.getDate());
//                viewHolder.tvPlace.setText(model.getPlace());
//                viewHolder.tvCity.setText(model.getCity());
//            }
//        };
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot != null && !dataSnapshot.equals("")){
                    GenericTypeIndicator<Map<String, RecordDonationPojo>> genericTypeIndicator = new GenericTypeIndicator<Map<String, RecordDonationPojo>>() {};
                    Map<String, RecordDonationPojo> map = dataSnapshot.child("path").child("to").child("data").child("RecordDonation").getValue(genericTypeIndicator);
                    historyAdapter = new HistoryAdapter(DonationHistoryActivity.this, map);
                    recyclerView.setAdapter(historyAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

//    //ViewHolder for our Firebase UI
//    public static class donationViewHolder extends RecyclerView.ViewHolder{
//
//        TextView tvDate;
//        TextView tvPlace;
//        TextView tvCity;
//
//        public donationViewHolder(View v) {
//            super(v);
//            tvDate = (TextView) v.findViewById(R.id.rd_date);
//            tvPlace = (TextView) v.findViewById(R.id.rd_place);
//            tvCity = (TextView) v.findViewById(R.id.rd_city);
//        }
//    }
}
