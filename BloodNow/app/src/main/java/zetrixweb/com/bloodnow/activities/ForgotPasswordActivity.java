package zetrixweb.com.bloodnow.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import zetrixweb.com.bloodnow.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutPassword;

    private TextView resetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_resetPass);

        resetPass = (TextView) findViewById(R.id.resetPass);

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
