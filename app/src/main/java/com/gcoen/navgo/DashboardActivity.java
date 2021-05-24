package com.gcoen.navgo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;


public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "";
    Button buttonSCanQr;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        buttonSCanQr = findViewById(R.id.btn_scan);
        buttonSCanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if (ActivityCompat.checkSelfPermission(DashboardActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(DashboardActivity.this,ScannedBarcodeActivity.class);
                        startActivity(intent);
                    } else {
                        ActivityCompat.requestPermissions(DashboardActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }


            }
        });
    }

}