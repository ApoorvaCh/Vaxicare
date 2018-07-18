package com.btp.batchten.cdvsprototype;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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

public class PendingList extends AppCompatActivity {

    ArrayList<String> idArray;
    ArrayList<String> nameArray;
    ArrayList<String> fatherArray;
    ArrayList<String> motherArray;
    ArrayList<String> numberArray;
    ArrayList<String> addressArray;
    ProgressBar progressBar;
    Context c;

    public void makeChart() {

        ListView lv = findViewById(R.id.pendingListView);
        lv.setAdapter(new PendingList.MyAdapter(this));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, int ipp, long l) {
                TextView pend = (TextView) view.findViewById(R.id.idTextView1);
                if (!pend.getText().toString().equals("ID")||!pend.getText().toString().equals("")) {
                    Intent i = new Intent(c, ViewChart.class);
                    i.putExtra("open ID", Integer.parseInt(pend.getText().toString()));
                    startActivity(i);
                }
            }
        });
    }

    public class PendingRow {
        public String childId;
        public String childName;
        public String fatherName;
        public String motherName;
        public String numberName;
        public String addressName;


        public PendingRow(String childId, String childName, String fatherName, String motherName, String numberName, String addressName) {
            this.childId = childId;
            this.childName = childName;
            this.fatherName = fatherName;
            this.motherName = motherName;
            this.numberName = numberName;
            this.addressName = addressName;
        }


    }


    class MyAdapter extends BaseAdapter {

        ArrayList<PendingRow> rowArrayList;
        Context c;

        public MyAdapter(Context c) {
            this.c = c;
            rowArrayList = new ArrayList<PendingRow>();
            for (int i = 0; i < idArray.size()-1; i++) {
                rowArrayList.add(new PendingRow(idArray.get(i), nameArray.get(i), fatherArray.get(i), motherArray.get(i), numberArray.get(i), addressArray.get(i)));
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
            View v = li.inflate(R.layout.pending_list_rows, viewGroup, false);
            TextView idText = v.findViewById(R.id.idTextView1);
            TextView nameText = v.findViewById(R.id.nameTextView1);
            TextView fatherText = v.findViewById(R.id.fatherTextView1);
            TextView motherText = v.findViewById(R.id.motherTextView1);
            TextView numberText = v.findViewById(R.id.numberTextView1);
            TextView addressText = v.findViewById(R.id.addressTextView1);

            PendingRow temp = rowArrayList.get(i);

            idText.setText(temp.childId);
            nameText.setText(temp.childName);
            fatherText.setText(temp.fatherName);
            motherText.setText(temp.motherName);
            numberText.setText(temp.numberName);
            addressText.setText(temp.addressName);

            return v;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_list);
        progressBar = findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.GONE);
        c = this;
        new GetPendingData().execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this.getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public class GetPendingData extends AsyncTask<Void, Void, String> {


        String currentDate;

        @Override
        protected void onPreExecute() {
            idArray = new ArrayList<String>();
            nameArray = new ArrayList<String>();
            fatherArray = new ArrayList<String>();
            motherArray = new ArrayList<String>();
            numberArray = new ArrayList<String>();
            addressArray = new ArrayList<String>();
            currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {

                String link = "http://vaxicare.000webhostapp.com/pending_list.php";
                String data = URLEncoder.encode("date", "UTF-8") + "=" +
                        URLEncoder.encode(currentDate, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                return sb.toString();
            } catch (Exception e) {
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
            if (divided.length>5) {
                for (int i = 0; i < divided.length - 1; ) {
                    if(i<divided.length - 1)
                    idArray.add(divided[i++]);
                    if(i<divided.length - 1)
                    nameArray.add(divided[i++]);
                    if(i<divided.length - 1)
                    fatherArray.add(divided[i++]);
                    if(i<divided.length - 1)
                    motherArray.add(divided[i++]);
                    if(i<divided.length - 1)
                    numberArray.add(divided[i++]);
                    if(i<divided.length - 1)
                    addressArray.add(divided[i++]);
                }
                progressBar.setVisibility(View.GONE);

                makeChart();
            }
            else
            {
                Toast.makeText(c, "no pending tasks remaining........"+divided.length, Toast.LENGTH_SHORT).show();
            }

        }
    }
    public boolean isConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        }
        catch(Exception e)
        {

        }
        return false;
    }
}
