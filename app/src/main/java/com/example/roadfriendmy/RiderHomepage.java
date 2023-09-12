package com.example.roadfriendmy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RiderHomepage extends AppCompatActivity {
    
    Button Acceptbtn, Declinebtn; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_homepage);
        Acceptbtn = findViewById(R.id.Accept);
        Declinebtn = findViewById(R.id.Decline);

        Acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RiderHomepage.this, "Ride Accepted", Toast.LENGTH_SHORT).show();
            }
        });

        Declinebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RiderHomepage.this, "Ride Declined", Toast.LENGTH_SHORT).show();
            }
        });
    }
}