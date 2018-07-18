package com.btp.batchten.cdvsprototype;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ViewChart extends AppCompatActivity {

    int passedID;
    ArrayList<String> idArray;
    ArrayList<String> nameArray;
    ArrayList<String> scheduledDatesArray;
    ArrayList<String> administeredDatesArray;

    ProgressBar progressBar;

    public void makeChart(){

        TextView childID = (TextView) findViewById(R.id.chidID);
        childID.setText("Child No: " + passedID);
        ListView lv = findViewById(R.id.listView);
        lv.setAdapter(new MyAdapter(this));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, int i, long l) {
                TextView pend = (TextView) view.findViewById(R.id.administeredDateTextView);
                if (MainActivity.appUser == false && pend.getText().toString().equals("Pending")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewChart.this);
                    builder.setMessage("Is the vaccine Administered?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int ide) {
                                    TextView tv = (TextView)view.findViewById(R.id.vaccineIDTextView);
                                    new AdministrationTask().execute(""+passedID,tv.getText().toString());
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            }).setNeutralButton("Skip", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    // Create the AlertDialog object and return it
                    builder.create().show();
            }
        }
        });
    }

    public class SingleRow{
        public String vaccineId;
        public String vaccineName;
        public String  scheduledDate;
        public String administeredDate;

        public SingleRow(String vaccineId, String vaccineName, String scheduledDate, String administeredDate){
            this.vaccineId = vaccineId;
            this.vaccineName = vaccineName;
            this.scheduledDate = scheduledDate;
            this.administeredDate = administeredDate;
        }


    }


    class MyAdapter extends BaseAdapter{

        ArrayList<SingleRow> rowArrayList;
        Context c;

        public MyAdapter(Context c){
            this.c = c;
            rowArrayList = new ArrayList<SingleRow>();
            rowArrayList.add(new SingleRow("ID", "Name", "SD", "AD"));
            for(int i=0; i<idArray.size(); i++){
                rowArrayList.add(new SingleRow(idArray.get(i), nameArray.get(i), scheduledDatesArray.get(i), administeredDatesArray.get(i)));
            }
        }


        @Override
        public int getCount() {
            return rowArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return rowArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater li = (LayoutInflater) c.getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.single_row, viewGroup, false);
            final TextView id = v.findViewById(R.id.vaccineIDTextView);
            TextView name = v.findViewById(R.id.vaccineNameTextView);
            TextView schedule = v.findViewById(R.id.scheduledDatreTextView);
            TextView administer = v.findViewById(R.id.administeredDateTextView);

            SingleRow temp = rowArrayList.get(i);

            id.setText(temp.vaccineId);
            name.setText(temp.vaccineName);
            schedule.setText(temp.scheduledDate);
            administer.setText(temp.administeredDate);

            return v;
        }
    }



    public void generateChart(View v) {
        EditText idText = (EditText) findViewById(R.id.editText);
        if (!idText.getText().toString().equals("")) {
            passedID = Integer.parseInt(idText.getText().toString());
            new GetDataTask().execute();
        }
        else
            Toast.makeText(getApplicationContext(),"select a valid number",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_chart);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        passedID = getIntent().getIntExtra("open ID",0);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if(passedID != 0){
            new GetDataTask().execute();
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this.getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public class GetDataTask extends AsyncTask<Void, Void, String>{


        String currentDate;

        @Override
        protected void onPreExecute() {
            idArray = new ArrayList<String>();
            nameArray = new ArrayList<String>();
            scheduledDatesArray = new ArrayList<String>();
            administeredDatesArray = new ArrayList<String>();
            currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{

                String link="http://vaxicare.000webhostapp.com/view_chart.php";
                String data  = URLEncoder.encode("id", "UTF-8") + "=" +
                        URLEncoder.encode(""+passedID, "UTF-8");
                data += "&" + URLEncoder.encode("current_date", "UTF-8") + "=" +
                        URLEncoder.encode(currentDate, "UTF-8");

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
                }

                return sb.toString();
            } catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            String divided[] = result.split("~");
            for (int i=0;i<divided.length-1;){
                idArray.add(divided[i++]);
                nameArray.add(divided[i++]);
                scheduledDatesArray.add(divided[i++]);
                administeredDatesArray.add(divided[i++]);
            }
            progressBar.setVisibility(View.GONE);

            makeChart();


        }
        }

    public class AdministrationTask extends AsyncTask<String, Void, Void>{
        String currentDate;


        @Override
        protected void onPreExecute() {

            currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
        }

        @Override
        protected Void doInBackground(String... string) {
            try{

                String childID = string[0];
                String admisteredID = string[1];



                String link="http://vaxicare.000webhostapp.com/administer.php";
                String data  = URLEncoder.encode("child_id", "UTF-8") + "=" +
                        URLEncoder.encode(childID, "UTF-8");
                data += "&" + URLEncoder.encode("vaccine_month_id", "UTF-8") + "=" +
                        URLEncoder.encode(admisteredID, "UTF-8");
                data += "&" + URLEncoder.encode("administered_date", "UTF-8") + "=" +
                        URLEncoder.encode(currentDate, "UTF-8");

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
                }

            } catch(Exception e){
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            new GetDataTask().execute();
        }
    }


}
