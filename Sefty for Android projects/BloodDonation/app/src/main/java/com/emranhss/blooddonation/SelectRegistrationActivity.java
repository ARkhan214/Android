package com.emranhss.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SelectRegistrationActivity extends AppCompatActivity {

    private ImageView donor, recipient;
    private TextView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_registration);

        donor = findViewById(R.id.donor);
        recipient = findViewById(R.id.recipient);
        backButton = findViewById(R.id.backButton);

        donor.setOnClickListener(new View().OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectRegistrationActivity.this, DonorRegistrationActivity.class);
                startActivity(intent);
            }
        });

        recipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectRegistrationActivity.this, RecipientRegistrationActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectRegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}