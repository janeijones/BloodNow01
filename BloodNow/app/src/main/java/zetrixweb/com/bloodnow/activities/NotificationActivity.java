package zetrixweb.com.bloodnow.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import zetrixweb.com.bloodnow.R;
import zetrixweb.com.bloodnow.adapters.NotificationAdapter;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = (RecyclerView) findViewById(R.id.notification_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false));
        notificationAdapter = new NotificationAdapter(NotificationActivity.this);
        recyclerView.setAdapter(notificationAdapter);

    }
}
