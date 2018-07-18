package com.btp.batchten.cdvsprototype;

import android.app.AlertDialog;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by johnb on 06-Apr-18.
 */

public class GetVaccineData extends AsyncTask<Void, Void, String> {


    MainActivity c;

    private Context mContext;

    public GetVaccineData (Context context){
        mContext = context;
    }


    GetVaccineData(MainActivity c) {
        this.c = c;
    }

    @Override
    protected void onPreExecute() {

        MainActivity.vaccineNames = new ArrayList<>();
        MainActivity.vaccineLinks = new ArrayList<>();
        MainActivity.vaccineAges = new ArrayList<>();

    }

    @Override
    protected String doInBackground(Void... voids) {
        try{

            String link="http://vaxicare.000webhostapp.com/vaccine_data.php";
            String data  = URLEncoder.encode("username", "UTF-8") + "=" +
                    URLEncoder.encode("", "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                    URLEncoder.encode("", "UTF-8");

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
        String splitting[] = result.split("~");
        if(isConnected()) {
            for (int i = 0; i < splitting.length - 1; ) {
                MainActivity.vaccineNames.add(splitting[i++]);
                MainActivity.vaccineLinks.add(splitting[i++]);
                MainActivity.vaccineAges.add(splitting[i++]);
            }
         //   Log.i("Testing", splitting[3]);
        }
        else
        {
            //Toast.makeText(this,"",Toast.LENGTH_LONG).show();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
            alertDialogBuilder.setMessage("We need Active data Connection for working!!\nPlease Turn on data services!!");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            c.startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        c.createActivity();
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
