package com.btp.batchten.cdvsprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AnganwadiScreen extends AppCompatActivity {

    public void openMap(View view){
        Intent i = new Intent(this, AnganwadiMap.class);
        startActivity(i);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anganwadi_screen);
    }
}
