package com.gcoen.navgo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "";
    Button buttonSCanQr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        buttonSCanQr = findViewById(R.id.btn_scan);




        buttonSCanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(DashboardActivity.this,ScannedBarcodeActivity.class);
              startActivity(intent);
            }
        });
    }


}