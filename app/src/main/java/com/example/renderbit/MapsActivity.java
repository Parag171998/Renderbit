package com.example.renderbit;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String lat;
    String longi;
    String name;
    EditText otpText;
    Button check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Intent intent = getIntent();

        lat = intent.getStringExtra("lat");
        longi = intent.getStringExtra("long");
        name = intent.getStringExtra("name");
        final int otp = intent.getIntExtra("otp", -1);

        otpText = findViewById(R.id.otpText);
        check = findViewById(R.id.button);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!otpText.getText().toString().isEmpty())
                {
                   if(Integer.valueOf(otpText.getText().toString()).intValue() == otp)
                   {
                       Toast.makeText(MapsActivity.this, "OTP is correct!", Toast.LENGTH_SHORT).show();
                   }
                   else
                   {
                       Toast.makeText(MapsActivity.this, "OTP is not correct!", Toast.LENGTH_SHORT).show();
                   }
                }
                else {
                    Toast.makeText(MapsActivity.this, "Please enter the OTP!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(longi));
        mMap.addMarker(new MarkerOptions().position(sydney).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 3.5f ) );

    }
}
