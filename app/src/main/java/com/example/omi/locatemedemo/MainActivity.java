package com.example.omi.locatemedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button sendLocationButton,getLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        this.sendLocationButton = (Button)findViewById(R.id.sendLocationButton);
        this.getLocationButton = (Button)findViewById(R.id.getLocationButton);

        this.sendLocationButton.setOnClickListener(this);
        this.getLocationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.sendLocationButton:
            {
                sendLocation();
                break;
            }
            case R.id.getLocationButton:
            {
                getLocation();
                break;
            }
        }

    }


    private void sendLocation()
    {
        Intent intent = new Intent(this,SendLocationActivity.class);
        startActivity(intent);
    }

    private void getLocation()
    {
        Intent intent = new Intent(this,GetLocationActivity.class);
        startActivity(intent);
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
