package com.btp.batchten.cdvsprototype;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;

public class AddChild extends AppCompatActivity {

    EditText date;
    static ProgressBar progressBar;
    static Context c;

    public void submitDetails(View v){

        EditText name = findViewById(R.id.babyName);
        EditText date = findViewById(R.id.dateEditText);
        EditText father = findViewById(R.id.fatherText);
        EditText mother = findViewById(R.id.motherText);
        EditText number = findViewById(R.id.phoneText);
        EditText address = findViewById(R.id.addressText);

        if(name.getText().toString().equals("")||date.getText().toString().equals("")||father.getText().toString().equals("")||mother.getText().toString().equals("")||number.getText().toString().equals("")||address.getText().toString().equals("")) {
            Toast.makeText(c,"fill complete details and then submit",Toast.LENGTH_LONG).show();
        }
        else
        {
            String nameString = name.getText().toString();
            String dateString = date.getText().toString();
            String fatherString = father.getText().toString();
            String motherString = mother.getText().toString();
            String numberString = number.getText().toString();
            String addressText = address.getText().toString();

            new UpdatingTask().execute(nameString, dateString, fatherString, motherString, numberString, addressText);
        }

    }

    public void showDatePicker(final View v){

        Calendar c = Calendar.getInstance();

        Log.i("Reached","Yes");


        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String day=Integer.toString(i2);
                String month=Integer.toString(i1);
                if(i1<10){
                    month="0"+month;
                }
                if(i2<10){
                    day="0"+day;
                }

                date.setText(i+"-"+month+"-"+day);
                v.clearFocus();
                findViewById(R.id.babyName).clearFocus();
                findViewById(R.id.Submit).requestFocus();

            }
        };

        new DatePickerDialog(this,onDateSetListener,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();




    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        c = this;
        progressBar = findViewById(R.id.progressBar3);
        final TextView dateEditText = (TextView)findViewById(R.id.dateEditText);
        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==true){
                    showDatePicker(dateEditText);
                }
            }
        });
        date = (EditText) findViewById(R.id.dateEditText);
        EditText editText = findViewById(R.id.addressText);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                }
            }
        });
    }

    public static class UpdatingTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... strings) {
            try{
                String name = (String)strings[0];
                String date = (String)strings[1];
                String father = (String) strings[2];
                String mother = (String) strings[3];
                String number = (String)  strings[4];
                String address = (String) strings[5];

                String link="http://vaxicare.000webhostapp.com/add_child.php";
                String data  = URLEncoder.encode("name", "UTF-8") + "=" +
                        URLEncoder.encode(name, "UTF-8");
                data += "&" + URLEncoder.encode("date", "UTF-8") + "=" +
                        URLEncoder.encode(date, "UTF-8");
                data += "&" + URLEncoder.encode("father", "UTF-8") + "=" +
                        URLEncoder.encode(father, "UTF-8");
                data += "&" + URLEncoder.encode("mother", "UTF-8") + "=" +
                        URLEncoder.encode(mother, "UTF-8");
                data += "&" + URLEncoder.encode("number", "UTF-8") + "=" +
                        URLEncoder.encode(number, "UTF-8");
                data += "&" + URLEncoder.encode("address", "UTF-8") + "=" +
                        URLEncoder.encode(address, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                return Integer.parseInt(sb.toString());
            } catch(Exception e){
                return 0;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer result) {
            progressBar.setVisibility(View.GONE);
            Intent i = new Intent(c,ViewChart.class);
            i.putExtra("open ID", result);
            c.startActivity(i);
        }
    }

}
