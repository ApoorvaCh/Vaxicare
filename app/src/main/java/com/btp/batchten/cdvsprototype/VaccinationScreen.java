package com.btp.batchten.cdvsprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class VaccinationScreen extends AppCompatActivity {

    public void addChildOnClick(View view){

        Intent intent = new Intent(this, AddChild.class);
        startActivity(intent);

    }

    public void viewChartOnClick(View view){

        Intent intent = new Intent(this, ViewChart.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_vaccination);
    }
}
