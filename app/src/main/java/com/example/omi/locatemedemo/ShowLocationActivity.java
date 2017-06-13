package com.example.omi.locatemedemo;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omi.locatemedemo.model.SmsMessage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowLocationActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    public static final int LOCATION_PERMISSION_CODE = 1;

    GoogleMap googleMap;
    PopupWindow popupWindow;
    LayoutInflater inflater;
    TextView locationNameTextView;
    String locationName;
    SmsMessage smsMessage;
    double latitude, longitude;
    LatLng locationPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.smsMessage = (SmsMessage) getIntent().getSerializableExtra("smsMessage");

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.clear();
        this.googleMap.setBuildingsEnabled(true);
        int fineLocationPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (fineLocationPermissionResult == PackageManager.PERMISSION_GRANTED && coarseLocationPermissionResult == PackageManager.PERMISSION_GRANTED) {
            this.googleMap.setMyLocationEnabled(true);
            this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(smsMessage.getBody());
            if(jsonObject.has("latitude"))
                latitude = Double.parseDouble(jsonObject.getString("latitude"));
            if(jsonObject.has("longitude"))
                longitude = Double.parseDouble(jsonObject.getString("longitude"));
            if(jsonObject.has("locationName"))
                locationName = jsonObject.getString("locationName");
            locationPoint = new LatLng(latitude,longitude);
            this.googleMap.addMarker(new MarkerOptions()
                    .position(locationPoint)
                    .title("This is the location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationPoint, 15));
            this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        } catch (JSONException e) {

            final Dialog successfulDialog = new Dialog(this);
            successfulDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            successfulDialog.setContentView(R.layout.error_dialog_layout);
            successfulDialog.setCancelable(true);
            successfulDialog.setCanceledOnTouchOutside(false);
            successfulDialog.getWindow().setLayout(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
            successfulDialog.show();


            successfulDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    finish();
                }
            });

            ImageButton crossBt = (ImageButton) successfulDialog.findViewById(R.id.crossBt);
            TextView errorMessage = (TextView) successfulDialog.findViewById(R.id.errorTv);
            errorMessage.setText("The selected SMS is not a valid SMS for this app. Please select a Location SMS.");
            crossBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    successfulDialog.cancel();
                    finish();

                }
            });

            e.printStackTrace();
            return;
        }
        if(this.inflater == null)
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.custom_layout,null);
        final TextView latitudeTextView = (TextView)customView.findViewById(R.id.latitudeTextView);
        TextView longitudeTextView = (TextView)customView.findViewById(R.id.longitudeTextView);
        this.locationNameTextView = (TextView)customView.findViewById(R.id.locationNameTextView);
        Button sendButton = (Button) customView.findViewById(R.id.sendButton);
        sendButton.setVisibility(View.GONE);
        CardView cardView = (CardView) customView.findViewById(R.id.card_view);
        cardView.setOnClickListener(this);
        latitudeTextView.setText("latitude: "+latitude);
        longitudeTextView.setText("longitude: "+longitude);
        locationNameTextView.setText(locationName);




        if(this.popupWindow != null)
            this.popupWindow.dismiss();


        this.popupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        final View rootView = findViewById(R.id.rootView);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,15);

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == LOCATION_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    this.googleMap.setMyLocationEnabled(true);
                    this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                }
                else
                {
                    Toast.makeText(this,"Oops you just denied the permissions",Toast.LENGTH_LONG).show();
                }


            }
            else
            {

                Toast.makeText(this,"Oops you just denied the permissions",Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.card_view:
            {
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationPoint, 15));
                this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                break;

            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_user_manual) {
            Intent intent = new Intent(this,UserManualActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

