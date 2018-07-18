package com.btp.batchten.cdvsprototype;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;

public class AnganwadiMap extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    HashSet<LatLng> anganwadiSet;
    LocationManager locationManager;
    LocationListener locationListener;
    LatLng userLocation;

    @SuppressLint("MissingPermission")
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {


            AddLocation();
        }
        else{
            Log.i("Testing","Denied-OutsideORPR");

        }
    }

    public AnganwadiMap(){

    }

    public static AnganwadiMap newInstance(int sectionNumber) {
        AnganwadiMap fragment = new AnganwadiMap();
        Bundle args = new Bundle();
        args.putInt("eheh", sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_anganwadi_map, container, false);
        setRetainInstance(true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng((double)17,(double)82),6));

        anganwadiSet = new HashSet<LatLng>();
        anganwadiSet.add(new LatLng(17.7343056, 83.2627402));
        anganwadiSet.add(new LatLng(17.7676257, 83.2515705));
        anganwadiSet.add(new LatLng(17.7687757, 83.2394124));
        anganwadiSet.add(new LatLng(17.6914814, 83.2200971));
        anganwadiSet.add(new LatLng(17.7412656, 83.1543448));
        anganwadiSet.add(new LatLng(17.7641914, 83.1522868));
        anganwadiSet.add(new LatLng(17.9558318, 83.1960381));
        anganwadiSet.add(new LatLng(17.9627321, 83.2064363));
        anganwadiSet.add(new LatLng(17.8020118, 82.9978973));
        anganwadiSet.add(new LatLng(17.917375, 83.0106746));
        anganwadiSet.add(new LatLng(17.8392561, 82.9302754));
        anganwadiSet.add(new LatLng(18.1059724, 83.3262609));
        anganwadiSet.add(new LatLng(18.0941823, 83.3151777));
        anganwadiSet.add(new LatLng(18.108271, 83.3537999));
        anganwadiSet.add(new LatLng(18.1211727, 83.3379202));
        /*hyderabad anganwadi centers*/
        anganwadiSet.add(new LatLng(17.3882353, 78.4611854));
        anganwadiSet.add(new LatLng(17.416392, 78.4883344));
        anganwadiSet.add(new LatLng(17.30713, 78.5262702));
        anganwadiSet.add(new LatLng(17.4407416, 78.5365525));
        anganwadiSet.add(new LatLng(17.3734246, 78.4391803));
        anganwadiSet.add(new LatLng(17.3842403, 78.4932636));
        anganwadiSet.add(new LatLng(17.448926,78.4827078 ));
        anganwadiSet.add(new LatLng(17.4357028,78.4327648 ));
        anganwadiSet.add(new LatLng(17.4569541, 78.401105));


        return rootView;
    }

    @SuppressLint("MissingPermission")
    public void AddLocation(){

        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.i("Testing","Moved");

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),10));

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        Log.i("Testing","requsetLocationUpdates");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!= null) {
            Log.i("Testing", "getLastKnownLocation");
            locationListener.onLocationChanged(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
        else{
            Toast.makeText(getContext(),"Click the location button on top right", Toast.LENGTH_LONG);
        }
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Log.i("Testing","setOnMyLocationButtonClickListener");
                return false;
            }
        });
        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                Log.i("Testing","setOnMyLocationClickListener");
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int i = 1;
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng((double)17,(double)82),6));


        for (LatLng center : anganwadiSet) {
            mMap.addMarker(new MarkerOptions().position(center).title("Anganwadi " + i++));
        }


        double averageLatitude=0;
        double averageLongitude=0;

        for(LatLng calc : anganwadiSet){
            averageLatitude = averageLatitude + calc.latitude/anganwadiSet.size();
            averageLongitude = averageLongitude + calc.longitude/anganwadiSet.size();
        }


        if(Build.VERSION.SDK_INT < 23){
            AddLocation();
        }
        else {


            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.i("Testing", "Denied-reuest now");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                Log.i("Testing", "Granted automatic");

                AddLocation();
            }
        }

   }
}
